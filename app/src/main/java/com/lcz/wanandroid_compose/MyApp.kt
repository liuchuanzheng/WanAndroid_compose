package com.lcz.wanandroid_compose

import android.app.Application

/**
 * 作者:     刘传政
 * 创建时间:  11:30 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}