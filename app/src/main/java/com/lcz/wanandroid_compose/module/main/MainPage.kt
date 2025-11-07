package com.lcz.wanandroid_compose.module.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lcz.wanandroid_compose.module.main.home.widget.HomeWidget
import com.lcz.wanandroid_compose.module.main.project.widget.ProjectWidget
import com.lcz.wanandroid_compose.module.main.mine.widget.MineWidget
import com.lcz.wanandroid_compose.module.main.shop.widget.ShopWidget
import com.lcz.wanandroid_compose.module.main.takeout.wdiget.TakeoutWdiget
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.widget.BottomBar
import com.lcz.wanandroid_compose.widget.BottomBarItem
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  11:09 2025/9/9
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(paramsBean: AppRoutePath.Main, modifier: Modifier = Modifier) {
    val bottomBarItems = listOf(
        BottomBarItem(
            icon = Icons.Default.Home,
            text = "首页"
        ),
        BottomBarItem(
            icon = Icons.Default.Email,
            text = "项目"
        ),
        BottomBarItem(
            icon = Icons.Default.LocationOn,
            text = "外卖"
        ),
        BottomBarItem(
            icon = Icons.Default.ShoppingBasket,
            text = "商品"
        ),
        BottomBarItem(
            icon = Icons.Default.Person,
            text = "我的"
        ),
    )

    val pagerState = rememberPagerState(initialPage = 0) { bottomBarItems.size }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            BottomBar(items = bottomBarItems, currentSelectIndex = pagerState.currentPage) {
                coroutineScope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
//                .statusBarsPadding()
//                .navigationBarsPadding()
                .padding(bottom = it.calculateBottomPadding())//这里只添加bottom,让内容区域在底部导航栏上方,否则内容区域是被底部导航栏盖住的
                .fillMaxSize()
        ) {
            HorizontalPager(
                userScrollEnabled = false,//是否允许用户手动滑动
                state = pagerState
            ) {
                when (it) {
                    0 -> {
                        HomeWidget()
                    }

                    1 -> {
                        ProjectWidget()
                    }

                    2 -> {
                        TakeoutWdiget()
                    }

                    3 -> {
                        ShopWidget()
                    }

                    4 -> {
                        MineWidget()
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun MainPagePreview() {
    WanAndroid_composeTheme {
        MainPage(AppRoutePath.Main())
    }
}
