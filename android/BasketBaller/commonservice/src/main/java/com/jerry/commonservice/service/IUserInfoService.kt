package com.jerry.commonservice.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.jerry.commonservice.entity.UserInfo
import com.jerry.network.ApiResponse
import io.reactivex.Observable

interface IUserInfoService : IProvider {
    fun getUserInfo(username:String): Observable<ApiResponse<UserInfo>>?
}
