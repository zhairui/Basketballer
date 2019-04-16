package com.jerry.network.func

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.jerry.network.ApiResponse
import com.jerry.network.TokenInvalidObservableManager
import com.jerry.network.Utils
import com.jerry.network.exception.ApiException
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.reflect.Modifier
import java.lang.reflect.Type

class ApiResponseFunc<T> :Function<ResponseBody,T> {

    val type:Type
    val gson:Gson
    constructor(type: Type){
        gson = Gson()
        this.type = type
    }
    override fun apply(responseBody: ResponseBody): T?{
        var result:T?=null
        try {
            var json = responseBody.string()
            var jsonObject = JsonParser().parse(json).asJsonObject
            var status = jsonObject.get("status").asInt
            if(status == -401){
                if(jsonObject.get("msg")!=null){
                    var msg = jsonObject.get("msg").asString
                    TokenInvalidObservableManager.getTokenObservable().onNext(msg)
                }
            }
            result = gson.fromJson(json,type)
        }catch (e:Exception){
            e.printStackTrace()
            throw IllegalStateException("json返回解析出错")
        }finally {
            responseBody.close()
        }
        return result
    }
}