package com.my.words.network

import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://dict.youdao.com/") //基础url,其他部分在GetRequestInterface里
        .client(httpClient())
        .addConverterFactory(GsonConverterFactory.create()) //Gson数据转换器
        .build()

    private fun httpClient(): OkHttpClient {
        return Builder() //                .addInterceptor(new AccessTokenInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    companion object {
        val instance = RetrofitClient()
    }
}