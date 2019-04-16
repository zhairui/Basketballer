package com.jerry.network.callback

interface IProgressView {

    fun show()

    fun dismiss()

    interface CancelListener{
        fun cancelDialog()
    }

}