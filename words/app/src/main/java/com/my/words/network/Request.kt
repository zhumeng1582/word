package com.my.words.network

import android.util.Log
import androidx.core.util.Consumer
import com.my.words.beans.BaseResponse
import com.my.words.beans.ErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

abstract class Request {
    val retrofit: Retrofit = RetrofitClient.instance.retrofit

    /**
     * 发送网络请求(异步)
     *
     * @param call call
     */
    fun <T : BaseResponse?> sendRequest(
        call: Call<T>,
        success: Consumer<T?>,
        failed: Consumer<ErrorResponse?>
    ) {
        call.enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                if (response.code() != 200) {
                    Log.w("Http Response", "请求响应错误")
                    failed.accept(ErrorResponse())
                    return
                }
                if (response.body() == null ||
                    response.body()?.result == null ||
                    response.body()?.result?.code != 200
                ) {
                    failed.accept(ErrorResponse())
                    return
                }
                success.accept(response.body())
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                failed.accept(ErrorResponse())
            }
        })
    }
}