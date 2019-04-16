package com.jerry.network

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

object TokenInvalidObservableManager  {

    private var tokenInvalidObservable:PublishSubject<String> = PublishSubject.create()

    fun getTokenObservable():PublishSubject<String>{
        return tokenInvalidObservable
    }
}