package com.jerry.base.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BaseFragmentPagerAdapter :FragmentPagerAdapter{



    var fragmentList:List<Fragment>

    constructor(fm:FragmentManager,list:List<Fragment>):super(fm){
        fragmentList = list
    }
    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}