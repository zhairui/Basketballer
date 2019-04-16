package com.jerry.network.callback

/**
 * 上传进度回调接口
 */
interface ProgressResonseCallBack {

    fun onResponseProgress(bytesWritten:Long,contentLength:Long,done:Boolean)
}