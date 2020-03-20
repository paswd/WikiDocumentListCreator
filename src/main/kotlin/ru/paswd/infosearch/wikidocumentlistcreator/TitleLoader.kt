package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.CategoryMembersResponse
import ru.paswd.infosearch.wikidocumentlistcreator.exceptions.ApiException

class TitleLoader {

    var result = mutableListOf<String>()

    fun getAllChildrenTitles(root: String, onResultListener: OnResultListener)
            = getAllChildrenTitles(root, -1, onResultListener)

    fun getAllChildrenTitles(root: String, level: Int, onResultListener: OnResultListener) {
        ApiService.getApi()
            .getCategoryMembers(root, 500)
            .enqueue(
                object: Callback<CategoryMembersResponse> {

                    override fun onResponse(
                        call: Call<CategoryMembersResponse>,
                        response: Response<CategoryMembersResponse>
                    ) {
                        if (response.body() == null)
                            throw ApiException()

                        synchronized (result) {
                            response.body()?.query?.categoryMembers?.forEach { page ->
                                result.add(page.title ?: "")
                            }
                        }

                        if (level == 0) {
                            onResultListener.onResult()
                            return
                        }

                        val categories = mutableListOf<String>()

                        result.forEach { title ->
                            if (title.contains("Категория:"))
                                categories.add(title)
                        }

                        if (categories.isEmpty()) {
                            onResultListener.onResult()
                            return
                        }

                        var count = 0

                        categories.forEach {
                            getAllChildrenTitles(it, if (level == -1) -1 else level - 1, OnResultListener {
                                synchronized (count) {
                                    count++

                                    if (count == categories.size)
                                        onResultListener.onResult()
                                }
                            })
                        }
                    }

                    override fun onFailure(call: Call<CategoryMembersResponse>, t: Throwable) {
                        throw ApiException()
                    }

                }
            )
    }

    interface OnResultListener {
        fun onResult()

        companion object {
            inline operator fun invoke(crossinline op: () -> Unit) =
                object : OnResultListener {
                    override fun onResult() = op()
                }
        }
    }
}