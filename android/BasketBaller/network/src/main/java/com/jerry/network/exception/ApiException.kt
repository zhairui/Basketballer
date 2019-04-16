package com.jerry.network.exception

import com.google.gson.JsonParseException
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.NotSerializableException
import java.lang.ClassCastException
import java.lang.Exception
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

class ApiException :Exception {

    val code:Int
    var exceptionMessage:String?

    constructor(throwable: Throwable,code:Int):super(throwable){
        this.code = code
        this.exceptionMessage = throwable?.message
    }

    companion object {
        @JvmStatic
        fun handleException(throwable: Throwable):ApiException{
            var ex:ApiException

            when(throwable){
                is HttpException ->{
                    ex = ApiException(throwable,throwable.code())
                }
                is JsonParseException,
                is JSONException,
                is JsonSyntaxException,
                is JsonSerializer<*>,
                is NotSerializableException,
                is ParseException ->{
                    ex = ApiException(throwable,ERROR.PARSE_ERROR)
                    ex.exceptionMessage = "解析错误"
                }
                is ClassCastException ->{
                    ex = ApiException(throwable,ERROR.CAST_ERROR)
                    ex.exceptionMessage = "类型转换错误"
                }
                is ConnectException ->{
                    ex = ApiException(throwable,ERROR.NETWORK_ERROR)
                    ex.exceptionMessage = "连接失败"
                }
                is SSLHandshakeException->{
                    ex = ApiException(throwable,ERROR.SSL_ERROR)
                    ex.exceptionMessage = "证书验证失败"
                }
                is ConnectTimeoutException->{
                    ex = ApiException(throwable,ERROR.TIMEOUT_ERROR)
                    ex.exceptionMessage = "连接超时"
                }
                is SocketTimeoutException ->{
                    ex = ApiException(throwable,ERROR.TIMEOUT_ERROR)
                    ex.exceptionMessage= "连接超时"
                }
                is UnknownHostException->{
                    ex = ApiException(throwable,ERROR.UNKONWNHOST_ERROR)
                    ex.exceptionMessage = "无法解析该域名"
                }
                is NullPointerException ->{
                    ex = ApiException(throwable,ERROR.NULLPOINTER_EXCETPION)
                    ex.exceptionMessage = "空指针异常"
                }
                else->{
                    ex = ApiException(throwable,ERROR.UNKNOWN)
                    ex.exceptionMessage ="未知错误"
                }
            }
            return ex
        }
    }


    class ERROR{
        companion object {
            //未知错误
            val UNKNOWN = 0x01
            //解析错误
            val PARSE_ERROR = UNKNOWN.shl(1)
            //网络错误
            val NETWORK_ERROR = UNKNOWN.shl(2)
            //协议错误
            val HTTP_ERROR = UNKNOWN.shl(3)
            //证书出错
            val SSL_ERROR = UNKNOWN.shl(4)
            //连接超时
            val TIMEOUT_ERROR = UNKNOWN.shl(5)
            //调用错误
            val INVOKE_ERROR = UNKNOWN.shl(6)
            //类型转换错误
            val CAST_ERROR = UNKNOWN.shl(7)
            //请求取消
            val REQUEST_CANCEL = UNKNOWN.shl(8)
            //未知主机错误
            val UNKONWNHOST_ERROR = UNKNOWN.shl(9)
            //空指针错误
            val NULLPOINTER_EXCETPION = UNKNOWN.shl(10)
        }
    }
}