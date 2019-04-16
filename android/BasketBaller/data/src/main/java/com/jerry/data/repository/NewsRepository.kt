package com.jerry.data.repository

import com.jerry.data.dao.NewsDao
import com.jerry.data.entity.NewsEntity

class NewsRepository private constructor(private val newsDao:NewsDao):BaseRepository(){

    fun getNewsList() = newsDao.getNewsList()

    fun insertAll(newsList:List<NewsEntity>) = newsDao.insertAll(newsList)

    companion object {

        @Volatile private var instance:NewsRepository?=null

        fun getInstance(newsDao: NewsDao):NewsRepository=
            instance?: synchronized(this){
                instance?:NewsRepository(newsDao).also { instance = it }
            }

    }
}