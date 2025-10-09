package com.lcz.wanandroid_compose.module.ticktok.page

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.module.ticktok.viewmodel.TickTokPageViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.ui.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.widget.CoilImage
import kotlinx.coroutines.delay

/**
 * 作者:     刘传政
 * 创建时间:  9:38 2025/9/30
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun TickTokPage(paramsBean: AppRoutePath.TickTok, viewModel: TickTokPageViewModel = viewModel()) {
    val videoList by viewModel.videoList
    val pagerState = rememberPagerState(pageCount = { videoList.size })
    LaunchedEffect(Unit) {
        val currentVideoBean = videoList.getOrNull(0)
        currentVideoBean?.let { videoBean ->
            viewModel.updateVideoProperty(videoBean.id) {
                it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = true))
            }
        }
        viewModel.netGetVideoList(true)
    }
    var currentVideoIndex by remember { mutableStateOf(0) }
    // 监听页面变化
    LaunchedEffect(pagerState.currentPage) {
        currentVideoIndex = pagerState.currentPage
    }
    // 让当前视频是播放状态
    LaunchedEffect(currentVideoIndex) {
        val currentVideoBean = videoList.getOrNull(currentVideoIndex)
        currentVideoBean?.let { videoBean ->
            viewModel.updateVideoProperty(videoBean.id) {
                it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = true))
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        if (videoList.isNotEmpty()) {
            VerticalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                val isVisible = pagerState.currentPage == page
                var videoBean by remember { mutableStateOf(videoList.get(page)) }
                LogUtil.i("TickTokPage", "VideoItem: $videoBean")

                VideoItem(
                    videoBean,
                    page,
                    isVisible,

                    onProgressChanged = {

                        viewModel.updateVideoProperty(videoBean.id) { videoBean ->
                            videoBean.copy(videoPlayState = videoBean.videoPlayState.copy(currentPlayProgress = it))
                        }
                        videoBean = videoList.get(page)
                        LogUtil.i("TickTokPage", "onProgressChanged: $videoBean")
                    },
                    onPlayPauseChanged = { isPlaying ->
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = isPlaying))
                        }
                        videoBean = videoList.get(page)
                        LogUtil.i("TickTokPage", "onPlayPauseChanged $videoBean")
                    },
                    onVideoCompleted = {
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = false))
                        }
                        videoBean = videoList.get(page)
                    },
                    onDurationRead = {
                        viewModel.updateVideoProperty(videoBean.id) { videoBean ->
                            videoBean.copy(videoPlayState = videoBean.videoPlayState.copy(duration = it))
                        }
                        videoBean = videoList.get(page)
                        LogUtil.i("TickTokPage", "onDurationRead: it=$it, $videoBean")
                    },
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无内容")
            }

        }
        Spacer(modifier = Modifier.statusBarsPadding())
        IconButton(onClick = {
            globalNavController?.popBackStack()
        }) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIos,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun VideoItem(
    videoBean: VideoBean,
    page: Int,
    isVisible: Boolean,
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayCard(
            page,
            videoBean,
            isVisible,
            onProgressChanged = {
                onProgressChanged.invoke(it)
            },
            onPlayPauseChanged = {
                onPlayPauseChanged.invoke(it)
            },
            onVideoCompleted = {
                onVideoCompleted.invoke()
            },
            onDurationRead = {
                onDurationRead.invoke(it)
            },

            )
        Cover(
            videoBean
        )
    }

}

@Composable
private fun BoxScope.Cover( videoBean: VideoBean) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomStart),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 10.dp
                    )
            ) {
                Text(text = videoBean.author, color = Color.White)
                Text(text = videoBean.title, color = Color.White)
            }
            Cover_right(videoBean)

        }

    }
}

@Composable
fun Cover_right(videoBean: VideoBean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 10.dp)) {
        Box {
            Column {
                CoilImage(
                    model = videoBean.authorIconUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color.White, CircleShape)
                        .clip(CircleShape)

                )
                Spacer(modifier = Modifier.height(9.dp))
            }

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color = Color.Red)
                    .align(Alignment.BottomCenter)

            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Text(text = videoBean.likeCount.toString(), color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Text(text = videoBean.collectCount.toString(), color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Text(text = videoBean.shareCount.toString(), color = Color.White, fontSize = 12.sp)
        }

    }

}

@Composable
private fun VideoPlayCard(
    page: Int,
    videoBean: VideoBean,
    isVisible: Boolean,
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},

    ) {
    var isUserSeek by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
            }
    ) {
        // 播放器实现
        ExoPlayerWrapper(
            videoUrl = videoBean.videoUrl,
            isPlaying = videoBean.videoPlayState.isPlaying,
            progress = videoBean.videoPlayState.currentPlayProgress,
            isUserSeek,
            isVisible,
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
            CustomSeekBar(
                progress = videoBean.videoPlayState.currentPlayProgress,
                max = videoBean.videoPlayState.duration.toFloat(),
                onSeek = { value, isUser ->
                    isUserSeek = isUser
                    onProgressChanged.invoke(value)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayerWrapper(
    videoUrl: String,
    isPlaying: Boolean,
    progress: Float,
    isUserSeek: Boolean,//是否是用户拖动进度条
    isVisible: Boolean,
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},
) {
    LogUtil.i(tag = "TickTokPage", msg = "ExoPlayerWrapper重组:${isPlaying}}")
    val context = LocalContext.current
    var isVideoCompleted by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }
    val currentIsPlaying by rememberUpdatedState(isPlaying) // 关键修改：获取最新状态快照
    // 创建ExoPlayer实例
    var exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            LogUtil.i(tag = "TickTokPage", msg = "创建exoPlayer:${this} 视频url:${videoUrl}")
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
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
                            msg = "duration:${exoPlayer.duration} isPlaying:${currentIsPlaying}"
                        )
                        isInitialized = true
                        onDurationRead(exoPlayer.duration)
                        if (currentIsPlaying) {
                            exoPlayer.play()
                        } else {
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
            msg = "添加监听:${exoPlayer} playerListener${playerListener} 视频url:${videoUrl}"
        )
        exoPlayer.addListener(playerListener)
        onDispose {
            exoPlayer.removeListener(playerListener)
            exoPlayer.pause()
            exoPlayer.release()
            LogUtil.i(
                tag = "TickTokPage",
                msg = "销毁exoPlayer:${exoPlayer} playerListener${playerListener} 视频url:${videoUrl}"
            )
        }
    }

    // 控制播放状态
    LaunchedEffect(isPlaying) {
        if (isVideoCompleted || !isInitialized) return@LaunchedEffect
        if (!isVisible) return@LaunchedEffect

        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }
    // 监听播放进度
    LaunchedEffect(Unit, isPlaying) {

        while (true) {
            delay(5000) // 约60帧/秒更新
            if (isPlaying && isVisible && !isUserSeek && isInitialized && !isVideoCompleted) {

                val duration = exoPlayer.duration
                if (duration > 0) {
                    val newProgress = exoPlayer.currentPosition.toFloat()
                    onProgressChanged(newProgress)
                }
            }
            LogUtil.i(
                tag = "TickTokPage",
                msg = "循环一次:视频url:isPlaying:${isPlaying} ${videoUrl}"
            )
        }
    }
    // 拖动进度设置
    LaunchedEffect(progress) {
        if (isUserSeek && exoPlayer.duration > 0) {
            exoPlayer.seekTo(progress.toLong())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onPlayPauseChanged(!isPlaying)
            }
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
}

@Composable
private fun CustomSeekBar(
    progress: Float,
    max: Float = 100f,
    onSeek: (Float, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Slider(
        value = progress,
        onValueChange = {
            onSeek(it, true)
        },
        onValueChangeFinished = {
            onSeek(progress, false) // 修改3：拖动结束时同步最终值
        },
        valueRange = 0f..max,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color.Red.copy(alpha = 0.7f)
        )
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun TickTokPagePreview() {
    WanAndroid_composeTheme {
        TickTokPage(AppRoutePath.TickTok(), TickTokPageViewModel(true))
    }

}
