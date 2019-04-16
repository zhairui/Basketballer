package com.jerry.login.viewmodels

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.jerry.base.BaseViewModel
import com.jerry.commonservice.entity.UserInfo
import com.jerry.commonservice.service.IUserInfoService
import com.jerry.login.entity.Token
import com.jerry.network.ApiResponse
import com.jerry.network.LoadingDefault
import com.jerry.network.callback.CallBack
import com.jerry.network.NetRequest
import com.jerry.network.callback.ProgressCallBack
import com.jerry.network.exception.ApiException
import com.jerry.network.subscribe.CallBackSubscriber
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

class LoginViewModel :BaseViewModel() {

    @Autowired(name = "/user/UserInfoServiceImpl")
    @JvmField var userInfoService:IUserInfoService?=null

    var username = ObservableField<String>()
    var password = ObservableField<String>()

    init {
        ARouter.getInstance().inject(this)
        userInfoService = ARouter.getInstance().navigation(IUserInfoService::class.java)
    }
    fun login(v:View){
        if(TextUtils.isEmpty(username.get())){
            ToastUtils.showShort("请输入手机号")
            //return
        }
        if(TextUtils.isEmpty(password.get())){
            ToastUtils.showShort("请输入密码")
            //return
        }
        NetRequest.getInstance()
            .post("/user/login")
            .params("username","15011111111")
            .params("password","123")
            .getObservable<ApiResponse<Token>>()
            .flatMap(object :Function<ApiResponse<Token>,Observable<ApiResponse<UserInfo>>>{
                override fun apply(t: ApiResponse<Token>): Observable<ApiResponse<UserInfo>>? {
                    var tokenHeader = t.data.tokenHeader
                    var token = t.data.token
                    var map = linkedMapOf<String,String>()
                    if(!TextUtils.isEmpty(tokenHeader) && !TextUtils.isEmpty(token)){
                        map.put(tokenHeader,token)
                    }
                    NetRequest.getInstance().addCommonHeaders(map)
                    return userInfoService?.getUserInfo("15011111111")
                }

            })
            .subscribeWith(CallBackSubscriber(object :ProgressCallBack<ApiResponse<UserInfo>>(v.context){
                override fun onSuccess(t: ApiResponse<UserInfo>) {

                }
            }))

    }
}