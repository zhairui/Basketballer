package com.jerry.network.callback

import android.content.Context
import com.jerry.network.ApiResponse
import com.jerry.network.LoadingDefault
import com.jerry.network.exception.ApiException
import io.reactivex.disposables.Disposable

abstract class ProgressCallBack<T> :CallBack<T> ,IProgressView.CancelListener{

    val mContext:Context
    val mProgressView :IProgressView
    var disposed:Disposable?=null
    constructor(context: Context,progressView:IProgressView){
        this.mContext = context
        this.mProgressView = progressView
    }

    constructor(context: Context,cancel:Boolean = true){
        this.mContext = context
        mProgressView = LoadingDefault(context,this,cancel)
    }


    override fun onStart() {
        mProgressView.show()
    }

    override fun onCompleted() {
        mProgressView.dismiss()
    }

    override fun onError(e: ApiException) {
        mProgressView.dismiss()
    }

    override fun cancelDialog() {
        mProgressView.dismiss()
        disposed?.let { if(!it.isDisposed) it.dispose() }
    }

    fun subscription(disposable: Disposable){this.disposed = disposable}

}