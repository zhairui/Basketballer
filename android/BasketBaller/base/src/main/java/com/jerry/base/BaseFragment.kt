package com.jerry.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T:ViewDataBinding,V:BaseViewModel> : Fragment() {

    private val mViewDataBinding by lazy {
        DataBindingUtil.inflate<T>(layoutInflater,getLayoutId(),null,false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.setVariable(getBindingVariable(),getViewModel())
        mViewDataBinding?.lifecycleOwner = this
        mViewDataBinding?.executePendingBindings()

        initView()
    }
    abstract fun getBindingVariable():Int

    abstract fun getViewModel():V

    abstract fun initView()

    @LayoutRes
    abstract fun getLayoutId():Int

    fun getViewDataBinding() = mViewDataBinding
}