package com.my.words.network

import com.my.words.beans.YouDaoWord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouDao {
    //https://dict.youdao.com/suggest?q=take&le=eng&doctype=json
    @GET("suggest?le=eng&doctype=json")
    fun suggest(@Query("q") q: String?): Call<YouDaoWord?>
}