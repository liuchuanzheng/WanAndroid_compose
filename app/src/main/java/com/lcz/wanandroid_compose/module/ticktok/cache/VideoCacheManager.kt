package com.lcz.wanandroid_compose.module.ticktok.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheKeyFactory
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

/**
 * 作者:     刘传政
 * 创建时间:  9:32 2025/10/10
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@UnstableApi
object VideoCacheManager {
    private const val MAX_CACHE_SIZE = 1024L * 1024 * 1024 // 1GB 最大缓存
    private var cache: Cache? = null

    private fun getCache(context: Context): Cache {
        if (cache == null) {
            // 缓存目录（通常放在应用私有目录）
            val cacheDir = File(context.cacheDir, "exo_player_cache")
            // 缓存驱逐策略：当缓存超过 MAX_CACHE_SIZE 时，删除最久未使用的文件
            val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)
            cache = SimpleCache(cacheDir, evictor)
        }
        return cache!!
    }

    // 创建带缓存的数据源工厂
    fun createCacheDataSourceFactory(context: Context): DataSource.Factory {
        // 1. 上游数据源（网络请求，如 HTTP）
        val upstreamFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("ExoPlayer-Cache-Demo") // 设置 User-Agent
            .setConnectTimeoutMs(5000) // 连接超时
            .setReadTimeoutMs(5000) // 读取超时

        // 2. 缓存数据源工厂
        return CacheDataSource.Factory()
            .setCache(getCache(context)) // 关联缓存实例
            .setUpstreamDataSourceFactory(upstreamFactory) // 关联上游数据源
            .setFlags(CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            // 标志说明：
            // - FLAG_BLOCK_ON_CACHE: 缓存命中时阻塞等待缓存数据（避免播放卡顿）
            // - FLAG_IGNORE_CACHE_FOR_MEDIA_ITEM: 忽略 MediaItem 中的缓存配置，使用当前缓存策略
            .setCacheKeyFactory(object : CacheKeyFactory {
                // 自定义缓存键生成逻辑（默认使用媒体 URL，可重写避免 URL 变化导致缓存失效）
                override fun buildCacheKey(dataSpec: DataSpec): String {
                    return dataSpec.key ?: dataSpec.uri.toString()
                }
            })
    }
}
