package com.jerry.network.request

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jerry.network.ApiResponse
import com.jerry.network._io_main
import com.jerry.network.callback.CallBack
import com.jerry.network.func.ApiResponseFunc
import com.jerry.network.func.RetryExceptionFunc
import com.jerry.network.getKGenericType
import com.jerry.network.io_main
import com.jerry.network.subscribe.CallBackSubscriber
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.lang.Exception

class PostRequest :BaseBodyRequest<PostRequest> {

    constructor(url:String):super(url)


    fun <T> execute(callback: CallBack<T>) :Disposable{
        return build().toObservable(generateRequest(),callback)
            .subscribeWith(CallBackSubscriber(callback))
    }

    inline fun <reified T> getObservable() :Observable<T>{
        return build().toObservable(generateRequest())
    }

    inline fun <T> toObservable(observable: Observable<ResponseBody>,callBack: CallBack<T>):Observable<T> {
        return observable.map(ApiResponseFunc<T>(callBack.getGenericityType()))
            .compose(io_main())
            .retryWhen(RetryExceptionFunc(retryCount,retryDelay,retryIncreaseDelay))
    }

    inline fun <reified T> toObservable(observable: Observable<ResponseBody>):Observable<T> {
        return observable.map(object :Function<ResponseBody,T>{
            override fun apply(t: ResponseBody): T? {
                var json = t.string()
                var result:T = Gson().fromJson(json, getKGenericType<T>())
                return result
            }
        })
            .compose(io_main())
            .retryWhen(RetryExceptionFunc(retryCount,retryDelay,retryIncreaseDelay))
    }
}