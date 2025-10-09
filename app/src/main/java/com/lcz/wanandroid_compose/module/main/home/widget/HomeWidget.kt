package com.lcz.wanandroid_compose.module.main.home.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.main.home.viewmodel.HomeWidgetViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToSearch
import com.lcz.wanandroid_compose.navigation.app_navigateToVideoPlayer
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.ui.video.VideoItem
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.ui.video.sampleVideos
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.util.toHtml
import com.lcz.wanandroid_compose.widget.Banner
import com.lcz.wanandroid_compose.widget.RefreshableList

/**
 * 作者:     刘传政
 * 创建时间:  10:27 2025/9/15
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWidget(modifier: Modifier = Modifier) {
    val viewModel = viewModel<HomeWidgetViewModel>()

    val articleList by viewModel.articleList.collectAsState()
    val bannerList = viewModel.bannerList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()

    LaunchedEffect(Unit) {
        LogUtil.d("ProjectChildPage", "触发刷新")
        if (articleList.isNullOrEmpty()) {
            viewModel.netGetArticleList(true)
            viewModel.netGetBanner()
        }
    }
    //顶部状态栏的滚动跟随行为。一共三种，自己挑
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            CenterAlignedTopAppBar(
                modifier = Modifier
                    .height(75.dp) // 设置自定义高度（Material3 默认高度为 80.dp）
                    .shadow(8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    TitleBar()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            RefreshableList(
                items = articleList ?: emptyList(),
                isRefreshing = pageState.isRefreshing,
                isLoadingMore = pageState.isLoadingMore,
                allowRefresh = true,
                allowLoadMore = true,
                hasMore = pageState.hasMore,
                onRefresh = {
                    viewModel.netGetArticleList(true)
                    viewModel.netGetBanner()
                },
                onLoadMore = {
                    viewModel.netGetArticleList(false)
                },
                headerContent = {
                    if (bannerList.value.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .height(200.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(12.dp))
                        ) {
                            Banner(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                images = bannerList.value.map {
                                    it.imagePath ?: ""
                                },
                                autoScroll = true,
                                indicatorSize = 10,
                                indicatorModifier = Modifier.padding(bottom = 10.dp),
                                onBannerItemClick = {

                                }
                            )
                        }
                    }
                },
                itemContent = { index, item ->
                    ItemView(index, item)
                }
            )
        }

    }
}

@Composable
fun TitleBar() {
    val roundedCornerShape = RoundedCornerShape(12.dp)
    

    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 搜索框
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .clip(roundedCornerShape)
                .border(BorderStroke(1.dp, Color.Gray), shape = roundedCornerShape)
                .clickable {
                    globalNavController?.app_navigateToSearch(AppRoutePath.Search())
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
            Text(text = "搜索", fontSize = 14.sp)
        }
        
        // 视频播放器按钮
        Row(
            modifier = Modifier
                .clip(roundedCornerShape)
                .border(BorderStroke(1.dp, Color.Gray), shape = roundedCornerShape)
                .clickable {
                    globalNavController?.app_navigateToVideoPlayer(
                        AppRoutePath.VideoPlayer()
                    )
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            Text(text = "视频", fontSize = 14.sp)
        }
    }
}

@Composable
fun ItemView(index: Int, item: Article) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .shadow(
                elevation = 4.dp,          // 提升阴影高度
                shape = MaterialTheme.shapes.medium,
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
//            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                BorderStroke(1.dp, color = if (item.isTop) Color.Red else Color.Green),
                shape = MaterialTheme.shapes.medium
            )
            .clickable {

            }
            .padding(horizontal = 16.dp, vertical = 8.dp)


    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    item.author?.ifEmpty { item.shareUser }?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (item.type == 1) Text(
                        text = "置顶",
                        color = Color.Red,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(1.dp, Color.Red, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 4.dp)
                    )
                    if (item.fresh == true) Text(
                        text = "新",
                        color = Color.Magenta,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(1.dp, Color.Magenta, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp)
                    )


                }
                Text(
                    text = item.niceDate.toString(),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title?.toHtml().toString(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.superChapterName.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = if (item.collect == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (item.collect == true) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemViewPreview() {
    WanAndroid_composeTheme {
        ItemView(
            1,
            Article(
                title = "这是标题",
                shareUser = "这是分享人",
                niceDate = "这是日期",
                superChapterName = "这是分类",
                collect = true,
                fresh = true,
                type = 1
            )
        )
    }
}

@Preview
@Composable
fun ItemViewPreview_Dark() {
    WanAndroid_composeTheme(darkTheme = true) {
        ItemView(
            1,
            Article(
                title = "这是标题1",
                shareUser = "这是分享人",
                niceDate = "这是日期",
                superChapterName = "这是分类",
                collect = true
            )
        )
    }
}

@Preview
@Composable
fun MyCoinHistoryPagePreview() {
    WanAndroid_composeTheme {
        HomeWidget()
    }
}


@Preview
@Composable
fun MyCoinHistoryPagePreview_Dark() {
    WanAndroid_composeTheme(darkTheme = true) {
        HomeWidget()
    }

}
