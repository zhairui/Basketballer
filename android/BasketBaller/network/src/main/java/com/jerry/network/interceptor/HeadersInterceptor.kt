package com.jerry.network.interceptor

import com.jerry.network.HeadersManager
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor :Interceptor{

    val headers:HeadersManager

    constructor(headers:HeadersManager){
        this.headers = headers
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        var builder = chain.request().newBuilder()
        headers.getHeaders().entries.forEach {
            builder.header(it.key,it.value).build()
        }
        return chain.proceed(builder.build())
    }

}