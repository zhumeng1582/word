package com.my.words.network

import androidx.core.util.Consumer
import com.my.words.beans.ErrorResponse
import com.my.words.beans.YouDaoWord

class YouDaoRequest : Request() {
    fun suggest(q: String, success: Consumer<YouDaoWord?>, failed: Consumer<ErrorResponse?>) {
        val request = retrofit.create(YouDao::class.java)
        val call = request.suggest(q)
        sendRequest(call, success, failed)
    }
}