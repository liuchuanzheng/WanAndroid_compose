package com.lcz.wanandroid_compose.module.douyin.bean

/**
 * 作者:     刘传政
 * 创建时间:  14:54 2025/9/25
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
data class VideoBean(
    val videoUrl: String,
    val coverUrl: String,
    val title: String,
    val author: String,
    val authorIconUrl: String,
    val commentCount: Int,
    val likeCount: Int,
    val collectCount: Int,
    val forwardCount: String,
    val isFollow: Boolean,
    val isCollect: Boolean,
    val isLiked: Boolean,
    val shareCount: Int,
    val duration: Int,
    val id: Int
)
data class VideoPlayState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isLoading: Boolean = true,
    val isCompleted: Boolean = false
)