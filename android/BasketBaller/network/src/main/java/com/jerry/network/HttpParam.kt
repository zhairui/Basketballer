package com.jerry.network

import com.jerry.network.callback.ProgressResonseCallBack
import okhttp3.MediaType
import java.io.File
import java.io.InputStream
import java.io.Serializable
import java.net.URLConnection

class HttpParam :Serializable {

    val urlParamsMap : LinkedHashMap<String,String> = linkedMapOf()
    val fileParamsMap : LinkedHashMap<String,ArrayList<FileWrapper>> = linkedMapOf()
    fun put(name:String,value:String){
        urlParamsMap.put(name,value)
    }

    fun put(params: HttpParam) {
        params?.let {
            urlParamsMap.putAll(params.urlParamsMap)
            fileParamsMap.putAll(params.fileParamsMap)
        }
    }

    fun put(params: Map<String, String>?) {
        if (params == null || params.isEmpty()) return
        urlParamsMap.putAll(params)
    }

    fun <T : File> put(key: String,file:T,responseCallBack:ProgressResonseCallBack){
        put(key,file,file.name,responseCallBack)
    }

    fun <T : File> put(key: String,file:T,fileName:String,progressResonseCallBack: ProgressResonseCallBack){
        put(key,file,fileName,guessMimeType(fileName),progressResonseCallBack)
    }

    fun put(key: String,fileWrapper:FileWrapper){
        if(key!=null && fileWrapper!=null){
            put(key,fileWrapper.file,fileWrapper.fileName,fileWrapper.contentType,fileWrapper.responseCallBack)
        }
    }

    fun  put(key: String,content:File,fileName: String,contentType:MediaType,responseCallBack: ProgressResonseCallBack){
        key?.let {
            var fileWrappers :ArrayList<FileWrapper>? = fileParamsMap[key]
            if(fileWrappers == null){
                fileWrappers = arrayListOf()
                fileParamsMap.put(key,fileWrappers)
            }
            fileWrappers.add(FileWrapper(content,fileName,contentType,responseCallBack))

        }
    }

    fun <T : File> putFileParams(key: String,files:List<T>,responseCallBack: ProgressResonseCallBack){
        if(key!=null && files!=null && !files.isEmpty()){
            files.forEach {
                put(key,it,responseCallBack)
            }
        }
    }

    fun putFileWrapperParams(key: String,fileWrappers:List<FileWrapper>){
        if(key!=null && fileWrappers!=null && !fileWrappers.isEmpty()){
            fileWrappers.forEach {
                put(key,it)
            }
        }
    }



    fun removeUrl(key: String) {
        urlParamsMap.remove(key)
    }

    fun removeFile(key: String) {
        fileParamsMap.remove(key)
    }

    fun remove( key:String) {
        removeUrl(key)
        removeFile(key)
    }

    fun clear() {
        urlParamsMap.clear()
        fileParamsMap.clear()
    }

    fun guessMimeType(path:String):MediaType{
        var fileNameMap = URLConnection.getFileNameMap()
        var resolvePath = path.replace("#","")
        var contentType = fileNameMap.getContentTypeFor(resolvePath)
        if(contentType == null){
            contentType = "application/octet-stream"
        }
        return MediaType.parse(contentType)!!
    }

    class FileWrapper{
        val file:File
        val fileName:String
        val contentType:MediaType
        val fileSize:Long
        val responseCallBack:ProgressResonseCallBack

        constructor(file:File,fileName: String,contentType: MediaType,responseCallBack: ProgressResonseCallBack){
            this.file = file
            this.fileName = fileName
            this.contentType = contentType
            this.fileSize = file.length()
            this.responseCallBack = responseCallBack
        }
    }
}