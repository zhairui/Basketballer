package com.jerry.network

import com.google.gson.reflect.TypeToken
import com.jerry.network.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

fun <T> io_main():ObservableTransformer<T,T>{
    return ObservableTransformer {upstream ->
        upstream
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable -> Observable.error(ApiException.handleException(t))}
    }
}

fun <T> _io_main():ObservableTransformer<ApiResponse<T>,T>{
    return ObservableTransformer{ upstream ->
        upstream
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(object :Function<ApiResponse<T>,T>{
                override fun apply(t: ApiResponse<T>): T {
                    return t.data
                }
            })
            .onErrorResumeNext { t: Throwable -> Observable.error(ApiException.handleException(t)) }

    }
}

fun <T> _main():ObservableTransformer<ApiResponse<T>,T>{
    return ObservableTransformer { upstream ->
        upstream.map { it.data }
            .onErrorResumeNext(Function<Throwable,Observable<T>>{
                Observable.error(ApiException.handleException(it))
            })
    }
}

inline fun <reified T> getKGenericType() = object : TypeToken<T>(){}.type