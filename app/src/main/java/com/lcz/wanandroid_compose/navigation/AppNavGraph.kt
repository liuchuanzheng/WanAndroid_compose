package com.lcz.wanandroid_compose.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lcz.wanandroid_compose.module.coin.CoinRankPage
import com.lcz.wanandroid_compose.module.coin.MyCoinHistoryPage
import com.lcz.wanandroid_compose.module.customerservice.page.CustomerServicePage
import com.lcz.wanandroid_compose.module.demo.DemoPage
import com.lcz.wanandroid_compose.module.demo.nestedscroll.NestedScrollDemoPage
import com.lcz.wanandroid_compose.module.demo.netcache.NetCacheDemoPage
import com.lcz.wanandroid_compose.module.demo.sign.page.SignPage
import com.lcz.wanandroid_compose.module.demo.slider.SliderDemoPage
import com.lcz.wanandroid_compose.module.demo.ticktokprogressbar.TickTokProgressBarPage
import com.lcz.wanandroid_compose.module.login.LoginPage
import com.lcz.wanandroid_compose.module.main.MainPage
import com.lcz.wanandroid_compose.module.search.page.SearchPage
import com.lcz.wanandroid_compose.module.setting.page.SettingPage
import com.lcz.wanandroid_compose.module.ticktok.page.TickTokPage
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.module.web.WebPage

/**
 * 作者:     刘传政
 * 创建时间:  10:45 2025/9/9
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
var globalNavController: NavHostController? = null
val TAG = "AppNavGraph"

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    globalNavController = navController
    DisposableEffect(navController) {
        var previousDestination: String? = null

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val currentRoute = destination.route ?: "unknown_route"
            val previousRoute = previousDestination ?: "app_start"
            LogUtil.i(
                TAG,
                "导航路径：${previousRoute.split('.').last().split("?").first()} → ${
                    currentRoute.split('.').last().split("?").first()
                }"
            )
            previousDestination = currentRoute
        }

        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = AppRoutePath.Main(from = "导航图默认启动"),
            //这里统一设置页面动画。如果单独页面需要设置动画，那么在composable中单独设置即可
            //动画。一个页面对应四个动画。都是定义的自己，不影响别的页面。比如ABC三个页面。当时人是B，当前在A，
            //A打开B，那么B就有个正向进入动画enterTransition
            //B再打开C，那么B就有个正向退出动画exitTransition
            //C返回B，那么B就有个反向进入动画popEnterTransition
            //B返回A，那么B就有个反向退出动画popExitTransition
            // 正向进入动画
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            // 正向退出动画
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            // 反向进入动画
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            // 反向退出动画
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable<AppRoutePath.Main> { backStackEntry ->

                val paramsBean = backStackEntry.toRoute<AppRoutePath.Main>()
                MainPage(paramsBean)

            }
            composable<AppRoutePath.Test> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Test>()
                Button(
                    modifier = Modifier
                        .padding(vertical = 100.dp)
                        .sharedElement(rememberSharedContentState("test_button"), animatedVisibilityScope = this),
                    onClick = {
                        PageJumpManager.navigateToTest2(AppRoutePath.Test2(from = "Test"))
                    }) {
                    Text(text = "当前页面是test,点击跳转test2")
                }
            }
            composable<AppRoutePath.Test2> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Test2>()
                Scaffold(bottomBar = {
                    Text(
                        "bar", modifier = Modifier
                            .height(100.dp)
                            .background(Color.Red)
                    )
                }) {
                    it
                    Button(
                        modifier = Modifier
                            .padding(vertical = 100.dp)
                            .sharedElement(rememberSharedContentState("test_button"), animatedVisibilityScope = this),
                        onClick = {
                        }) {
                        Text(text = "当前test2")
                    }
                }
            }
            composable<AppRoutePath.Login> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Login>()
                LoginPage(paramsBean)
            }
            composable<AppRoutePath.MyCoinHistory> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.MyCoinHistory>()
                MyCoinHistoryPage(paramsBean)
            }
            composable<AppRoutePath.CoinRank> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.CoinRank>()
                CoinRankPage(paramsBean)
            }
            composable<AppRoutePath.Setting> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Setting>()
                SettingPage(paramsBean)
            }
            composable<AppRoutePath.Search> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Search>()
                SearchPage(paramsBean)
            }
            composable<AppRoutePath.Demo> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Demo>()
                DemoPage(paramsBean)
            }
            composable<AppRoutePath.Sign> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Sign>()
                SignPage(paramsBean)
            }
            composable<AppRoutePath.SliderDemo> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.SliderDemo>()
                SliderDemoPage(paramsBean)
            }
            composable<AppRoutePath.TickTok> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.TickTok>()
                TickTokPage(paramsBean)
            }
            composable<AppRoutePath.TickTokProgressBar> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.TickTokProgressBar>()
                TickTokProgressBarPage(paramsBean)
            }
            composable<AppRoutePath.NetCache> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.NetCache>()
                NetCacheDemoPage(paramsBean)
            }
            composable<AppRoutePath.Web> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Web>()
                WebPage(paramsBean.url, onBackPressed = {
                    navController.popBackStack()
                })
            }
            composable<AppRoutePath.NestedScrollDemo> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.NestedScrollDemo>()
                NestedScrollDemoPage(paramsBean)
            }

            composable<AppRoutePath.CustomerService> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.CustomerService>()
                CustomerServicePage(paramsBean)
            }


        }
    }
}
