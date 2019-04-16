package com.jerry.user.serviceImpl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.jerry.commonservice.entity.UserInfo
import com.jerry.commonservice.service.IUserInfoService
import com.jerry.network.ApiResponse
import com.jerry.network.NetRequest
import com.jerry.network.callback.CallBack
import com.jerry.network.exception.ApiException
import io.reactivex.Observable

@Route(path = "/user/UserInfoServiceImpl")
class UserInfoServiceImpl :IUserInfoService {
    override fun init(context: Context?) {

    }

    override fun getUserInfo(username: String): Observable<ApiResponse<UserInfo>>? {
        return getUserInfo()
    }
    fun getUserInfo():Observable<ApiResponse<UserInfo>>?{
        return NetRequest.getInstance()
            .get("/user/getUserInfo")
            .params("username","15011111111")
            .getObservable()
    }
}