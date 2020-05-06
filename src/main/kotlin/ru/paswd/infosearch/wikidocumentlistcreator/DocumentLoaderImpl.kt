package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.ContentResponse
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnGotContentListener
import ru.paswd.infosearch.wikidocumentlistcreator.logger.Logger
import ru.paswd.infosearch.wikidocumentlistcreator.utils.StringUtils

class DocumentLoaderImpl(val shift: Int) : DocumentLoader {

    override fun load(pageId: Long, listener: OnGotContentListener) {

        ApiService.getApi()
            .getPageContentById(pageId)
            .enqueue(object : Callback<ContentResponse> {

                override fun onResponse(call: Call<ContentResponse>, response: Response<ContentResponse>) {
                    if (response.body() != null && response.body()?.data != null && response.body()?.data?.title != null)
                        listener.onResult(response.body()!!.data!!.title!!, toJson(response.body()!!.data!!))
                    else
                        listener.onResult("", "")
                }

                override fun onFailure(call: Call<ContentResponse>, t: Throwable) {
                    Logger.error("Loading document with id \"$pageId\" failed. Message: ${t.message}")
                    listener.onResult("", "")
                }

            })
    }

    private fun toJson(data: ContentResponse.Data): String {
        var res = "${getSpace()}{\n"
        res += "${getSpace()}${StringUtils.getSpace()}\"title\": \"${StringUtils.convertJsonSpecialChars(
            StringUtils.removeMarkup(data.title))}\",\n"
        res += "${getSpace()}${StringUtils.getSpace()}\"pageId\": ${data.pageId},\n"
        res += "${getSpace()}${StringUtils.getSpace()}\"text\": \"${StringUtils.convertJsonSpecialChars(
            StringUtils.removeMarkup(data.text))}\"\n"
        res += "${getSpace()}}"

        return res
    }

    private fun getSpace(): String {
        return StringUtils.getSpace(shift)
    }
}