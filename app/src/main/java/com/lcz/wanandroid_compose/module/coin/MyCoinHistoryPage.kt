package com.lcz.wanandroid_compose.module.coin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.coin.data.MyCoinHistoryResponseBean
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.PageJumpManager
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil
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
fun MyCoinHistoryPage(paramsBean: AppRoutePath.MyCoinHistory, modifier: Modifier = Modifier) {
    val viewModel = viewModel<MyCoinHistoryPageViewModel>()

    val coinList by viewModel.coinList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()

    LaunchedEffect(Unit) {
        LogUtil.d("ProjectChildPage", "触发刷新")
        if (coinList.isNullOrEmpty()) {
            viewModel.netGetMyCoinHistory(true)
        }
    }
    //顶部状态栏的滚动跟随行为。一共三种，自己挑
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "积分记录")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        globalNavController?.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        PageJumpManager.navigateToCoinRank(AppRoutePath.CoinRank())
                    }) {
                        Text(text = "排行榜")
                    }
                },
                modifier = Modifier.shadow(8.dp),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            RefreshableList(
                items = coinList ?: emptyList(),
                isRefreshing = pageState.isRefreshing,
                isLoadingMore = pageState.isLoadingMore,
                allowRefresh = true,
                allowLoadMore = true,
                hasMore = pageState.hasMore,
                onRefresh = {
                    viewModel.netGetMyCoinHistory(true)
                },
                onLoadMore = {
                    viewModel.netGetMyCoinHistory(false)
                },
                itemContent = { index, item ->
                    MyCoinHistoryItemView(index, item)
                }
            )
        }

    }
}

@Composable
fun MyCoinHistoryItemView(index: Int, item: MyCoinHistoryResponseBean?) {
    item?.let {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier

                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .clickable {

                }

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 5.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.desc.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis

                    )
                }
                Text(
                    text = item.coinCount.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun MyCoinHistoryPagePreview() {
    WanAndroid_composeTheme {
        MyCoinHistoryPage(AppRoutePath.MyCoinHistory())
    }

}
