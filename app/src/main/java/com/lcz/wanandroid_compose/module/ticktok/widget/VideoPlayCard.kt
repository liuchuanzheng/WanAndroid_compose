package com.lcz.wanandroid_compose.module.ticktok.widget

import android.R.attr.value
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.module.ticktok.cache.VideoCacheManager
import com.lcz.wanandroid_compose.util.LogUtil
import kotlinx.coroutines.delay

/**
 * 作者:     刘传政
 * 创建时间:  10:21 2025/10/10
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun VideoPlayCard(
    page: Int,
    videoBean: VideoBean,
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},

    ) {
    LogUtil.i(
        tag = "TickTokPage",
        msg = "VideoPlayCard重组:id:${videoBean.id} isPlaying:${videoBean.videoPlayState.isPlaying}}"
    )
    var isUserSeek by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 播放器实现
        ExoPlayerWrapper(
            id = videoBean.id,
            videoUrl = videoBean.videoUrl,
            isPlaying = videoBean.videoPlayState.isPlaying,
            progress = videoBean.videoPlayState.currentPlayProgress,
            isUserSeek,
            onProgressChanged = {
                onProgressChanged.invoke(it)
            },
            onPlayPauseChanged = {
                onPlayPauseChanged(it)
            },
            onVideoCompleted = {
                onVideoCompleted.invoke()
            },
            onDurationRead = {
                onDurationRead.invoke(it)
            },
        )
        if (videoBean.videoPlayState.duration.toFloat() > 0) {
            // 底部进度条
            var progress =
                (videoBean.videoPlayState.currentPlayProgress / videoBean.videoPlayState.duration.toFloat()).coerceIn(
                    0f,
                    1f
                )
            var currentTime = formatVideoDuration(videoBean.videoPlayState.currentPlayProgress.toLong())
            var totalTime = formatVideoDuration(videoBean.videoPlayState.duration)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp)
            ) {
                CustomTikTokProgressBar(
                    progress = progress,
                    currentTime = currentTime,
                    totalTime = totalTime,
                    onProgressChanged = { newProgress, isUser ->
                        isUserSeek = isUser
                        onProgressChanged.invoke(newProgress * videoBean.videoPlayState.duration)
                    }
                )
            }
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayerWrapper(
    id: Int,
    videoUrl: String,
    isPlaying: Boolean,
    progress: Float,
    isUserSeek: Boolean,//是否是用户拖动进度条
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},

    ) {
    LogUtil.i(tag = "TickTokPage", msg = "ExoPlayerWrapper重组:id:${id} isPlaying:${isPlaying}}")
    val context = LocalContext.current
    var isVideoCompleted by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }
    val currentIsPlaying by rememberUpdatedState(isPlaying) // 关键修改：获取最新状态快照
    // 创建ExoPlayer实例
    var exoPlayer = remember {
        ExoPlayer.Builder(context).setMediaSourceFactory(
            ProgressiveMediaSource.Factory(
                VideoCacheManager.createCacheDataSourceFactory(context)
            )
        ).build().apply {
            LogUtil.i(tag = "TickTokPage", msg = "创建exoPlayer:${this} id:${id}")
            repeatMode = ExoPlayer.REPEAT_MODE_ONE //重复播放

            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false
            // 恢复播放位置
            if (progress > 0) {
                seekTo((progress).toLong())
            }

        }
    }
    val playerListener = remember {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        isVideoCompleted = true
                        onVideoCompleted()
                    }

                    Player.STATE_READY -> {
                        LogUtil.i(
                            tag = "TickTokPage",
                            msg = "id:${id} duration:${exoPlayer.duration} isPlaying:${currentIsPlaying}"
                        )
                        isInitialized = true
                        onDurationRead(exoPlayer.duration)
                        if (currentIsPlaying) {
                            LogUtil.i(
                                tag = "TickTokPage",
                                msg = "id:${id} duration play"
                            )
                            exoPlayer.play()
                        } else {
                            LogUtil.i(
                                tag = "TickTokPage",
                                msg = "id:${id} duration pause"
                            )
                            exoPlayer.pause()
                        }

                    }
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
//                showPlayButton = !playing
//                onPlayStateChanged(playing)
            }
        }
    }


    // 清理资源
    DisposableEffect(Unit) {
        LogUtil.i(
            tag = "TickTokPage",
            msg = "添加监听:id:${id} ${exoPlayer} playerListener${playerListener} "
        )
        exoPlayer.addListener(playerListener)
        onDispose {
            exoPlayer.removeListener(playerListener)
            exoPlayer.pause()
            exoPlayer.release()
            LogUtil.i(
                tag = "TickTokPage",
                msg = "销毁exoPlayer:id:${id} ${exoPlayer} "
            )
        }
    }

    // 控制播放状态
    LaunchedEffect(isPlaying) {
        if (isVideoCompleted || !isInitialized) return@LaunchedEffect

        if (isPlaying) {
            LogUtil.i(
                tag = "TickTokPage",
                msg = "播放状态切换 play:id:${id}"
            )
            exoPlayer.play()
        } else {
            LogUtil.i(
                tag = "TickTokPage",
                msg = "播放状态切换 pause:id:${id}"
            )
            exoPlayer.pause()
        }
    }
    // 监听播放进度
    LaunchedEffect(isPlaying) {

        while (isPlaying) {
            delay(100) // 约60帧/秒更新
            if (isPlaying && !isUserSeek && isInitialized && !isVideoCompleted) {

                val duration = exoPlayer.duration
                if (duration > 0) {
                    val newProgress = exoPlayer.currentPosition.toFloat()
                    onProgressChanged(newProgress)
                }
            }
//            LogUtil.i(
//                tag = "TickTokPage",
//                msg = "循环一次:视频url:isPlaying:${isPlaying} ${videoUrl}"
//            )
        }
    }
    // 拖动进度设置
    LaunchedEffect(progress) {
        if (isUserSeek && exoPlayer.duration > 0) {
            exoPlayer.seekTo(progress.toLong())
        }
    }
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // ExoPlayer视图
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        useController = false // 隐藏默认控制器
                        setArtworkDisplayMode(PlayerView.ARTWORK_DISPLAY_MODE_FIT)
                    }
                },
                update = { view ->
                    // 确保每次播放器实例变化时都绑定到视图
                    view.player = exoPlayer
                }
            )
            // 播放按钮
            AnimatedVisibility(
                !isPlaying,
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                enter = fadeIn() + slideInVertically(
                    animationSpec = spring(
                        stiffness = 120f,// 降低刚度值使动画更慢
                        dampingRatio = 0.4f// 调整阻尼比（0.6 是临界阻尼，值越小反弹越多）,
                    ),
                    initialOffsetY = { fullHeight -> fullHeight }),

                exit = slideOutVertically(  // 新增垂直滑动退出
                    animationSpec = tween(1000),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut(),
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline, contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Magenta
                )
            }

        }
        //留出底部一定区域，防止跟进度条重叠，点击事件与拖动事件冲突
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .clickable {
                    onPlayPauseChanged(!isPlaying)
                }
        ) {
        }
    }

}


/**
 * 将视频时长（毫秒）转换为可读时间格式
 * - 时长 < 1小时：返回 "MM:SS"（如 "02:30"）
 * - 时长 ≥ 1小时：返回 "HH:MM:SS"（如 "01:02:30"）
 * @param durationMs 视频时长（毫秒，需 ≥ 0）
 */
fun formatVideoDuration(durationMs: Long): String {
    // 确保时长非负（避免异常输入）
    val totalSeconds = (durationMs / 1000).coerceAtLeast(0)

    // 计算时、分、秒
    val hours = totalSeconds / 3600
    val remainingSeconds = totalSeconds % 3600
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    // 根据是否有小时位，返回不同格式
    return if (hours > 0) {
        // 带小时：HH:MM:SS（小时不足两位补0）
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        // 不带小时：MM:SS（分钟不足两位补0）
        String.format("%02d:%02d", minutes, seconds)
    }
}