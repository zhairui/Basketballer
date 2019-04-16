package com.jerry.network

import com.jerry.network.cookie.CookieManager
import com.jerry.network.interceptor.HeadersInterceptor
import com.jerry.network.request.GetRequest
import com.jerry.network.request.PostRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class NetRequest {

    val DEFAULT_MILLISECONDS = 60000L  //超时时间
    val DEFAULT_RETRY_COUNT = 3 //重试次数
    val DEFAULT_RETRY_DELAY = 500L  //默认重试延时
    val DEFAULT_RETRY_INCREASEDELAY = 0L
    val DEFAULT_CACHE_NEVER_EXPIRE = -1 //缓存过期时间，默认永不缓存

    var baseurl = "http://172.16.236.175:9090/"
    private var mRetryCount = DEFAULT_RETRY_COUNT
    private var mRetryDelay = DEFAULT_RETRY_DELAY
    private var mRetryIncreaseDelay = DEFAULT_RETRY_INCREASEDELAY
    private var cookieJar:CookieManager?=null
    private var headers=HeadersManager()

    private var okhttpClientBuilder:OkHttpClient.Builder
    private var retrofitBuilder:Retrofit.Builder

    companion object {
        @Volatile private var instance:NetRequest? = null
        fun getInstance():NetRequest=
            instance?: synchronized(this){
                instance?:NetRequest().also { instance = it }
            }
    }
    private constructor(){
        okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.hostnameVerifier(DefaultHostnameVerifier())
        okhttpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
        okhttpClientBuilder.readTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
        okhttpClientBuilder.writeTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
        okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.baseUrl(baseurl)
    }


    fun getOkHttpClientBuilder():OkHttpClient.Builder{
        return okhttpClientBuilder
    }

    fun getOkhttpClient():OkHttpClient{
        return okhttpClientBuilder.build()
    }

    fun getRetrofitBuilder():Retrofit.Builder{
        return retrofitBuilder
    }

    fun setBaseUrl(baseurl:String):NetRequest{
        this.baseurl = baseurl
        return this
    }

    fun setCookieStore(cookieManager: CookieManager):NetRequest{
        cookieJar = cookieManager
        okhttpClientBuilder.cookieJar(cookieManager)
        return this
    }

    fun getCookieManager():CookieManager?{
        return cookieJar
    }

    fun setReadTimeOut(readTimeOut:Long):NetRequest{
        okhttpClientBuilder.readTimeout(readTimeOut,TimeUnit.MILLISECONDS)
        return this
    }

    fun setWriteTimeOut(writeTimeOut:Long):NetRequest{
        okhttpClientBuilder.writeTimeout(writeTimeOut,TimeUnit.MILLISECONDS)
        return this
    }

    fun setConnectTimeOut(connectTimeOut:Long):NetRequest{
        okhttpClientBuilder.connectTimeout(connectTimeOut,TimeUnit.MILLISECONDS);
        return this
    }

    fun setRetryCount(retryCount:Int):NetRequest{
        mRetryCount = retryCount
        return this
    }

    fun getRetryCount():Int{
        return mRetryCount
    }

    fun setRetryDelay(retryDelay:Long):NetRequest{
        mRetryDelay = retryDelay
        return this
    }

    fun getRetryDelay():Long{
        return mRetryDelay
    }

    fun setRetryIncreaseDelay(retryIncreaseDelay:Long):NetRequest{
        mRetryIncreaseDelay = retryIncreaseDelay
        return this
    }

    fun getRetryIncreaseDelay():Long{
        return mRetryIncreaseDelay
    }

    fun addCommonHeaders(map: LinkedHashMap<String,String>):NetRequest{
        this.headers.put(map)
        return this
    }

    fun getCommonHeaders():HeadersManager{
        return headers
    }



    fun get(url:String): GetRequest {
        return GetRequest(url)
    }

    fun post(url: String) :PostRequest{
        return PostRequest(url)
    }




    class DefaultHostnameVerifier : HostnameVerifier{
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }
}