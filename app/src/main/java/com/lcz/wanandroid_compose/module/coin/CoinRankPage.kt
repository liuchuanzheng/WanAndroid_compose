package com.lcz.wanandroid_compose.module.coin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.coin.data.MyCoinHistoryResponseBean
import com.lcz.wanandroid_compose.module.main.mine.bean.MyCoinResponseBean
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.widget.RefreshableList

/**
 * 作者:     刘传政
 * 创建时间:  16:15 2025/9/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinRankPage(modifier: Modifier = Modifier) {
    //顶部状态栏的滚动跟随行为。一共三种，自己挑
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val viewModel: CoinRankPageViewModel = viewModel()
    val coinRankList by viewModel.coinRankList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()
    val myCoin by viewModel.myCoin.collectAsState()

    LaunchedEffect(Unit) {
        LogUtil.d("ProjectChildPage", "触发刷新")
        if (coinRankList.isNullOrEmpty()) {
            viewModel.netGetCoinRankList(true)
            viewModel.netGetMyCoin()
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
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
                title = {
                    Text(text = "积分排行榜")
                },
                actions = {
                    Text(
                        text = "我的排名: ${myCoin.rank ?: 0}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = myCoin.coinCount.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 5.dp, end = 16.dp),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            RefreshableList(
                items = coinRankList ?: emptyList(),
                isRefreshing = pageState.isRefreshing,
                isLoadingMore = pageState.isLoadingMore,
                allowRefresh = true,
                allowLoadMore = true,
                hasMore = pageState.hasMore,
                onRefresh = {
                    viewModel.netGetCoinRankList(true)
                },
                onLoadMore = {
                    viewModel.netGetCoinRankList(false)
                },
                itemContent = { index, item ->
                    CoinRankItemView(index, item)
                }
            )
        }
    }
}

@Composable
fun CoinRankItemView(index: Int, item: MyCoinResponseBean?) {
    item?.let {
        // 新增颜色判断逻辑
        val (rankColor, rankSize) = when (it.rank?.toIntOrNull() ?: 0) {
            1 -> Color(0xFFFFD700) to 24f  // 金色
            2 -> Color(0xFFC0C0C0) to 22f  // 银色
            3 -> Color(0xFFCD7F32) to 20f  // 铜色
            in 4..50 -> Color.Red to 18f    // 前50名
            in 51..100 -> Color.Blue to 16f // 前100名
            in 101..1000 -> Color.Green to 14f // 前1000名
            else -> Color.Gray to 12f       // 其他
        }

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
                Row(modifier = Modifier.weight(1f)) {

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when (it.rank?.toIntOrNull() ?: 0) {
                            1 -> Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "金牌",
                                tint = rankColor,
                                modifier = Modifier.size(24.dp)
                            )

                            2 -> Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "银牌",
                                tint = rankColor,
                                modifier = Modifier.size(22.dp)
                            )

                            3 -> Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "铜牌",
                                tint = rankColor,
                                modifier = Modifier.size(20.dp)
                            )

                            else -> Text(
                                text = item.rank.toString(),
                                color = rankColor,
                                fontSize = rankSize.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                    // 修改排名显示部分

                    Text(
                        text = item.username.toString(),
                        modifier = Modifier.weight(3f)
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
fun CoinRankPagePreview() {
    WanAndroid_composeTheme {
        CoinRankPage()
    }

}