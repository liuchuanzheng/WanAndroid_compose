package com.lcz.wanandroid_compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.collections.isNotEmpty

/**
 * 可刷新列表组件
 * @param items 列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param allowRefresh 是否允许刷新
 * @param allowLoadMore 是否允许加载更多
 * @param hasMore 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param itemContent 列表项UI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RefreshableList(
    items: List<T>,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    allowRefresh: Boolean = true,
    allowLoadMore: Boolean = true,
    hasMore: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    headerContent: @Composable (() -> Unit)? = null,
    itemContent: @Composable (T) -> Unit
) {
    val listState = rememberLazyListState()
    var totalCount = items.size + if (headerContent != null) 1 else 0
    // 监听滚动位置，触发加载更多
    LaunchedEffect(totalCount) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect {
            if (it != null && it >= totalCount - 1 && items.isNotEmpty() && !isLoadingMore && hasMore && allowLoadMore) {
                onLoadMore()
            }
        }
    }
    val state = rememberPullToRefreshState()

    if (allowRefresh) {
        PullToRefreshBox(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = state,
            indicator = {
                // 自定义下拉刷新指示器
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state,
//                threshold = 120.dp //下拉距离
                )
            }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                // 头
                if (headerContent != null) {
                    item {
                        headerContent()
                    }
                }
                if (items.isEmpty()) {
                    item {
                        EmptyPlaceholder()
                    }
                }
                // 列表项
                items(items) {
                    itemContent(it)
                }
                if (allowLoadMore) {
                    // 加载更多/没有更多数据
                    item {

                        if (isLoadingMore) {
                            // 加载更多指示器
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp)

                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "加载更多", modifier = Modifier,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                        } else if (!hasMore && items.isNotEmpty()) {
                            // 没有更多数据提示
                            Text(
                                "没有更多数据了", modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            // 底部占位
                            Modifier.padding(8.dp)
                        }
                    }
                }

            }
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // 头
            if (headerContent != null) {
                item {
                    headerContent()
                }
            }
            // 列表项
            items(items) {
                itemContent(it)
            }
        }
    }
}

@Composable
fun EmptyPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(400.dp)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            Icons.Default.HourglassEmpty,
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "暂无数据",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}