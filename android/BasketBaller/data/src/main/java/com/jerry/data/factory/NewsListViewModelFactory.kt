package com.jerry.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jerry.data.repository.BaseRepository
import com.jerry.data.repository.NewsRepository

class NewsListViewModelFactory(private val repository:NewsRepository):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NewsRepository::class.java).newInstance(repository)
    }
}