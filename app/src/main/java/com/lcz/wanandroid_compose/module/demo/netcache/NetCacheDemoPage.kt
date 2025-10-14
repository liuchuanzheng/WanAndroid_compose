package com.lcz.wanandroid_compose.module.demo.netcache

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lcz.wanandroid_compose.base.BaseNetResponseBean
import com.lcz.wanandroid_compose.base.BasePageResponseBean
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

@Composable
fun NetCacheDemoPage(modifier: AppRoutePath.NetCache) {
    val coroutineScope = rememberCoroutineScope()
    var context = LocalContext.current
    val apiService = RetrofitClient.create(context)
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    CacheVerification.checkCacheFiles(context)
                }
            }
        ) {
            Text("检查缓存目录")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    // 发起网络请求
                    withContext(Dispatchers.IO) {
                        try {
                            // 处理响应数据
                            val response = apiService.login("18501231486", "005876")
                            if (response != null) {
                                Log.d("http_cache", "Response body: ${response.toString()}")
                            }
                        } catch (e: Exception) {
                            Log.e("http_cache", "Error: ${e.message}")
                        }

                    }
                }
            }
        ) {
            Text("请求数据1")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    // 发起网络请求
                    withContext(Dispatchers.IO) {
                        try {
                            // 处理响应数据
                            val response = apiService.getArticlePageList(1, 10)
                            if (response != null) {
                                Log.d("http_cache", "Response body: ${response.toString()}")
                            }
                        } catch (e: Exception) {
                            Log.e("http_cache", "Error: ${e.message}")
                        }

                    }
                }
            }
        ) {
            Text("请求数据2")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    // 发起网络请求
                    withContext(Dispatchers.IO) {
                        try {
                            // 处理响应数据
                            val response = apiService.getArticleTopList()
                            if (response != null) {
                                Log.d("http_cache", "Response body: ${response.toString()}")
                            }
                        } catch (e: Exception) {
                            Log.e("http_cache", "Error: ${e.message}")
                        }

                    }
                }
            }
        ) {
            Text("请求数据3")
        }
    }
}

object OkHttpCacheConfig {
    // 缓存大小设置为10MB
    private const val CACHE_SIZE: Long = 10 * 1024 * 1024
    private const val CACHE_DIR_NAME = "retrofit_cache"

    // 创建带缓存的OkHttpClient
    fun createOkHttpClient(context: Context): OkHttpClient {
        // 创建缓存目录
        val cacheDir = File(context.cacheDir, CACHE_DIR_NAME)
        // 创建缓存实例
        val cache = Cache(cacheDir, CACHE_SIZE)

        return OkHttpClient.Builder()
            .cache(cache)
            // 添加应用拦截器处理缓存控制
            .addInterceptor(CacheControlInterceptor(context))
            // 添加网络拦截器处理响应缓存
//            .addNetworkInterceptor(CacheResponseInterceptor())
//            .addInterceptor(CacheLoggingInterceptor()) // 添加日志拦截器
            // 设置超时时间
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

class CacheControlInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 根据网络状态设置不同的缓存策略
        if (!isNetworkAvailable()) {
            // 无网络时强制使用缓存，设置最大过期时间为4周
            request = request.newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(4, TimeUnit.DAYS)
                        .build()
                )
                .build()
        }

        return chain.proceed(request)
    }

    // 检查网络连接状态
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            // 兼容旧版本Android
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}

class CacheResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // 获取请求头中的缓存控制指令
        val cacheControlHeader = request.header("Cache-Control")

        // 如果请求中没有指定缓存控制策略，使用默认策略
        return if (cacheControlHeader.isNullOrEmpty()) {
            // 默认缓存策略：有网络时缓存60秒
            response.newBuilder()
                .header("Cache-Control", "public, max-age=60")
                .removeHeader("Pragma") // 移除Pragma头，避免与Cache-Control冲突
                .build()
        } else {
            // 使用请求中指定的缓存策略
            response.newBuilder()
                .header("Cache-Control", cacheControlHeader)
                .removeHeader("Pragma")
                .build()
        }
    }
}

class ApiCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)

        // 检查请求是否有缓存策略注解
        if (invocation != null) {
            val method: Method = invocation.method()
            val cacheStrategy = method.getAnnotation(MyCacheStrategy::class.java)

            if (cacheStrategy != null) {
                // 根据注解值构建新的请求
                val builder = request.newBuilder()

                if (cacheStrategy.maxAge > 0) {
                    // 设置缓存时间
                    builder.header("Cache-Control", "public, max-age=${cacheStrategy.maxAge}")
                } else if (cacheStrategy.onlyIfCached) {
                    // 仅使用缓存.如果没有缓存会报Error: HTTP 504 Unsatisfiable Request (only-if-cached)
                    builder.header("Cache-Control", "only-if-cached")
                } else {
                    // 不使用缓存
                    builder.header("Cache-Control", "no-cache")
                }

                return chain.proceed(builder.build())
            }
        }

        // 没有缓存策略注解，使用默认行为
        return chain.proceed(request)
    }
}

class CacheLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val cacheResponse = response.cacheResponse
        val networkResponse = response.networkResponse

        // 记录请求URL
        val url = request.url.toString()
        // 记录请求 Cache-Control 头信息
        request.header("Cache-Control")?.let {
            Log.d("http_cache", "请求头 Cache-Control for $url: $it")
        }
        // 记录缓存控制头信息
        response.header("Cache-Control")?.let {
            Log.d("http_cache", "响应头 Cache-Control for $url: $it")
        }
        when {
            cacheResponse != null && networkResponse == null -> {
                // 完全来自缓存
                Log.d("http_cache", "完全来自缓存 for: $url")
            }

            networkResponse != null && cacheResponse == null -> {
                // 完全来自网络
                Log.d("http_cache", "完全来自网络 for: $url")
            }

            networkResponse != null && cacheResponse != null -> {
                // 部分来自缓存，部分来自网络
                Log.d("http_cache", "部分来自缓存，部分来自网络 for: $url")
            }
        }


        return response
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://www.wanandroid.com"

    // 创建Retrofit实例
    fun create(context: Context): ApiService {
        val okHttpClient = OkHttpCacheConfig.createOkHttpClient(context)
            .newBuilder()
            // 添加API缓存拦截器
            .addInterceptor(ApiCacheInterceptor())
            .addInterceptor(CacheLoggingInterceptor()) // 添加日志拦截器
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

object CacheVerification {
    fun checkCacheFiles(context: Context) {
        val cacheDir = File(context.cacheDir, "retrofit_cache")
        if (cacheDir.exists() && cacheDir.isDirectory) {
            val cacheFiles = cacheDir.listFiles()
            Log.d("http_cache", "Cache directory exists with ${cacheFiles?.size ?: 0} files")
            cacheFiles?.forEach {
                Log.d("http_cache", "Cache file: ${it.name}, size: ${it.length()} bytes")
            }
        } else {
            Log.d("http_cache", "Cache directory does not exist or is not a directory")
        }
    }
}

// 缓存策略注解
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MyCacheStrategy(
    // 缓存时间（秒），默认为-1表示不使用缓存
    val maxAge: Int = -1,
    // 是否仅使用缓存（无网络时）
    val onlyIfCached: Boolean = false
)

interface ApiService {
    // 不使用缓存的接口（如登录）
    @MyCacheStrategy(maxAge = 1*60)
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): BaseNetResponseBean<LoginResponseBean>

    /** 获取首页/体系详情文章数据 */
    // 缓存1小时的接口（如新闻列表）
    @MyCacheStrategy(maxAge = 3600)
    @GET("article/list/{pageNo}/json")
    suspend fun getArticlePageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
        @Query("cid") categoryId: Int? = null
    ): BaseNetResponseBean<BasePageResponseBean<Article>>
    /** 获取置顶文章集合数据 */
    //仅在无网络时使用缓存的接口
    @MyCacheStrategy(onlyIfCached = false)
    @GET("article/top/json")
    suspend fun getArticleTopList(): BaseNetResponseBean<List<Article>>
}