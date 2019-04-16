package com.jerry.network

data class ApiResponse<T>(val status:Int, val msg:String, val data:T)