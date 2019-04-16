package com.jerry.commonview

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LoadingDialog :BaseDialog(){

    override fun layoutRes(): Int {
        return R.layout.commonres_dialog_loading
    }

    companion object {
        @JvmStatic
        fun newInstance():LoadingDialog{
            return LoadingDialog()
        }
    }
    private var exitTime: Long = 0
    override fun onStart() {
        super.onStart()
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK  && event.action ==KeyEvent.ACTION_UP) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    exitTime = System.currentTimeMillis()
                } else {
                    dialog.dismiss()
                }
                true
            }
            false
        }
    }

}