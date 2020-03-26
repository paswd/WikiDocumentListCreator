package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.CategoryMembersResponse
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.ContentResponse
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnAllThreadsFinishListener
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnLongResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnGotContentListener
import ru.paswd.infosearch.wikidocumentlistcreator.logger.Logger
import ru.paswd.infosearch.wikidocumentlistcreator.utils.StringUtils
import java.io.File
import java.util.*

class GraphParserImpl : GraphParser() {

    companion object {
        private const val ROOT_ELEMENT_ERROR_MSG = "Cannot get root element"
    }

    val queue: Queue<Pair<Long, Int>> = LinkedList<Pair<Long, Int>>()
    val documents = mutableMapOf<Long, Boolean>()

    private var locked = false

    private var file: File? = null
    private var depthLimit = -1
    private var listener: OnLongResultListener? = null

    override fun parse(root: String, file: File, depth: Int, listener: OnLongResultListener) {
        if (locked) {
            Logger.warn("Parser is locked")
            return
        }

        Logger.info("Getting document list...")

        locked = true

        this.file = file
        this.depthLimit = depth
        this.listener = listener

        getRootPageId(root)
    }

    private fun getRootPageId(root: String) {
        ApiService.getApi()
            .getPageContentByTitle(root)
            .enqueue(object : Callback<ContentResponse> {

                override fun onResponse(call: Call<ContentResponse>, response: Response<ContentResponse>) {
                    if (response.body() == null
                        || response.body()?.data == null
                        || response.body()?.data?.pageId == null
                    ) {
                        onFatalError(ROOT_ELEMENT_ERROR_MSG)
                    }

                    queue.add(Pair(response.body()?.data?.pageId ?: -1, 0))

                    iteration(OnResultListener {
                        Logger.info("Documents list successfully created")
                        Logger.info("Starting export to file...")
                        printResults()
                    })
                }

                override fun onFailure(call: Call<ContentResponse>, t: Throwable) {
                    onFatalError(ROOT_ELEMENT_ERROR_MSG)
                }

            })
    }

    private fun iteration(listener: OnResultListener) {
        val levelSize = queue.size
        val currentDepth = queue.peek().second

        if (currentDepth > depthLimit && depthLimit != -1) {
            listener.onResult()
            return
        }

        val threadListener = OnAllThreadsFinishListener(queue.size, OnResultListener {
            var queueSize = 0
            synchronized(queue) {
                queueSize = queue.size
            }

            if (queueSize == 0)
                listener.onResult()
            else
                iteration(listener)
        })

        synchronized(queue) {
            var count = 0
            while (queue.size > 0 && queue.peek().second == currentDepth) {

                val currentElement = queue.remove()

                ApiService.getApi()
                    .getCategoryMembers(currentElement.first)
                    .enqueue(object : Callback<CategoryMembersResponse> {
                        override fun onResponse(
                            call: Call<CategoryMembersResponse>,
                            response: Response<CategoryMembersResponse>
                        ) {

                            if (response.body() == null
                                || response.body()?.query == null
                                || response.body()?.query?.categoryMembers == null
                            ) {
                                Logger.warn("Level ${currentElement.second} | Cannot get elements of category \"${currentElement.first}\". Message: \"Empty body\"")
                                threadListener.append()
                                return
                            }

                            val responseDocuments = response.body()!!.query!!.categoryMembers!!

                            synchronized(queue) {
                                for (document in responseDocuments) {
                                    if (document.pageId == null || document.title == null)
                                        continue

                                    if (document.title!!.contains("Категория:")
                                        && documents[document.pageId!!] != true
                                    ) {
                                        queue.add(Pair(document.pageId!!, currentDepth + 1))
                                    }
                                }
                            }

                            synchronized(documents) {
                                for (document in responseDocuments) {
                                    if (document.pageId == null || document.title == null)
                                        continue

                                    documents[document.pageId!!] = true
                                }
                            }

                            synchronized(count) {
                                count++
                                Logger.info("Category Analysis | Level ${currentElement.second} ($count / $levelSize)")
                            }
                            threadListener.append()
                        }

                        override fun onFailure(call: Call<CategoryMembersResponse>, t: Throwable) {
                            Logger.warn("Level ${currentElement.second} | Cannot get elements of category \"${currentElement.first}\". Message: \"${t.message}\"")
                            threadListener.append()
                        }

                    })
            }
        }
    }

    private fun printResults() {

        val documentLoader: DocumentLoader = DocumentLoaderImpl(2)

        if (file == null) {
            Logger.error("Opening file error")
            onFinish(0)
            return
        }

        synchronized(documents) {

            var count = 0
            var correct = 0

            val size = documents.size

            file!!.writeText("{\n")
            file!!.appendText("${StringUtils.getSpace()}\"total\": $size,\n")
            file!!.appendText("${StringUtils.getSpace()}\"data\":\n")
            file!!.appendText("${StringUtils.getSpace()}[\n")

            val threadListener = OnAllThreadsFinishListener(size, OnResultListener {
                file!!.appendText("\n    ]\n")
                file!!.appendText("}\n")
                onFinish(correct)
            })

            documents.forEach { (pageId, _) ->

                documentLoader.load(pageId, OnGotContentListener { title, content ->

                    synchronized(count) {
                        count++
                        if (content != "") {
                            synchronized(correct) {
                                synchronized(file!!) {

                                    if (correct != 0)
                                        file!!.appendText(",\n")

                                    correct++

                                    file!!.appendText(content)
                                }
                                Logger.info("($count / $size) Saved \"$title\"")
                            }
                        }
                    }

                    threadListener.append()
                })
            }
        }
    }

    private fun onFinish(total: Int) {
        listener?.onResult(total.toLong())
        locked = false
    }

    private fun onFatalError(msg: String) {
        Logger.error(msg)
        onFinish(0)
    }

}