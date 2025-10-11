package com.lcz.wanandroid_compose.module.ticktok.bean

/**
 * 作者:     刘传政
 * 创建时间:  14:54 2025/9/25
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
data class VideoBean(
    var videoUrl: String,
    var coverUrl: String,
    var title: String,
    var author: String,
    var authorIconUrl: String,
    var commentCount: Int,
    var likeCount: Int,
    var collectCount: Int,
    var forwardCount: String,
    var isFollow: Boolean,
    var isCollect: Boolean,
    var isLiked: Boolean,
    var shareCount: Int,
    var duration: Int,//这代表网络返回的视频时长
    var id: Int,
    var videoPlayState: VideoPlayState = VideoPlayState()
)

data class VideoPlayState(
    var isPlaying: Boolean = false,
    var currentPlayProgress: Float = 0F,
    var duration: Long = 0L,//这代表视频实际时长
    var isLoading: Boolean = true,
    var isCompleted: Boolean = false
)