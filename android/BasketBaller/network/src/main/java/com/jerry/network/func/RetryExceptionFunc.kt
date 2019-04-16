package com.jerry.network.func

import android.util.Log
import com.jerry.network.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RetryExceptionFunc :Function<Observable<out Throwable>,Observable<*>> {

    var count = 0
    var delay = 500L
    var increaseDelay = 3000L //延迟叠加

    constructor(count :Int,delay:Long){
        this.count = count
        this.delay = delay
    }

    constructor(count :Int,delay:Long,increaseDelay:Long){
        this.count = count
        this.delay = delay
        this.increaseDelay = increaseDelay
    }

    @Throws(Exception::class)
    override fun apply(observable: Observable<out Throwable>): Observable<*> {
        return observable.zipWith(Observable.range(1,count+1),object :BiFunction<Throwable,Int,Wrapper>{
            override fun apply(t1: Throwable, t2: Int): Wrapper {
                return Wrapper(t1,t2)
            }
        })
            .flatMap{t: Wrapper ->
                if(t.index>1)
                    Log.i("NetRequest","重试次数:"+t.index)
                var errorCode = 0
                if(t.throwable is ApiException){
                    errorCode = t.throwable.code
                }
                if(t.throwable is ConnectException
                    ||t.throwable is SocketTimeoutException
                    ||errorCode == ApiException.ERROR.NETWORK_ERROR
                    ||errorCode == ApiException.ERROR.TIMEOUT_ERROR
                    ||t.throwable is TimeoutException
                    && t.index < count +1){
                    Observable.timer(delay + (t.index -1) * increaseDelay,TimeUnit.MILLISECONDS)
                }
                Observable.error<Any>(t.throwable)

        }
    }

    class Wrapper{
        val index:Int
        val throwable:Throwable

        constructor(throwable: Throwable,index:Int){
            this.throwable = throwable
            this.index = index
        }
    }

}