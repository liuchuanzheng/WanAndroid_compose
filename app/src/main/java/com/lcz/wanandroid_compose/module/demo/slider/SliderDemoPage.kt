package com.lcz.wanandroid_compose.module.demo.slider

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
//测试动画调式.有@Preview的预览中，会自动检测有没有可以调试的动画，有就会在预览中多一个《Start Animation Preview》按钮
//调试时可以随意预览不同进度的效果，组合的动画也会一一列出，可以调节速率。很棒
@Preview
@Composable
fun Demo2() {
    var bigBox by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(targetState = bigBox, label = "demo2SizeAndColor")
    val customSize by transition.animateDp(label = "demo2Size",
        transitionSpec = {
        tween(durationMillis = 2000, easing = LinearEasing)
    }) {
        when(it) {
            true -> 100.dp
            false -> 50.dp
        }
    }

    val customColor by transition.animateColor(label = "demo2Color",
        transitionSpec = {
        tween(durationMillis = 1000, easing = LinearEasing)
    }) {
        when(it) {
            true -> Color.Red
            false -> Color.Blue
        }
    }

    Box(modifier = Modifier
        .background(customColor)
        .size(customSize)
        .clickable {
            bigBox = !bigBox
        })
}