package com.lcz.wanandroid_compose.module.ticktok.viewmodel

import android.R.attr.duration
import android.provider.SyncStateContract.Helpers.update
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.util.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  9:39 2025/9/30
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class TickTokPageViewModel(isPreview: Boolean = false) : BaseViewModel() {
    val videoList = mutableStateOf(emptyList<VideoBean>())

    val mockVideoList = listOf(
        VideoBean(
            "https://jomin-web.web.app/resource/video/video_iu.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频",
            "刘传政",
            "https://img1.baidu.com/it/u=1221952588,3009131272&fm=253&app=138&f=JPEG?w=500&h=500",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            1,
        ),
        VideoBean(
            "https://www.w3schools.com/html/movie.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频1",
            "刘传政",
            "https://gips2.baidu.com/it/u=2095232276,2558595467&fm=3074&app=3074&f=PNG?w=2048&h=2048",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            2,
        ),
        VideoBean(
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频2",
            "刘传政",
            "https://img0.baidu.com/it/u=3076263464,3395437012&fm=253&app=138&f=JPEG?w=500&h=500",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            3,
        ),
        VideoBean(
            "https://www.w3school.com.cn/example/html5/mov_bbb.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频3",
            "刘传政",
            "https://img0.baidu.com/it/u=1215796994,2362759594&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            4,
        ),
        VideoBean(
            "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频4",
            "刘传政",
            "https://img2.baidu.com/it/u=2875155733,1880123587&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            5,
        ),

        )

    init {
        if (isPreview) {
            videoList.value = mockVideoList
        }
    }

    fun netGetVideoList(isRefresh: Boolean) {
        viewModelScope.launch {
            delay(1000)
            val mockVideoList = mockVideoList
            if (isRefresh) {
                videoList.value = mockVideoList
            } else {
                videoList.value += mockVideoList
            }
        }
    }

    fun updateVideoProperty(videoId: Int, update: (VideoBean) -> VideoBean) {
        videoList.value = videoList.value.map { video ->
            if (video.id == videoId) {
                var newVideo = update(video)
                newVideo
            } else {
                video
            }
        }
    }

    //指定id isPlaying变为true,其他的变为false
    fun updatePlayingState(videoId: Int) {
        videoList.value = videoList.value.map { video ->
            if (video.id == videoId) {
                video.copy(videoPlayState = video.videoPlayState.copy(isPlaying = true))
            } else {
                video.copy(videoPlayState = video.videoPlayState.copy(isPlaying = false))
            }
        }
    }

}