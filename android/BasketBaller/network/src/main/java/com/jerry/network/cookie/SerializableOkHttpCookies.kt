package com.jerry.network.cookie

import okhttp3.Cookie
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class SerializableOkHttpCookies :Serializable {

    @Transient
    private val cookies:Cookie
    @Transient
    private var clientCookies:Cookie?=null

    constructor(cookies:Cookie){
        this.cookies = cookies
    }

    fun getCookies():Cookie{
        return if(clientCookies!=null) clientCookies!! else cookies
    }

    fun writeObject(out : ObjectOutputStream){
        out.writeObject(cookies.name())
        out.writeObject(cookies.value())
        out.writeLong(cookies.expiresAt())
        out.writeObject(cookies.domain())
        out.writeObject(cookies.path())
        out.writeBoolean(cookies.secure())
        out.writeBoolean(cookies.httpOnly())
        out.writeBoolean(cookies.hostOnly())
        out.writeBoolean(cookies.persistent())
    }

    fun readObject( ois :ObjectInputStream){
        var name = ois.readObject() as String
        var value = ois.readObject() as String
        var expiresAt = ois.readLong()
        var domain = ois.readObject() as String
        var path = ois.readObject() as String
        var secure = ois.readBoolean()
        var httpOnly = ois.readBoolean()
        var hostOnly = ois.readBoolean()

        var builder = Cookie.Builder()
        builder.name(name)
            .value(value)
            .expiresAt(expiresAt)
            .path(path)
        builder = if (hostOnly) builder.hostOnlyDomain(domain) else builder.domain(domain)
        builder = if(secure) builder.secure() else builder
        builder = if(httpOnly) builder.httpOnly() else builder

        clientCookies = builder.build()
    }
}