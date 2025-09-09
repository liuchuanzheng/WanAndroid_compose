package com.lcz.wanandroid_compose.module.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lcz.wanandroid_compose.navigation.AppNavGraph
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToTest
import com.lcz.wanandroid_compose.navigation.globalNavController

/**
 * 作者:     刘传政
 * 创建时间:  11:09 2025/9/9
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(paramsBean:AppRoutePath.Main,modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "WanAndroid")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(text = "MainPage")
            Button(onClick = {
                globalNavController?.app_navigateToTest(AppRoutePath.Test(from = "MainPage"))
            }) {
                Text(text = "跳转")
            }
        }
    }
}