package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.CategoryMembersResponse
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnStringResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils
import java.io.File

class DocumentLoader {

    fun getAllChildren(root: String, file: File, onResultListener: OnResultListener)
            = getAllChildren(root, file, -1, true, onResultListener)

    fun getAllChildren(root: String, file: File, level: Int, onResultListener: OnResultListener)
            = getAllChildren(root, file, level, true, onResultListener)

    fun getAllChildren(root: String, file: File, level: Int, first: Boolean, onResultListener: OnResultListener) {
        onStart(file, first)
        println("${DateTimeUtils.getCurrentDateTime()} [INFO]  In work: \"$root\"")
        Thread.sleep(100L)
        ApiService.getApi()
            .getCategoryMembers(root, 500)
            .enqueue(
                object: Callback<CategoryMembersResponse> {

                    override fun onResponse(
                        call: Call<CategoryMembersResponse>,
                        response: Response<CategoryMembersResponse>
                    ) {

                        if (response.body() == null)
                            return

                        val categoryMembers = response.body()?.query?.categoryMembers

                        val categories = mutableListOf<String>()

                        if (level != 0) {
                            categoryMembers?.forEach { page ->
                                val title = page.title ?: ""
                                if (title.contains("Категория:"))
                                    categories.add(title)
                            }
                        }

                        var count = 0

                        categoryMembers?.forEach { page ->
                            val title = page.title ?: ""

                            ContentLoader.load(title, OnStringResultListener{
                                synchronized(file) {
                                    synchronized(count) {
                                        println("Loaded \"$title\"")
                                        if (count != 0 || !first)
                                            file.appendText(",\n")

                                        file.appendText(it)

                                        count++

                                        if (categories.isEmpty() && count >= categoryMembers.size)
                                            onFinish(file, first, onResultListener)
                                    }
                                }
                            })
                        }

                        if (level == 0)
                            return

                        count = 0

                        categories.forEach {
                            getAllChildren(it, file, if (level == -1) -1 else level - 1, false, OnResultListener {
                                synchronized (count) {
                                    count++

                                    if (count == categories.size)
                                        onFinish(file, first, onResultListener)
                                }
                            })
                        }
                    }

                    override fun onFailure(call: Call<CategoryMembersResponse>, t: Throwable) {
                        println("Loading category \"$root\" failed. Message: ${t.message}")
                    }

                }
            )
    }

    private fun onStart(file: File, first: Boolean) {
        if (first)
            file.appendText("[\n")
    }

    private fun onFinish(file: File, first: Boolean, onResultListener: OnResultListener) {
        if (first)
            file.appendText("\n]\n")

        onResultListener.onResult()
    }
}