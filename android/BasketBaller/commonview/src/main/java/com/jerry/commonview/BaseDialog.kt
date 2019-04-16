package com.jerry.commonview

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


abstract class BaseDialog: DialogFragment(){

    var dimAmount=0.5f
    var gravity=Gravity.NO_GRAVITY
    var width =0
    var height =0
    @StyleRes
    var animStyle =0
    var outCancel =true
    var layoutView:View? =null
    var fmTag=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE,R.style.dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return when{
            layoutRes()!=-1 -> inflater.inflate(layoutRes(),container)
            layoutView!=null -> layoutView!!
            else            ->
                    throw IllegalArgumentException("must set contentView");
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.let {
            var lp =dialog.window.attributes
            lp?.let {
                it.dimAmount= dimAmount
                it.gravity = gravity

                when{
                    width <=0 -> it.width =WindowManager.LayoutParams.WRAP_CONTENT
                    else -> it.width=width
                }

                when{
                    height<=0 ->it.height=WindowManager.LayoutParams.WRAP_CONTENT
                    else ->it.height=height
                }

            }

            it.attributes=lp
            if(animStyle>0){
                it.setWindowAnimations(animStyle)
            }
        }
        dialog.setCancelable(outCancel)

    }

    fun setD_Width(w:Int) :BaseDialog{
        width = w
        return this
    }
    fun setD_Height(h:Int) :BaseDialog{
        height = h
        return this
    }

    fun setD_DimAmount(d:Float) :BaseDialog{
        dimAmount = d
        return this
    }

    fun setD_Gravity(g:Int) :BaseDialog{
        gravity = g
        return this
    }

    fun setD_OutCancel(c:Boolean) :BaseDialog{
        outCancel = c
        return this
    }

    fun setD_AnimStyle(@StyleRes a:Int) :BaseDialog{
        animStyle = a
        return this
    }
    fun setD_LayoutView(view: View) :BaseDialog{
        layoutView = view
        return this
    }
    fun setD_FgTag(tag:String):BaseDialog{
        fmTag=tag
        return this
    }
    
    fun show(manager:FragmentManager){
        var ft =manager.beginTransaction()
        if(TextUtils.isEmpty(fmTag)){
            throw IllegalArgumentException("must set tag")
        }
        if(dialog!=null && dialog.isShowing){
            return
        }
        if(isAdded){
            ft.show(this).commit()
        }else{
            ft.add(this,fmTag)
            ft.commitAllowingStateLoss()
        }
    }

    open fun initView(view: View){

    }

    @LayoutRes
    protected abstract fun layoutRes():Int

}