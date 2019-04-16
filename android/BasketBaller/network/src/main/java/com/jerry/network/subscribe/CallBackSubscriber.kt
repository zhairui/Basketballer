package com.jerry.network.subscribe

import com.blankj.utilcode.util.LogUtils
import com.jerry.network.ApiResponse
import com.jerry.network.callback.CallBack
import com.jerry.network.callback.ProgressCallBack
import com.jerry.network.exception.ApiException

class CallBackSubscriber<T>:BaseSubscriber<T>{

    val mCallBack: CallBack<T>

    constructor(callBack: CallBack<T>){
        this.mCallBack = callBack
        if(mCallBack is ProgressCallBack) mCallBack.subscription(this)
    }

    override fun onStart() {
        super.onStart()
        mCallBack.onStart()
    }

    override fun onError(e: ApiException) {
        mCallBack.onError(e)
    }

    override fun onNext(t: T) {
        super.onNext(t)
        mCallBack.onSuccess(t)
    }

    override fun onComplete() {
        super.onComplete()
        mCallBack.onCompleted()
    }

}