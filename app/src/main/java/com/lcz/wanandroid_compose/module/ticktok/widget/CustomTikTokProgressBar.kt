package com.lcz.wanandroid_compose.module.ticktok.widget

import android.R.attr.end
import android.R.attr.fontWeight
import android.R.attr.radius
import android.R.attr.strokeWidth
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.common.math.LinearTransformation.horizontal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 自定义抖音风格视频进度条
 * 功能：显示时间、可拖动、拖动时变高大、松手后2秒恢复狭窄、支持点击跳转
 */
@Composable
fun CustomTikTokProgressBar(
    modifier: Modifier = Modifier,
    progress: Float, // 0f-1f
    currentTime: String, // 当前时间文本
    totalTime: String, // 总时长文本
    onProgressChanged: (Float, isUser: Boolean) -> Unit, // 进度变化回调
) {
    // 状态管理
    var isDragging by remember { mutableStateOf(false) }
    var showExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val progressRefresh by rememberUpdatedState(progress)

    // 进度条高度动画 (正常2dp/展开8dp)
    val progressHeight = remember { Animatable(2f) }
    // 滑块大小动画 (正常12dp/展开18dp)
    val thumbSize = remember { Animatable(12f) }

    // 拖动交互与动画控制
    LaunchedEffect(isDragging) {
        if (isDragging) {
            showExpanded = true
            // 同时启动多个动画
            launch {
                progressHeight.animateTo(8f, tween(200))
            }
            launch {
                thumbSize.animateTo(18f, tween(200))
            }
        } else if (showExpanded) {
            // 拖动结束后2秒恢复正常状态
            delay(2000)
            showExpanded = false
            launch {
                progressHeight.animateTo(2f, tween(300))
            }
            launch {
                thumbSize.animateTo(12f, tween(300))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (showExpanded) {
            // 时间显示
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // 当前时间
                Text(
                    text = currentTime,
                    color = Color.Red,
                    fontSize = 20.sp,
                )
                // 总时长
                Text(
                    text = " /$totalTime",
                    color = Color.Red.copy(
                        alpha = 0.5f
                    ),
                    fontSize = 18.sp,
                )
            }
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .height(30.dp)//固定一个高度，防止进度条变窄时不好触摸
                // 合并点击和拖动事件处理
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (!isDragging) {
                            val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                            onProgressChanged(newProgress, true)
                            // 点击时也显示展开状态2秒
                            scope.launch {
                                showExpanded = true
                                progressHeight.animateTo(8f, tween(200))
                                thumbSize.animateTo(18f, tween(200))
                                delay(2000)
                                showExpanded = false
                                progressHeight.animateTo(2f, tween(300))
                                thumbSize.animateTo(12f, tween(300))
                            }
                        }
                    }
                }

//                .background(Color.Blue)
                .padding(bottom = 5.dp)

        ) {
            // 进度条容器
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressHeight.value.dp)
                    .clip(RoundedCornerShape(50)) // 圆角进度条
                    .background(Color.Gray.copy(alpha = 0.5f)) // 抖音风格深灰底色
                    .shadow(elevation = if (showExpanded) 4.dp else 0.dp)

            ) {


                // 拖动交互区域 - 只处理拖动事件
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    scope.launch {
                                        isDragging = true
                                    }
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    //乘3是增加拖动速度，提升用户体验
                                    val newProgress = (progressRefresh + dragAmount.x * 2 / size.width).coerceIn(0f, 1f)
                                    onProgressChanged(newProgress, true)
                                },
                                onDragEnd = {
                                    scope.launch {
                                        isDragging = false
                                    }
                                    onProgressChanged(progressRefresh, false)
                                },
                                onDragCancel = {
                                    scope.launch {
                                        isDragging = false
                                    }
                                }
                            )
                        }
                        .drawBehind {
                            // 绘制已播放进度
                            drawRect(
                                color = Color(0xFFFF2C55),
                                size = Size(size.width * progressRefresh, size.height)
                            )

                            // 计算绿点位置 - 修复进度少时不显示的问题
                            val dotRadius = size.height * 0.5f // 使用进度条高度的70%作为半径
                            val dotCenterX = size.width * progressRefresh

                            // 确保绿点始终可见，不会超出边界
                            val adjustedCenterX = dotCenterX.coerceIn(
                                dotRadius, // 最小X坐标
                                size.width - dotRadius // 最大X坐标
                            )

                            // 绘制绿点
                            drawCircle(
                                color = Color.Green,
                                radius = dotRadius,
                                center = Offset(adjustedCenterX, size.height / 2)
                            )
                        }
                )


            }
        }


    }
}