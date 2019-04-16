package com.jerry.login.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.jerry.base.BaseActivity
import com.jerry.base.router.RouterMapping
import com.jerry.login.BR
import com.jerry.login.R
import com.jerry.login.databinding.LoginActivityLoginBinding
import com.jerry.login.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.login_activity_login.*

@Route(path = RouterMapping.NewsModule.LOGINACTIVITY)
class LoginActivity : BaseActivity<LoginActivityLoginBinding,LoginViewModel>(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_login.setOnClickListener {
            getViewModel()?.login(it)
        }
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.login_activity_login
    }

    override fun initVariableId(): Int {
        return BR.loginViewModel
    }
}