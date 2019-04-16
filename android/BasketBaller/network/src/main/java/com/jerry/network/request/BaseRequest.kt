package com.jerry.network.request

import com.jerry.network.ApiService
import com.jerry.network.HeadersManager
import com.jerry.network.HttpParam
import com.jerry.network.NetRequest
import com.jerry.network.interceptor.HeadersInterceptor
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

abstract class BaseRequest<R:BaseRequest<R>> {

    protected var readTimeOut:Long =0
    protected var writeTimeOut:Long = 0
    protected var connectTimeOut:Long =0
    protected val params = HttpParam()
    protected var retryCount = 0
    protected var retryDelay = 0L
    protected var retryIncreaseDelay = 0L
    protected val converterFactories = arrayListOf<Converter.Factory>()
    protected val adapterFactories = arrayListOf<CallAdapter.Factory>()
    protected val headers :HeadersManager = HeadersManager()
    lateinit  var apiService:ApiService

    constructor(){
        retryCount = NetRequest.getInstance().getRetryCount()
        retryDelay = NetRequest.getInstance().getRetryDelay()
        retryIncreaseDelay = NetRequest.getInstance().getRetryIncreaseDelay()

        headers.put(NetRequest.getInstance().getCommonHeaders().getHeaders())
    }

    fun readTimeOut(readTimeOut:Long):R{
        this.readTimeOut = readTimeOut
        return this as R
    }

    fun writeTimeOut(writeTimeOut:Long):R{
        this.writeTimeOut = writeTimeOut
        return this as R
    }

    fun connectTimeOut(connectTimeOut:Long):R{
        this.connectTimeOut = connectTimeOut
        return this as R
    }

    fun params(key:String,value:String):R{
        params.put(key,value)
        return this as R
    }

    fun params(param:HttpParam):R{
        params.put(param)
        return this as R
    }

    fun params(map: Map<String,String>):R{
        params.put(map)
        return this as R
    }

    fun removeParam(key: String):R{
        params.remove(key)
        return this as R
    }

    fun removeAllParams(): R {
        params.clear()
        return this as R
    }

    fun addConverterFactory(factory:Converter.Factory):R{
        converterFactories.add(factory)
        return this as R
    }

    fun addCallAdapterFactory(factory:CallAdapter.Factory):R{
        adapterFactories.add(factory)
        return this as R
    }

    fun addHeader(key: String,value: String):R{
        headers.put(key,value)
        return this as R
    }



    fun getOkHttpClientBuilder():OkHttpClient.Builder{
        var newBuilder:OkHttpClient.Builder
        if(readTimeOut ==0L && writeTimeOut ==0L && connectTimeOut ==0L ){
            newBuilder = NetRequest.getInstance().getOkHttpClientBuilder()
        }else{
            newBuilder= NetRequest.getInstance().getOkhttpClient().newBuilder()
            if(readTimeOut>0) newBuilder.readTimeout(readTimeOut,TimeUnit.MILLISECONDS)
            if(writeTimeOut>0) newBuilder.writeTimeout(writeTimeOut,TimeUnit.MILLISECONDS)
            if(connectTimeOut>0) newBuilder.connectTimeout(connectTimeOut,TimeUnit.MILLISECONDS)
        }
        newBuilder.addInterceptor(HeadersInterceptor(headers))
        return newBuilder
    }

    fun build():R{
        var okHttpClient =  getOkHttpClientBuilder().build()
        NetRequest.getInstance().getRetrofitBuilder().client(okHttpClient)
        var retrofit = NetRequest.getInstance().getRetrofitBuilder().build()
        apiService = retrofit.create(ApiService::class.java)

        return this as R
    }

    abstract fun generateRequest():Observable<ResponseBody>
}