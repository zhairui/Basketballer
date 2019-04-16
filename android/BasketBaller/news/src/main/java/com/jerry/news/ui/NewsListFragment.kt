package com.jerry.news.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.jerry.base.BaseFragment
import com.jerry.base.router.RouterMapping
import com.jerry.data.ViewModelInjectorUtils
import com.jerry.news.BR
import com.jerry.news.R
import com.jerry.news.adapter.NewsListAdapter
import com.jerry.news.databinding.NewsFmNewslistBinding
import com.jerry.news.viewmodels.NewsListAdapterViewModel
import com.jerry.news.viewmodels.NewsListViewModel
import kotlinx.android.synthetic.main.news_fm_newslist.*

@Route(path = RouterMapping.NewsModule.NEWSLISTFRAGMENT)
class NewsListFragment : BaseFragment<NewsFmNewslistBinding,NewsListViewModel>(){


    lateinit var newsViewViewModel:NewsListViewModel

    override fun getBindingVariable(): Int {
        return BR.newsViewModel
    }

    override fun getViewModel(): NewsListViewModel {
        newsViewViewModel = ViewModelProviders.of(this,ViewModelInjectorUtils.provideNewsListViewModelFactory(context!!)).get(NewsListViewModel::class.java)
        return newsViewViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.news_fm_newslist
    }

    override fun initView() {
        val adapter = NewsListAdapter()
        getViewDataBinding().newsList.adapter = adapter
        getViewDataBinding().newsList.layoutManager = LinearLayoutManager(context)


        newsViewViewModel.getNewsList(requireContext())
        newsViewViewModel.newsListLiveData.observe(this, Observer {newsList->
            getViewDataBinding().hasNews = (newsList!=null && newsList.isNotEmpty())
            adapter.submitList(newsList)
        })
    }

}