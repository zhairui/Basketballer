package com.jerry.network

import android.text.TextUtils

class HeadersManager {

    private val headersMap = linkedMapOf<String,String>()

    fun put(key:String,value:String){
        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)){
            return
        }
        headersMap.put(key,value)
    }

    fun put(maps:LinkedHashMap<String,String>){
        maps?.entries.forEach {
            headersMap.put(it.key,it.value)
        }
    }

    fun getHeaders():LinkedHashMap<String,String>{
        return headersMap
    }

    fun isEmpty():Boolean{
        return headersMap.isEmpty()
    }

    fun get(key: String):String{
        return headersMap[key]!!
    }

    fun remove(key: String):String{
        return headersMap.remove(key)!!
    }

    fun clear(){
        headersMap.clear()
    }
}
