package com.lcz.wanandroid_compose.module.ticktok.page

import android.R.attr.onClick
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.module.ticktok.viewmodel.TickTokPageViewModel
import com.lcz.wanandroid_compose.module.ticktok.widget.Cover
import com.lcz.wanandroid_compose.module.ticktok.widget.VideoPlayCard
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.ui.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil

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
        viewModel.netGetVideoList(true)
    }
    var currentVideoIndex by remember { mutableStateOf(0) }
    // 监听页面变化
    LaunchedEffect(pagerState.currentPage) {
        currentVideoIndex = pagerState.currentPage
    }
    // 让当前视频是播放状态
    LaunchedEffect(currentVideoIndex, videoList.size) {
        val currentVideoBean = videoList.getOrNull(currentVideoIndex)
        currentVideoBean?.let { videoBean ->
            viewModel.updatePlayingState(videoBean.id)
            LogUtil.i("TickTokPage", "修改播放状态为true id:${videoBean.id} ")
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
                    .fillMaxSize(),
                beyondViewportPageCount = 2, // 关键：缓存当前页前后各 2 页（共 5 页）
//                pageSpacing = 10.dp
            ) { page ->
                val isVisible = pagerState.currentPage == page
                var videoBean = videoList.get(page)
                LogUtil.e("TickTokPage", "id:${videoBean.id} isPlaying:${videoBean.videoPlayState.isPlaying}")
                VideoItem(
                    videoBean,
                    page,
                    onProgressChanged = {

                        viewModel.updateVideoProperty(videoBean.id) { videoBean ->
                            videoBean.copy(videoPlayState = videoBean.videoPlayState.copy(currentPlayProgress = it))
                        }
                        LogUtil.i("TickTokPage", "onProgressChanged: $videoBean")
                    },
                    onPlayPauseChanged = { isPlaying ->
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = isPlaying))
                        }
                        LogUtil.i("TickTokPage", "onPlayPauseChanged $videoBean")
                    },
                    onVideoCompleted = {
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(videoPlayState = it.videoPlayState.copy(isPlaying = false))
                        }
                    },
                    onDurationRead = {
                        viewModel.updateVideoProperty(videoBean.id) { videoBean ->
                            videoBean.copy(videoPlayState = videoBean.videoPlayState.copy(duration = it))
                        }
                        LogUtil.i("TickTokPage", "onDurationRead: it=$it, $videoBean")
                    },
                    onLikeChange = { like ->
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(isLiked = like, likeCount = if (like) it.likeCount + 1 else it.likeCount - 1)
                        }
                    },
                    onCollectChange = { collect ->
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(
                                isCollect = collect,
                                collectCount = if (collect) it.collectCount + 1 else it.collectCount - 1
                            )
                        }
                    },
                    onFollowChange = { follow ->
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(isFollow = follow)
                        }
                    },
                    onShareClick = {
                        viewModel.updateVideoProperty(videoBean.id) {
                            it.copy(shareCount = it.shareCount + 1)
                        }
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
        Column {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
            )
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
}

@Composable
fun VideoItem(
    videoBean: VideoBean,
    page: Int,
    onProgressChanged: (Float) -> Unit,
    onPlayPauseChanged: (Boolean) -> Unit,
    onVideoCompleted: () -> Unit = {},
    onDurationRead: (Long) -> Unit = {},
    onLikeChange: (Boolean) -> Unit,
    onCollectChange: (Boolean) -> Unit,
    onFollowChange: (Boolean) -> Unit,
    onShareClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayCard(
            page,
            videoBean,
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
            videoBean,
            onLikeChange = {
                onLikeChange.invoke(it)
            },
            onCollectChange = {
                onCollectChange.invoke(it)
            },
            onFollowChange = {
                onFollowChange.invoke(it)
            },
            onShareClick = {
                onShareClick.invoke()
            },
        )
    }

}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun TickTokPagePreview() {
    WanAndroid_composeTheme {
        TickTokPage(AppRoutePath.TickTok(), TickTokPageViewModel(true))
    }

}
