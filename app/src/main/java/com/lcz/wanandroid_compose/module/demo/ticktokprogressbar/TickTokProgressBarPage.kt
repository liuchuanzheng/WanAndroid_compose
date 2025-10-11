package com.lcz.wanandroid_compose.module.demo.ticktokprogressbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lcz.wanandroid_compose.module.ticktok.widget.CustomTikTokProgressBar
import com.lcz.wanandroid_compose.navigation.AppRoutePath

/**
 * 作者:     刘传政
 * 创建时间:  9:50 2025/10/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun TickTokProgressBarPage(paramsBean: AppRoutePath.TickTokProgressBar) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        var progress by remember { mutableStateOf(0.3f) } // 当前进度(0-1)
        var currentTime by remember { mutableStateOf("00:45") } // 当前时间
        var totalTime by remember { mutableStateOf("02:30") } // 总时长

        Column(modifier = Modifier.fillMaxWidth()) {

            // 视频进度条
            CustomTikTokProgressBar(
                progress = progress,
                currentTime = currentTime,
                totalTime = totalTime,
                onProgressChanged = { newProgress, isUser ->
                    progress = newProgress
                    // 这里可以添加更新当前时间的逻辑
                }
            )
        }
    }
}