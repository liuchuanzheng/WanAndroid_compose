package com.lcz.wanandroid_compose.module.douyin.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.module.douyin.bean.VideoBean
import com.lcz.wanandroid_compose.util.SPUtil.Companion.init
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  14:42 2025/9/25
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class DouyinPageViewModel(private val isPreview: Boolean = false) : BaseViewModel() {
    private val _videoList = MutableStateFlow<List<VideoBean>>(emptyList())
    val videoList: StateFlow<List<VideoBean>> = _videoList.asStateFlow()

    // 新增播放状态管理
    private val _playStates = mutableStateMapOf<Int, Boolean>()
    private val _progressMap = mutableStateMapOf<Int, Float>()
    private val _isUserSeek = mutableStateOf(false)//是否用户手动滑动进度条，只有用户手动滑动才会调用播放器的seekTo方法
    val isUserSeek = _isUserSeek
    fun setIsUserSeek(isUserSeek: Boolean) {
        _isUserSeek.value = isUserSeek
    }

    fun getPlayState(page: Int) = mutableStateOf(_playStates[page] ?: true)
    fun getProgress(page: Int) = mutableStateOf(_progressMap[page] ?: 0.5f)

    fun togglePlayState(page: Int) {
        _playStates[page] = !(_playStates[page] ?: true)
    }

    fun seekTo(page: Int, progress: Float) {
        _progressMap[page] = progress.coerceIn(0f..1f)
    }

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
            1000,
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
            1000,
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
            1000,
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
            1000,
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
            1000,
        ),
        VideoBean(
            "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209105011F0zPoYzHry.mp4",
            "https://v16-1258200328.cos.ap-shanghai.myqcloud.com/20250925/1052374416/1695632232328.jpg",
            "这是抖音视频5",
            "刘传政",
            "https://img0.baidu.com/it/u=2716469502,1779134408&fm=253&app=138&f=JPEG?w=500&h=500",
            1000,
            1000,
            1000,
            "1000",
            false,
            false,
            false,
            1000,
            1000,
            1000,
        ),
    )

    init {
        if (isPreview) {
            _videoList.value = mockVideoList
        }
    }

    fun netGetDouyinVideoList(isRefresh: Boolean) {
        viewModelScope.launch {
            delay(1000)
            val videoList = mockVideoList
            if (isRefresh) {
                _videoList.value = videoList
            } else {
                _videoList.value += videoList
            }
        }
    }

}