package com.jerry.network

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.jerry.commonview.LoadingDialog
import com.jerry.network.callback.CallBack
import com.jerry.network.callback.IProgressView
import com.jerry.network.callback.ProgressCallBack


class LoadingDefault:IProgressView{


    val dialog: Dialog
    val context :Context
    val progressBar:ProgressBar
    val cancelListener:IProgressView.CancelListener
    constructor(context: Context,cancelListener: IProgressView.CancelListener,cancel:Boolean=true){
        this.context = context
        this.cancelListener = cancelListener
        progressBar = ProgressBar(context)
        dialog = Dialog(context)
        var view:View = LayoutInflater.from(context).inflate(R.layout.commonres_dialog_loading,null)
        dialog.setContentView(view)
        dialog.setCancelable(cancel)
        dialog.setOnDismissListener {
            cancelListener?.cancelDialog()
        }
    }


    override fun show() {
        dialog?.let { if(!it.isShowing) it.show() }
    }

    override fun dismiss() {
        dialog?.dismiss()
    }

    interface OnDismissListener{
        fun dismiss()
    }

    var dismissListener:OnDismissListener?=null

    fun setOnDismissListener(listener: OnDismissListener){
        this.dismissListener = listener
    }

}