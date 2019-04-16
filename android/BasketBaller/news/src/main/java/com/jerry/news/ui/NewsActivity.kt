package com.jerry.news.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.jerry.base.BaseActivity
import com.jerry.base.router.RouterMapping
import com.jerry.news.R
import com.jerry.news.databinding.NewsAcNewsactivityBinding
import com.jerry.news.viewmodels.NewsListViewModel


@Route(path = RouterMapping.NewsModule.NEWSACTIVITY)
class NewsActivity : BaseActivity<NewsAcNewsactivityBinding,NewsListViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val newsFragment = ARouter.getInstance().build(RouterMapping.NewsModule.NEWSLISTFRAGMENT).navigation() as Fragmen
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.news_ac_newsactivity
    }

    override fun initVariableId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}