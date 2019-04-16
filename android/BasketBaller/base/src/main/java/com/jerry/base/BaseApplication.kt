package com.jerry.base

import android.app.Activity
import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.external.ExternalAdaptInfo
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.LogUtils
import java.util.*

abstract class BaseApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * ARouter
         */
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()   // 打印日志
            ARouter.openDebug()  // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化

        /**
         * AutoSize
         */
        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance()

            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
            .setCustomFragment(true).onAdaptListener = object :onAdaptListener{
            override fun onAdaptBefore(target: Any?, activity: Activity?) {
                //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
    //                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
    //                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target?.javaClass?.name))
            }

            override fun onAdaptAfter(target: Any?, activity: Activity?) {
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target?.javaClass?.name))
            }

        }
    }
}