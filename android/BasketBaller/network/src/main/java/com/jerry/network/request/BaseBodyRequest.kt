package com.jerry.network.request

import androidx.annotation.NonNull
import com.jerry.network.HttpParam
import com.jerry.network.UploadProgressRequestBody
import com.jerry.network.callback.ProgressResonseCallBack
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import java.io.File

abstract class BaseBodyRequest<R:BaseBodyRequest<R>> :BaseRequest<R>{

    protected var textcontent:String?=null //上传的文本内容
    protected var mediaType:MediaType?=null //
    protected var json :String?=null //上传的json
    protected var bs :ByteArray?=null//上传的字节数组
    protected var any:Any?=null //上传的对象
    protected var requestBody:RequestBody?=null //自定义的请求体

    protected val url:String
    enum class UploadType{
        /**
         * MultipartBody.Part方式上传
         */
        PART,
        /**
         * Map RequestBody方式上传
         */
        BODY
    }

    private var currentUploadType = UploadType.PART

    constructor(url:String){
        this.url = url
    }

    fun requestBody(requestBody: RequestBody):R{
        this.requestBody = requestBody
        return this as R
    }

    fun upString(string: String):R{
        this.textcontent = string
        this.mediaType = okhttp3.MediaType.parse("text/plain")
        return this as R
    }

    fun upString(string: String,@NonNull mediaType:String) :R{
        this.textcontent = string
        this.mediaType = okhttp3.MediaType.parse(mediaType)
        return this as R
    }

    fun upObject(@Body any: Any):R{
        this.any = any
        return this as R
    }

    fun upJson(json:String):R{
        this.json = json
        return  this as R
    }

    fun upBytes(bs:ByteArray):R{
        this.bs = bs
        return this as R
    }

    fun params(key:String,file:File,responseCallback:ProgressResonseCallBack):R{
        params.put(key,file,responseCallback)
        return this as R
    }

    fun addFileParams(key: String, files: List<File>, responseCallBack: ProgressResonseCallBack): R {
        params.putFileParams(key, files, responseCallBack)
        return this as R
    }

    fun addFileWrapperParams(key: String, fileWrappers: List<HttpParam.FileWrapper>): R {
        params.putFileWrapperParams(key, fileWrappers)
        return this as R
    }

    fun params(key: String, file: File, fileName: String, responseCallBack: ProgressResonseCallBack): R {
        params.put(key, file, fileName, responseCallBack)
        return this as R
    }

    fun uploadType(uploadType:UploadType):R{
        currentUploadType = uploadType
        return this as R
    }

    override fun generateRequest(): Observable<ResponseBody> {
        requestBody?.let {
            return apiService.postBody(url,it)
        }
        json?.let {
            var body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),it)
            return apiService.postJson(url,body)
        }
        any?.let {
            return apiService.postBody(url,it)
        }
        textcontent?.let {
            var body = RequestBody.create(mediaType,it)
            return apiService.postBody(url,body)
        }
        bs?.let {
            var body = RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"),it)
            return apiService.postBody(url,body)
        }
        if(params.fileParamsMap.isEmpty()) return apiService.postBody(url,params.urlParamsMap)

        if (currentUploadType == UploadType.PART){
            return uploadFilesWithParts()
        }else {
            return uploadFilesWithBodys()
        }
    }

    protected fun uploadFilesWithParts():Observable<ResponseBody>{
        var parts = arrayListOf<MultipartBody.Part>()
        params.urlParamsMap.entries.forEach {
            parts.add(MultipartBody.Part.createFormData(it.key,it.value))
        }

        params.fileParamsMap.entries.forEach {
            var entry  = it
            it.value.forEach {
                parts.add(addFile(entry.key,it))
            }
        }
        return apiService.uploadFiles(url,parts)
    }

    protected fun uploadFilesWithBodys():Observable<ResponseBody>{
        var bodyMap = hashMapOf<String,RequestBody>()
        params.urlParamsMap.entries.forEach {
            var body = RequestBody.create(MediaType.parse("text/plain"),it.value)
            bodyMap.put(it.key,body)
        }
        params.fileParamsMap.entries.forEach {
            var entry = it
            it.value.forEach {
                var requestBody = getRequestBody(it)
                var uploadProgressRequestBody = UploadProgressRequestBody(requestBody,it.responseCallBack)
                bodyMap.put(entry.key,uploadProgressRequestBody)
            }
        }
        return apiService.uploadFiles(url,bodyMap)

    }

    private fun addFile(key: String,fileWrapper:HttpParam.FileWrapper):MultipartBody.Part{
        var requestBody = getRequestBody(fileWrapper)
        fileWrapper.responseCallBack?.let {
            var uploadProgressRequestBody = UploadProgressRequestBody(requestBody,it)
            return MultipartBody.Part.createFormData(key,fileWrapper.fileName,uploadProgressRequestBody)
        }
        return MultipartBody.Part.createFormData(key,fileWrapper.fileName,requestBody)


    }

    fun getRequestBody(fileWrapper: HttpParam.FileWrapper):RequestBody?{
        return RequestBody.create(fileWrapper.contentType,fileWrapper.file)
    }


}