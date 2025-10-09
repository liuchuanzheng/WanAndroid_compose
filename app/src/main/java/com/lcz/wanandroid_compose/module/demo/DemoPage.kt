package com.lcz.wanandroid_compose.module.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToSign
import com.lcz.wanandroid_compose.navigation.app_navigateToSliderDemo
import com.lcz.wanandroid_compose.navigation.app_navigateToTickTok
import com.lcz.wanandroid_compose.navigation.globalNavController

/**
 * 作者:     刘传政
 * 创建时间:  9:19 2025/9/23
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun DemoPage(paramsBean: AppRoutePath.Demo) {
    LazyColumn(modifier = Modifier.statusBarsPadding()) {
        item {
            ItemView("手写签名", "通过画笔签名，保存图片") {
                globalNavController?.app_navigateToSign(AppRoutePath.Sign())
            }
            ItemView("滑动条", "滑动条demo") {
                globalNavController?.app_navigateToSliderDemo(AppRoutePath.SliderDemo())
            }
            ItemView("抖音视频", "抖音视频demo") {
                globalNavController?.app_navigateToTickTok(AppRoutePath.TickTok())
            }
        }
    }

}

@Composable
fun ItemView(title: String, description: String = "暂无描述", onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clickable {
                onClick.invoke()
            }
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        ) {
            Text("${title}", fontSize = 16.sp)
            Text("${description}", fontSize = 14.sp, color = Color.Gray)
        }
    }

}