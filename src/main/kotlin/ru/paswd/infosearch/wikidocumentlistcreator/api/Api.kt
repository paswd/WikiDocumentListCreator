package ru.paswd.infosearch.wikidocumentlistcreator.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.CategoryMembersResponse
import ru.paswd.infosearch.wikidocumentlistcreator.api.dto.ContentResponse

interface Api {
    companion object {
        const val API_PATH = "/w/api.php"
    }

    @GET(API_PATH)
    fun getCategoryMembers(
        @Query("cmpageid")
        pageId: Long?,

        @Query("cmlimit")
        limit: Int = 500,

        @Query("action")
        action: String = "query",

        @Query("format")
        format: String = "json",

        @Query("formatversion")
        formatVersion: Int = 2,

        @Query("list")
        categoryMember: String = "categorymembers"
    ): Call<CategoryMembersResponse>

    @GET(API_PATH)
    fun getPageContentByTitle(
        @Query("page")
        title: String?,

        @Query("format")
        format: String = "json",

        @Query("formatversion")
        formatVersion: Int = 2,

        @Query("prop")
        prop: String = "text",

        @Query("action")
        action: String = "parse"
    ): Call<ContentResponse>

    @GET(API_PATH)
    fun getPageContentById(
        @Query("pageid")
        pageId: Long?,

        @Query("format")
        format: String = "json",

        @Query("formatversion")
        formatVersion: Int = 2,

        @Query("prop")
        prop: String = "text",

        @Query("action")
        action: String = "parse"
    ): Call<ContentResponse>
}