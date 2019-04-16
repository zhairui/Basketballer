package com.jerry.user.ui

import android.os.Bundle
import com.jerry.base.BaseActivity
import com.jerry.user.BR
import com.jerry.user.R
import com.jerry.user.databinding.UserActivityUserBinding
import com.jerry.user.viewmodels.UserViewModel

class UserActivity:BaseActivity<UserActivityUserBinding,UserViewModel>(){
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.user_activity_user
    }

    override fun initVariableId(): Int {
        return BR.userViewModel
    }

}
