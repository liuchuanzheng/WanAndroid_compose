package com.lcz.wanandroid_compose.net

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.lcz.wanandroid_compose.MyApp
import com.lcz.wanandroid_compose.net.interceptor.CacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import com.lcz.wanandroid_compose.net.interceptor.logInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 作者:     刘传政
 * 创建时间:  10:08 2025/9/2
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
object RetrofitManager {
    // 通过 Application 类获取 Context
    private val appContext: Context by lazy {
        MyApp.instance.applicationContext
    }

    /** 请求根地址 */
    private val BASE_URL = "https://www.wanandroid.com"

    /** 请求超时时间 */
    private const val TIME_OUT_SECONDS = 10

    /** 请求cookie */
    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(appContext)
        )
    }

    /** OkHttpClient相关配置 */
    private val client: OkHttpClient
        get() = OkHttpClient.Builder()

            //设置缓存配置,缓存最大10M,设置了缓存之后可缓存请求的数据到data/data/包名/cache/net_cache目录中
            .cache(Cache(File(appContext.cacheDir, "net_cache"), 10 * 1024 * 1024))
            //添加缓存拦截器 可传入缓存天数
            .addInterceptor(CacheInterceptor(30))
            // 请求超时时间
            .connectTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .addInterceptor(ChuckerInterceptor(appContext))//网络监控库，方便调试
            // 请求过滤器
            .addInterceptor(logInterceptor)
            .build()

    /**
     * Retrofit相关配置
     */
    fun <T> getService(serviceClass: Class<T>, baseUrl: String? = null): T {
        return Retrofit.Builder()
            .client(client)
            // 使用Moshi更适合Kotlin
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl ?: BASE_URL)
            .build()
            .create(serviceClass)
    }
}