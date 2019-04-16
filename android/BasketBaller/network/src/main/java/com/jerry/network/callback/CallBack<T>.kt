package com.jerry.network.callback

import com.jerry.network.ApiResponse
import com.jerry.network.exception.ApiException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class CallBack<T> {

    abstract fun onStart()

    abstract fun onCompleted()

    abstract fun onError(e:ApiException)

    abstract fun onSuccess(t: T)

    fun getGenericityType():Type{
        var genericSuperclass = javaClass.genericSuperclass
        if(genericSuperclass is ParameterizedType){
            return genericSuperclass.actualTypeArguments[0]
        }else{
            return javaClass
        }
    }
}