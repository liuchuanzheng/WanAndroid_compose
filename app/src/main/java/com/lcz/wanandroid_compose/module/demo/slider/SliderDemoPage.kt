package com.lcz.wanandroid_compose.module.demo.slider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.util.LogUtil

/**
 * 作者:     刘传政
 * 创建时间:  9:55 2025/9/28
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun SliderDemoPage(modifier: AppRoutePath.SliderDemo) {
    val sliderValue = remember { mutableStateOf(0.5f) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        LogUtil.i(msg = "sliderValue改变: ${sliderValue.value}")
        Slider(
            value = sliderValue.value,
            onValueChange = {
                //此回调在用户滑动时调用，代码设置时不会调用
                sliderValue.value = it
                LogUtil.i(msg = "onValueChange Slider value: $it")
            },
            onValueChangeFinished = {
                // 此回调在用户完成滑动时调用，代码设置时不会调用
                LogUtil.i(msg = "onValueChangeFinished Slider value: ${sliderValue.value}")
            },
            valueRange = 0f..1f,
            steps = 0,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                sliderValue.value = 0.8f
            }
        ) {
            Text(text = "点击我")
        }
    }
}