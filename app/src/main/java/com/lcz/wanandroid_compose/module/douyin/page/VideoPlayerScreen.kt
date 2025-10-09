import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.lcz.wanandroid_compose.module.douyin.page.VideoItem
import com.lcz.wanandroid_compose.util.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 视频数据类
data class VideoItem(
    val id: String,
    val url: String,
    val title: String,
    val author: String
)

// 主视频播放屏幕
@Composable
fun VideoPlayerScreen(
    // 直接使用List替代LazyPagingItems
) {
    var videos: List<VideoItem> = remember {
        listOf(
            VideoItem("1", "https://jomin-web.web.app/resource/video/video_iu.mp4", "title", "author"),
            VideoItem("2", "https://www.w3schools.com/html/movie.mp4", "title", "author"),
            VideoItem("3", "https://media.w3.org/2010/05/sintel/trailer.mp4", "title", "author")
        )
    }
    val pagerState = rememberPagerState(pageCount = { videos.size })
    val coroutineScope = rememberCoroutineScope()

    // 存储每个视频的播放进度
    val videoProgressMap = remember { mutableMapOf<String, Long>() }

    // 当前正在播放的视频ID
    var currentPlayingVideoId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val videoItem = videos[page] // 直接通过索引获取视频

            // 当页面可见时才初始化播放器
            val isVisible = pagerState.currentPage == page
            VideoPlayerItem(
                videoItem = videoItem,
                isVisible = isVisible,
                initialPosition = videoProgressMap[videoItem.id] ?: 0L,
                onPlayPauseToggle = { isPlaying ->
                    if (isPlaying) {
                        currentPlayingVideoId = videoItem.id
                    } else if (currentPlayingVideoId == videoItem.id) {
                        currentPlayingVideoId = null
                    }
                },
                onProgressChanged = { position, duration ->
                    if (duration > 0) {
                        videoProgressMap[videoItem.id] = position
                    }
                },
                onVideoEnded = {
                    // 视频播放结束后自动播放下一个
//                    coroutineScope.launch {
//                        if (page < videos.lastIndex) { // 使用lastIndex替代itemCount - 1
//                            pagerState.animateScrollToPage(page + 1)
//                        }
//                    }
                }
            )
        }
    }

    // 当页面切换时，暂停上一个视频
    LaunchedEffect(pagerState.currentPage) {
        currentPlayingVideoId?.let { videoId ->
            // 查找之前的视频索引
            videos.indexOfFirst { it.id == videoId } // 直接使用List的indexOfFirst
                .takeIf { it != -1 && it != pagerState.currentPage }
                ?.let { previousPage ->
                    // 通知前一个视频暂停
                    currentPlayingVideoId = null
                }
        }
    }
}

// 单个视频播放器项
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerItem(
    videoItem: VideoItem,
    isVisible: Boolean,
    initialPosition: Long,
    onPlayPauseToggle: (Boolean) -> Unit,
    onProgressChanged: (Long, Long) -> Unit,
    onVideoEnded: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoItem.url))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var showControls by remember { mutableStateOf(true) }

    // 当视频可见性变化时
    LaunchedEffect(isVisible) {
        if (isVisible) {
            // 恢复到上次播放位置
            if (initialPosition > 0) {
                exoPlayer.seekTo(initialPosition)
                LogUtil.i(msg = "恢复到上次播放位置: $initialPosition")
            }
            exoPlayer.playWhenReady = true
            isPlaying = true
            onPlayPauseToggle(true)
        } else {
            exoPlayer.playWhenReady = false
            isPlaying = false
            onPlayPauseToggle(false)
        }
    }

    // 监听播放进度
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500) // 每500ms更新一次进度
            val currentPosition = exoPlayer.currentPosition
            val duration = exoPlayer.duration
            if (duration > 0) {
                progress = currentPosition.toFloat() / duration.toFloat()
                LogUtil.i(msg = "更新进度: $progress")
                onProgressChanged(currentPosition, duration)
            }

            // 检查视频是否播放结束
            if (currentPosition >= duration - 1000 && duration > 0) {
                onVideoEnded()
                break
            }
        }
    }

    // 处理播放器生命周期
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // 视频点击事件 - 切换播放/暂停
    val onVideoClick = {
        isPlaying = !isPlaying
        exoPlayer.playWhenReady = isPlaying
        onPlayPauseToggle(isPlaying)
        showControls = true // 点击时显示控件
    }

    // 进度条拖动事件
    val onProgressDragged = { newProgress: Float ->
        val duration = exoPlayer.duration
        if (duration > 0) {
            val newPosition = (newProgress * duration).toLong()
            exoPlayer.seekTo(newPosition)
            progress = newProgress
            onProgressChanged(newPosition, duration)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 视频播放器
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    useController = false // 禁用默认控制器
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onVideoClick() }
                    )
                }
        )

        // 视频信息展示
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 这里可以添加视频标题、作者等信息
            // 例如：
            /*
            Column {
                Text(
                    text = videoItem.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "作者: ${videoItem.author}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            */
        }

        // 底部进度条
        if (showControls) {
            VideoProgressBar(
                progress = progress,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                onProgressChanged = onProgressDragged
            )

            // 自动隐藏控件
            LaunchedEffect(showControls) {
                delay(30000)
                showControls = false
            }
        }
    }
}

// 视频进度条组件
@Composable
fun VideoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    onProgressChanged: (Float) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    var localProgress by remember { mutableFloatStateOf(progress) }

    LaunchedEffect(progress, isDragging) {
        if (!isDragging) {
            localProgress = progress
        }
    }

    Box(modifier = modifier.height(4.dp)) {
        LinearProgressIndicator(
            progress = localProgress,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val newProgress = offset.x / size.width
                            localProgress = newProgress.coerceIn(0f, 1f)
                            onProgressChanged(localProgress)
                        }
                    )
                }
                .scrollable(
                    state = rememberScrollState(0),
                    orientation = Orientation.Horizontal,
//                    onScroll = { delta ->
//                        isDragging = true
//                        val change = delta / size.width
//                        localProgress = (localProgress - change).coerceIn(0f, 1f)
//                        onProgressChanged(localProgress)
//                    },
                    reverseDirection = true
                )
        )
    }
}
    