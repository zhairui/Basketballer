package com.jerry.news.viewmodels

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.jerry.base.BaseViewModel
import com.jerry.base.URL
import com.jerry.data.entity.NewsEntity
import com.jerry.data.repository.NewsRepository
import com.jerry.network.ApiResponse
import com.jerry.network.NetRequest
import com.jerry.network.callback.ProgressCallBack
import com.jerry.network.io_main
import com.jerry.network.subscribe.CallBackSubscriber
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class NewsListViewModel :BaseViewModel {

    val newsRepository:NewsRepository

    internal constructor(newsRepository: NewsRepository){
        this.newsRepository = newsRepository
    }

    val newsListLiveData = MutableLiveData<List<NewsEntity>>()



    val newsListFromNetWork = NetRequest
        .getInstance()
        .get(URL.NEWSLIST)
        .params("pageNum",0.toString())
        .params("pageSize",10.toString())
        .getObservable<ApiResponse<List<NewsEntity>>>()
        .flatMap(object:Function<ApiResponse<List<NewsEntity>>,Observable<List<NewsEntity>>>{
            override fun apply(t: ApiResponse<List<NewsEntity>>): Observable<List<NewsEntity>> {
                return Observable.just(t.data)
            }
        })

    fun getNewsList(context: Context){

        val callBack = object:ProgressCallBack<List<NewsEntity>>(context){
            override fun onSuccess(t: List<NewsEntity>) {
                    newsListLiveData.value = t
                    insertNews(t)
                }
            }
        val newsListFromRoom = newsRepository.getNewsList().toObservable()
        Observable.concat(newsListFromRoom,newsListFromNetWork)
            .compose(io_main())
            .subscribeWith(CallBackSubscriber(callBack))

    }

    fun insertNews(news:List<NewsEntity>)= runBlocking{
        GlobalScope.launch {
            newsRepository.insertAll(news)
        }
    }
}