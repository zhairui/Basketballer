package com.jerry.news.viewmodels

import android.content.Context
import androidx.databinding.ObservableField
import com.jerry.base.BaseViewModel
import com.jerry.data.entity.NewsEntity

class NewsListAdapterViewModel(context: Context,newsEntity: NewsEntity) :BaseViewModel() {

    val article_title = ObservableField<String>(newsEntity.title)

}