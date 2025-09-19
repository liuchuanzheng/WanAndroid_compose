package com.lcz.wanandroid_compose.navigation

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
import com.lcz.wanandroid_compose.module.login.LoginPage
import com.lcz.wanandroid_compose.module.main.MainPage
import com.lcz.wanandroid_compose.module.search.page.SearchPage
import com.lcz.wanandroid_compose.module.setting.page.SettingPage
import com.lcz.wanandroid_compose.util.LogUtil
import kotlinx.serialization.Serializable

/**
 * 作者:     刘传政
 * 创建时间:  10:45 2025/9/9
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
var globalNavController: NavHostController? = null
val TAG = "AppNavGraph"

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
                        navController.app_navigateToTest2(AppRoutePath.Test2(from = "Test"))
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
                LoginPage()
            }
            composable<AppRoutePath.MyCoinHistory> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.MyCoinHistory>()
                MyCoinHistoryPage()
            }
            composable<AppRoutePath.CoinRank> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.CoinRank>()
                CoinRankPage()
            }
            composable<AppRoutePath.Setting> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Setting>()
                SettingPage(paramsBean)
            }
            composable<AppRoutePath.Search> { backStackEntry ->
                val paramsBean = backStackEntry.toRoute<AppRoutePath.Search>()
                SearchPage(paramsBean)
            }

        }
    }
}

fun NavHostController.app_navigateToTest(paramsBean: AppRoutePath.Test) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

fun NavHostController.app_navigateToTest2(paramsBean: AppRoutePath.Test2) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

fun NavHostController.app_navigateToLogin(paramsBean: AppRoutePath.Login) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

fun NavHostController.app_navigateToMyCoinHistory(paramsBean: AppRoutePath.MyCoinHistory) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

fun NavHostController.app_navigateToCoinRank(paramsBean: AppRoutePath.CoinRank) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

fun NavHostController.app_navigateToSetting(paramsBean: AppRoutePath.Setting) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}
fun NavHostController.app_navigateToSearch(paramsBean: AppRoutePath.Search) {
    this.navigate(paramsBean)
    LogUtil.i(TAG, "导航传递参数:${paramsBean}")
}

object AppRoutePath {
    @Serializable
    data class Main(val description: String = "主页", val from: String = "")

    @Serializable
    data class Test(val description: String = "测试页", val from: String = "")

    @Serializable
    data class Test2(val description: String = "测试页2", val from: String = "")

    @Serializable
    data class Login(val description: String = "登录页", val from: String = "")

    @Serializable
    data class MyCoinHistory(val description: String = "我的积分", val from: String = "")

    @Serializable
    data class CoinRank(val description: String = "积分排行榜", val from: String = "")
    @Serializable
    data class Setting(val description: String = "设置页", val from: String = "")
    @Serializable
    data class Search(val description: String = "搜索页", val from: String = "")
}
