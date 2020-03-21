package ru.paswd.infosearch.wikidocumentlistcreator

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.paswd.infosearch.wikidocumentlistcreator.api.ApiService
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.ContentResponse
import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnStringResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.logger.Log
import ru.paswd.infosearch.wikidocumentlistcreator.utils.StringUtils

object ContentLoader {

    private const val SPACE = "    "

    fun load(title: String, listener: OnStringResultListener) {
        ApiService.getApi()
            .getPageContent(title)
            .enqueue(object : Callback<ContentResponse> {

                override fun onResponse(call: Call<ContentResponse>, response: Response<ContentResponse>) {
                    if (response.body() == null || response.body()?.data == null)
                        return

                    listener.onResult(toJson(response.body()!!.data!!))
                }

                override fun onFailure(call: Call<ContentResponse>, t: Throwable) {
                    Log.error("Loading document $title failed. Message: ${t.message}")
                }

            })
    }

    private fun toJson(data: ContentResponse.Data): String {
        var res = "$SPACE{\n"
        res += "${SPACE}${SPACE}\"title\": \"${data.title}\",\n"
        res += "${SPACE}${SPACE}\"pageId\": ${data.pageId},\n"
        res += "${SPACE}${SPACE}\"text\": \"${StringUtils.convertJsonSpecialChars(
            StringUtils.removeMarkup(data.text))}\"\n"
        res += "$SPACE}"

        return res
    }
}