package com.jerry.network.cookie

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Cookie
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.experimental.and
import android.R.attr.host
import android.R.id.edit
import android.text.TextUtils
import okhttp3.HttpUrl



class PersistentCookieStore {

    val COOKIE_PREFS = "Cookies_Pres"
    val cookies : HashMap<String,ConcurrentHashMap<String,Cookie>> = hashMapOf()
    val cookiePrefs:SharedPreferences

    constructor(context: Context){
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS,0)
        var map = cookiePrefs.all
        map.entries.forEachIndexed { index, mutableEntry ->
            var cookieNames = (mutableEntry.value as String).split(",")
            cookieNames?.forEach {
                var cookie = cookiePrefs.getString(it,"")
                cookie?.let {
                    var decodedCookie = decodeCookie(cookie)
                    if(!cookies.containsKey(mutableEntry.key)){
                        cookies.put(mutableEntry.key, ConcurrentHashMap())
                    }
                    cookies[mutableEntry.key]?.put(it,decodedCookie)
                }
            }
        }
    }

    protected fun getCookieToken(cookie: Cookie): String {
        return cookie.name() + "@" + cookie.domain()
    }

    fun add(url: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)
        // 添加 host key. 否则有可能抛空.
        if (!cookies.containsKey(url.host())) {
            cookies[url.host()] = ConcurrentHashMap()
        }
        // 删除已经有的.
        if (cookies.containsKey(url.host())) {
            cookies[url.host()]?.remove(name)
        }
        // 添加新的进去
        cookies[url.host()]?.put(name,cookie)
        // 是否保存到 SP 中
        if (cookie.persistent()) {
            val prefsWriter = cookiePrefs.edit()
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies[url.host()]?.keys))
            prefsWriter.putString(name, encodeCookie(SerializableOkHttpCookies(cookie)))
            prefsWriter.apply()
        } else {
            val prefsWriter = cookiePrefs.edit()
            prefsWriter.remove(url.host())
            prefsWriter.remove(name)
            prefsWriter.apply()
        }
    }

    fun addCookies(cookies: List<Cookie>) {
        for (cookie in cookies) {
            val domain = cookie.domain()
            var domainCookies = this.cookies[domain]
            if (domainCookies == null) {
                domainCookies = ConcurrentHashMap()
                this.cookies[domain] = domainCookies
            }
        }
    }

    fun get(url: HttpUrl): List<Cookie> {
        val ret = arrayListOf<Cookie>()
        if (cookies.containsKey(url.host())) {
            var collection = cookies[url.host()]?.values
            collection?.forEach {
                if(isCookieExpired(it)){
                    remove(url,it)
                }else{
                    ret.add(it)
                }
            }
        }
        return ret
    }

    private fun isCookieExpired(cookie: Cookie):Boolean{
        return cookie.expiresAt() < System.currentTimeMillis()
    }

    fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
        return true
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)

        if (cookies.containsKey(url.host()) && cookies[url.host()]?.containsKey(name)!!) {
            cookies[url.host()]?.remove(name)

            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name)
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies[url.host()]?.keys))
            prefsWriter.apply()

            return true
        } else {
            return false
        }
    }

    fun getCookies(): List<Cookie> {
        val ret = arrayListOf<Cookie>()
        for (key in cookies.keys)
            ret.addAll(cookies[key]?.values!!)

        return ret
    }


    /**
     * cookies to string
     */
    protected fun encodeCookie(cookie: SerializableOkHttpCookies?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * String to cookies
     */
    protected fun decodeCookie(cookieString: String): Cookie {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableOkHttpCookies).getCookies()
        } catch (e: IOException) {
            Log.d("NetRequest","IOException in decodeCookie" + e.message)
        } catch (e: ClassNotFoundException) {
            Log.d("NetRequest","ClassNotFoundException in decodeCookie" + e.message)
        }

        return cookie!!
    }

    /**
     * byteArrayToHexString
     */
    protected fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element and 0xff.toByte()
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v.toInt()))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * hexStringToByteArray
     */
    protected fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}