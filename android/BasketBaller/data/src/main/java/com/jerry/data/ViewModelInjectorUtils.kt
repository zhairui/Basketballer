package com.jerry.data

import android.content.Context
import com.jerry.data.factory.NewsListViewModelFactory
import com.jerry.data.repository.NewsRepository

object ViewModelInjectorUtils {

    fun provideNewsListViewModelFactory(context: Context):NewsListViewModelFactory{
        val repository = NewsRepository.getInstance(AppDatabase.getInstance(context).getNewsDao())
        return NewsListViewModelFactory(repository)
    }
}