package com.lcz.wanandroid_compose.navigation

import android.R.attr.data
import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lcz.wanandroid_compose.module.main.MainPage
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

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    globalNavController = navController
    DisposableEffect(navController) {
        var previousDestination: String? = null

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val currentRoute = destination.route ?: "unknown_route"
            val previousRoute = previousDestination ?: "app_start"
            LogUtil.i(TAG, "导航路径：${previousRoute.split('.').last()} → ${currentRoute.split('.').last()}")
            previousDestination = currentRoute
        }

        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    NavHost(navController = navController, startDestination = AppRoutePath.Main(from = "导航图默认启动")) {
        composable<AppRoutePath.Main> { backStackEntry ->

            val paramsBean = backStackEntry.toRoute<AppRoutePath.Main>()
            MainPage(paramsBean)

        }
        composable<AppRoutePath.Test> { backStackEntry ->
            val paramsBean = backStackEntry.toRoute<AppRoutePath.Test>()
            Button(modifier = Modifier.padding(vertical = 100.dp), onClick = {
                navController.app_navigateToTest2(AppRoutePath.Test2(from = "Test"))
            }) {
                Text(text = "当前页面是test,点击跳转test2")
            }
        }
        composable<AppRoutePath.Test2> { backStackEntry ->
            val paramsBean = backStackEntry.toRoute<AppRoutePath.Test2>()
            Button(modifier = Modifier.padding(vertical = 100.dp), onClick = {

            }) {
                Text(text = "当前test2")
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

object AppRoutePath {
    @Serializable
    data class Main(val from: String = "")

    @Serializable
    data class Test(val from: String = "")

    @Serializable
    data class Test2(val from: String = "")
}
