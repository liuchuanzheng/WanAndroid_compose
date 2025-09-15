package com.lcz.wanandroid_compose.module.coin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun MyCoinHistoryPage(modifier: Modifier = Modifier) {
    val viewModel = viewModel<MyCoinHistoryPageViewModel>()

    val coinList by viewModel.coinList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()

    LaunchedEffect(Unit) {
        LogUtil.d("ProjectChildPage", "触发刷新")
        if (coinList.isNullOrEmpty()) {
            viewModel.netGetMyCoinHistory(true)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "积分记录")
                }, navigationIcon = {
                    IconButton(onClick = {
                        globalNavController?.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
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
                itemContent = {
                    Text("aaaa${it?.id}")
                }
            )
        }

    }
}

@Preview
@Composable
fun MyCoinHistoryPagePreview() {
    WanAndroid_composeTheme {
        MyCoinHistoryPage()
    }

}
