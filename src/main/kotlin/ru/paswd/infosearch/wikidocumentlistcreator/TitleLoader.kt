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

class TitleLoader {

    fun getAllChildrenTitles(root: String, file: File, onResultListener: OnResultListener)
            = getAllChildrenTitles(root, file, -1, onResultListener)

    fun getAllChildrenTitles(root: String, file: File, level: Int, onResultListener: OnResultListener) {
        println("${DateTimeUtils.getCurrentDateTime()} [INFO]  In work: \"$root\"")
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

                        val items = mutableListOf<String>()

                        var count = 0

                        response.body()?.query?.categoryMembers?.forEach { page ->
                            val title = page.title ?: ""
                            val currentCount = count

                            ContentLoader.load(title, OnStringResultListener{
                                synchronized(file) {
                                    if (currentCount != 0)
                                        file.appendText(",\n")

                                    file.appendText(it)
                                }
                            })
                            items.add(title)
                            count++
                        }

                        if (level == 0) {
                            onResultListener.onResult()
                            return
                        }

                        val categories = mutableListOf<String>()

                        items.forEach { title ->
                            if (title.contains("Категория:"))
                                categories.add(title)
                        }

                        if (categories.isEmpty()) {
                            onResultListener.onResult()
                            return
                        }

                        count = 0

                        categories.forEach {
                            getAllChildrenTitles(it, file, if (level == -1) -1 else level - 1, OnResultListener {
                                synchronized (count) {
                                    count++

                                    if (count == categories.size)
                                        onResultListener.onResult()
                                }
                            })
                        }
                    }

                    override fun onFailure(call: Call<CategoryMembersResponse>, t: Throwable) {
                    }

                }
            )
    }
}