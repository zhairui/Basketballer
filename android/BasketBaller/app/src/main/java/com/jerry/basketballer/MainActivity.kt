package com.jerry.basketballer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.jerry.base.adapter.BaseFragmentPagerAdapter
import com.jerry.base.router.RouterMapping
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var pagerAdapter = BaseFragmentPagerAdapter(supportFragmentManager,createFragmentList())

        viewpager.adapter = pagerAdapter

        pagerAdapter.notifyDataSetChanged()
        ll_bottom.setOnClickListener {
            ARouter.getInstance().build(RouterMapping.NewsModule.LOGINACTIVITY).navigation()
        }

    }


    fun createFragmentList():List<Fragment>{
        val fragmentList :MutableList<Fragment> = mutableListOf()
        val newsFragment = ARouter.getInstance().build(RouterMapping.NewsModule.NEWSLISTFRAGMENT).navigation() as Fragment

        val newsFragment2 = ARouter.getInstance().build(RouterMapping.NewsModule.NEWSLISTFRAGMENT).navigation() as Fragment
        fragmentList.add(newsFragment)
        fragmentList.add(newsFragment2)
        return fragmentList
    }
}
