package com.jerry.network.subscribe

import android.annotation.SuppressLint
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.network.ApiResponse
import com.jerry.network.exception.ApiException
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber

abstract class BaseSubscriber<T> :DisposableObserver<T> {

    constructor()

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(!NetworkUtils.isConnected()){
            onComplete()
        }
    }

    override fun onComplete() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
        when(e){
            is ApiException ->{
                onError(e)
            }
            else ->{
                onError(ApiException.handleException(e))
            }
        }
    }

    abstract fun onError(e:ApiException)

}