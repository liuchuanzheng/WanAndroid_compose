package com.lcz.wanandroid_compose.module.douyin.page

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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.lcz.wanandroid_compose.module.douyin.bean.VideoBean
import com.lcz.wanandroid_compose.module.douyin.viewmodel.DouyinPageViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.ui.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.widget.CoilImage
import kotlinx.coroutines.delay

/**
 * 作者:     刘传政
 * 创建时间:  14:11 2025/9/25
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
val tag = "DouyinPage"
fun log(msg: String) {
    LogUtil.i(tag = tag, msg = msg)
}

@Composable
fun DouyinPage(paramsBean: AppRoutePath.Douyin, viewModel: DouyinPageViewModel = viewModel()) {
    val videoList by viewModel.videoList.collectAsState()
    val pagerState = rememberPagerState(pageCount = { videoList.size })
    LaunchedEffect(Unit) {
        viewModel.netGetDouyinVideoList(true)
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
                // 当页面可见时才初始化播放器
                val isVisible = pagerState.currentPage == page
                VideoItem(videoList, page, viewModel, isVisible)
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
fun VideoItem(videoList: List<VideoBean>, page: Int, viewModel: DouyinPageViewModel, isVisible: Boolean) {
    var videoBean = videoList.get(page)
    log(msg = "VideoItem: $page-$isVisible")
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayCard(page, videoBean, viewModel, isVisible)
//        Cover(videoBean)
    }

}

@Composable
private fun VideoPlayCard(page: Int, videoBean: VideoBean, viewModel: DouyinPageViewModel, isVisible: Boolean) {
    val isPlaying by viewModel.getPlayState(page)
    val progress by viewModel.getProgress(page)
    val isUserSeek by viewModel.isUserSeek
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                log(msg = "点击切换播放状态")
                viewModel.togglePlayState(page)
            }
    ) {
        // 播放器实现
        ExoPlayerWrapper(
            url = videoBean.videoUrl,
            isPlaying = isPlaying,
            progress = progress,
            onProgressChanged = {
                viewModel.seekTo(page, it)
            },
            modifier = Modifier.fillMaxSize(),
            isUserSeek,
            isVisible
        )

        // 底部进度条
        CustomSeekBar(
            progress = progress,
            onSeek = { value, isUser ->
                viewModel.setIsUserSeek(isUser)
                viewModel.seekTo(page, value)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayerWrapper(
    url: String,
    isPlaying: Boolean,
    progress: Float,
    onProgressChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    isUserSeek: Boolean,
    isVisible: Boolean
) {
    log(msg = "ExoPlayerWrapper重组: isPlaying:$isPlaying-progress:$progress-isUserSeek:$isUserSeek-isVisible:$isVisible-url:$url")
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            log("prepare一次-$url")
            playWhenReady = false
            seekTo((duration * progress).toLong())

        }
    }




    DisposableEffect(Unit) {
        onDispose {
            log(msg = "onDispose: $progress-player${exoPlayer}-${url}")
            exoPlayer.release()
        }
    }
//    LaunchedEffect(isVisible) {
//        if (isVisible) {
//            if (isPlaying) {
//                exoPlayer.play()
//            }
//        } else {
//            if (!isPlaying) {
//                exoPlayer.pause()
//            }
//
//        }
//    }



    LaunchedEffect(isPlaying) {
        log(msg = "切换${isPlaying}-isVisible:$isVisible- isUserSeek:$isUserSeek")
        if (isVisible){
            if (isPlaying) {
                exoPlayer.play()
            }else {
                exoPlayer.pause()
            }
        }else {
            if (!isPlaying) {
                exoPlayer.pause()
            }
        }
        while (isPlaying && isVisible && !isUserSeek) {
            val duration = exoPlayer.duration
            if (duration > 0) {
                val newProgress = exoPlayer.currentPosition.toFloat() / duration
                log(msg = "循环获取newProgress: $newProgress")
                onProgressChanged(newProgress.coerceIn(0f, 1f))
            }
            delay(5000) // 约60帧/秒更新
//                withFrameMillis {
//                    val duration = exoPlayer.duration
//                    if (duration > 0) {
//                        val newProgress = exoPlayer.currentPosition.toFloat() / duration
//                        log(msg = "循环获取newProgress: $newProgress")
//                        onProgressChanged(newProgress.coerceIn(0f, 1f))
//                    }
//                }

        }
    }
    // 新增初始化进度设置
    LaunchedEffect(isVisible) {
        if (isPlaying && exoPlayer.duration > 0 && progress > 0 && isVisible) {
            log(msg = "初始化进度: $progress-player${exoPlayer}-${url}")
            log(msg = "seekTo: $progress-duration-${exoPlayer.duration}-${url}")
            exoPlayer.seekTo((exoPlayer.duration * progress).toLong())
        }

    }
    LaunchedEffect(progress) {
        if (isUserSeek && exoPlayer.duration > 0) {
            exoPlayer.seekTo((exoPlayer.duration * progress).toLong())
        }

    }

    Box {
        AndroidView(
            modifier = modifier.background(Color.Black),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    useController = false // 隐藏默认控制条
                }
            },
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
        valueRange = 0f..1f,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color.Red.copy(alpha = 0.7f)
        )
    )
}

@Composable
private fun BoxScope.Cover(videoBean: VideoBean) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun DouyinPagePreview() {
    WanAndroid_composeTheme {
        DouyinPage(AppRoutePath.Douyin(), DouyinPageViewModel(true))
    }
}
