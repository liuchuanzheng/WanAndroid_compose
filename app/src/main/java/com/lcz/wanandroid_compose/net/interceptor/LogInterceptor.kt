package com.lcz.wanandroid_compose.net.interceptor

import com.lcz.wanandroid_compose.util.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 * okhttp 日志拦截器
 * @author LTP  2022/3/21
 */
val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        // 使用自己的日志工具接管
        LogUtil.d(msg = message)

    }
    // 注意要生成BuildConfig类，就必须在gradle中配置buildConfig为true
}).setLevel(HttpLoggingInterceptor.Level.BODY)