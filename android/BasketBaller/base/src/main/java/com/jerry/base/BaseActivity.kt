package com.jerry.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.jerry.network.TokenInvalidObservableManager
import me.jessyan.autosize.internal.CustomAdapt
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<V:ViewDataBinding,VM:BaseViewModel> : AppCompatActivity(),CustomAdapt{



    protected var binding : V?=null
    private var viewModel: VM? =null
    private var viewModelId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding(savedInstanceState)
        handleTokenInvalid()
    }

    private fun handleTokenInvalid(){
        TokenInvalidObservableManager.getTokenObservable().subscribe {msg->
            ToastUtils.showShort(msg)
        }
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?){
        binding = DataBindingUtil.setContentView(this,initContentView(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if(viewModel==null){
            var modelClass : Class<ViewModel>
            var type = javaClass.genericSuperclass

            if(type is ParameterizedType){
                modelClass = type.actualTypeArguments[1] as Class<ViewModel>
            }else{
                modelClass = BaseViewModel::javaClass as Class<ViewModel>
            }
            viewModel = createViewModel(this,modelClass) as VM
        }
        binding?.setVariable(viewModelId,viewModel)
    }


    abstract fun initContentView(savedInstanceState: Bundle?):Int

    abstract fun initVariableId() :Int

    fun initViewModel():VM?{
        return null
    }

    fun <T: ViewModel> createViewModel(activity: AppCompatActivity,cls:Class<T>):T{
        return ViewModelProviders.of(activity).get(cls)
    }

//    fun <T: ViewModel> createViewModel(activity: AppCompatActivity,cls:Class<T>,factory:ViewModelProvider.Factory?):T{
//        return ViewModelProviders.of(activity,factory).get(cls)
//    }

    fun getViewModel():VM?{
        return viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 667f
    }
}