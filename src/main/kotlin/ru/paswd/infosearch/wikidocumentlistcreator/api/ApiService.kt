package ru.paswd.infosearch.wikidocumentlistcreator.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiService {
    private var retrofit: Retrofit? = null

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
//            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getApi(): Api {
        return retrofit!!.create(Api::class.java)
    }
}