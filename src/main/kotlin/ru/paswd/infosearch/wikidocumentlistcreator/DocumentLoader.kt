package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.CategoryMembersResponse
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnLongResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnStringResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.logger.Log
import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils
import java.io.File

class DocumentLoader {

    fun getAllChildren(root: String, file: File, listener: OnLongResultListener)
            = getAllChildren(root, file, -1, true, listener)

    fun getAllChildren(root: String, file: File, level: Int, listener: OnLongResultListener)
            = getAllChildren(root, file, level, true, listener)

    fun getAllChildren(root: String, file: File, level: Int, first: Boolean, listener: OnLongResultListener) {

        onStart(file, first)
        Log.info("Downloading category: \"$root\"")
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
                                        Log.info("Loaded \"$title\"")
                                        if (!(count == 0 && first))
                                            file.appendText(",\n")

                                        file.appendText(it)

                                        count++

                                        if (categories.isEmpty() && count >= categoryMembers.size)
                                            onFinish(file, first, categoryMembers.size.toLong(), listener)
                                    }
                                }
                            })
                        }

                        if (level == 0)
                            return

                        var categoriesCount = 0
                        var childrenCount = 0L

                        categories.forEach {
                            getAllChildren(it, file, if (level == -1) -1 else level - 1, false, OnLongResultListener {
                                synchronized (childrenCount) {
                                    childrenCount += it

                                    synchronized(categoriesCount) {
                                        categoriesCount++

                                        if (categoriesCount == categories.size)
                                            onFinish(file, first, childrenCount + (categoryMembers?.size?.toLong() ?: 0), listener)
                                    }
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

    private fun onFinish(file: File, first: Boolean, count: Long, listener: OnLongResultListener) {
        if (first)
            file.appendText("\n]\n")

        listener.onResult(count)
    }
}