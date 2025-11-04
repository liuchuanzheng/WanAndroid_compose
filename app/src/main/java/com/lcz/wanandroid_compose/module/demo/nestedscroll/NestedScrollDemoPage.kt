package com.lcz.wanandroid_compose.module.demo.nestedscroll

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.util.LogUtil
import kotlin.math.roundToInt

/**
 * 作者:     刘传政
 * 创建时间:  14:22 2025/11/3
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun NestedScrollDemoPage(
    paramsBean: AppRoutePath.NestedScrollDemo,
) {
    Demo1()
//    Demo2()
//    Demo3()
}

/**
 * 本身就是支持嵌套滚动的，所以不需要额外处理
 * 而且滑动和fling都是连贯的
 *
 */
@Composable
fun Demo2() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.Red)
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .border(width = 1.dp, color = Color.DarkGray)
                .height(250.dp)
                .fillMaxWidth(),
        ) {
            item {
                LazyColumn(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Green)
                        .height(100.dp)
                        .fillMaxWidth(),
                ) {
                    items(count = 10) { index ->
                        Text(
                            text = "Inner LazyColumn:Item ${index + 1}",
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Color.Cyan),
                        )
                    }
                }
            }
            items(count = 20) { index ->
                Text(
                    text = "Outer LazyColumn:Item ${index + 1}",
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Yellow),
                )
            }

        }

    }
}

/**
 * 处理分发与消费
 *
 */
@Composable
fun Demo3() {
    val minHeight = 80.dp
    val maxHeight = 200.dp
    val density = LocalDensity.current

    val minHeightPx = with(density) {
        minHeight.toPx()
    }

    val maxHeightPx = with(density) {
        maxHeight.toPx()
    }

    var currentHeightPx by remember {
        mutableStateOf(maxHeightPx)
    }
    val parentConnection = remember {
        object : NestedScrollConnection {
            // 处理 Pre-scroll (子组件滑动前)
            // 直接调用父的实现，父不优先消耗
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "父组件获取到 onPreScroll available: $available")
                var myConsumeY  = available.y/4
                currentHeightPx += myConsumeY
                return Offset(x = 0f, myConsumeY)
//                return super.onPreScroll(available, source)
            }

            // 处理 Post-scroll (子组件滑动后)
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                // 父组件消费最后的滚动距离
//                    offsetY += available.y
//                    return Offset(x = 0f, available.y) // 返回实际消耗的量
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "父组件获取到 onPostScroll consumed: $consumed, available: $available")
                return Offset(x = 0f, (available.y ))
//                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.Red)
            .fillMaxSize()
            .nestedScroll(parentConnection),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    with(density) {
                        currentHeightPx.toDp()
                    },
                )
                .background(Color.Green),
        )
        var offsetY by remember { mutableFloatStateOf(0f) }
        val dispatcher = remember { NestedScrollDispatcher() }
        val connection = remember {
            object : NestedScrollConnection {
                // 处理 Pre-scroll (子组件滑动前)
                // 直接调用父的实现，父不优先消耗
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    return super.onPreScroll(available, source)
                }

                // 处理 Post-scroll (子组件滑动后)
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    // 父组件消费最后的滚动距离
//                    offsetY += available.y
//                    return Offset(x = 0f, available.y) // 返回实际消耗的量
                    return super.onPostScroll(consumed, available, source)
                }
            }
        }

        Column(
            Modifier
                .clipToBounds() // 裁剪超出部分
                .offset { IntOffset(x = 0, y = offsetY.roundToInt()) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        // 父组件实际消费的滚动距离
                        val consumed = dispatcher.dispatchPreScroll(
                            available = Offset(x = 0f, y = delta),
                            source = NestedScrollSource.UserInput,
                        )
                        LogUtil.e(
                            tag = "NestedScrollDemoPage",
                            msg = "子组件获取到父组件消耗了consumed: $consumed, ",
                        )



                        offsetY += (delta-consumed.y)
                        // 通知父组件，子组件实际消费的距离
                        dispatcher.dispatchPostScroll(
                            consumed = Offset(
                                x = 0f,
                                y = (delta - consumed.y),
                            ),
                            available = Offset.Zero, source = NestedScrollSource.UserInput,
                        )

                    },
                    orientation = Orientation.Vertical,
                )
                .nestedScroll(
                    dispatcher = dispatcher,
                    connection = connection,
                ), // 新增
        ) {
            for (i in 1..10) {
                Text(text = "Text $i")
            }
        }

    }
}

/**
 * 大图跟对滚动而变化高度的示例
 *
 */
@Composable
fun Demo1() {
    val minHeight = 80.dp
    val maxHeight = 200.dp
    val density = LocalDensity.current

    val minHeightPx = with(density) {
        minHeight.toPx()
    }

    val maxHeightPx = with(density) {
        maxHeight.toPx()
    }

    var currentHeightPx by remember {
        mutableStateOf(maxHeightPx)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            /**
             * 预先劫持滑动事件，消费后再交由子布局。
             * available：当前可用的滑动事件偏移量
             * source：滑动事件的类型
             * 返回值：当前组件消费的滑动事件偏移量，如果不想消费可返回Offset.Zero
             */
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "onPreScroll available: $available")
                if (source == NestedScrollSource.Drag) {
                    if (available.y < 0) { //手指从下往上滑动
                        val dH = minHeightPx - currentHeightPx  // 向上滑动过程中，还差多少达到最小高度
                        if (available.y > dH) {  // 如果当前可用的滑动距离全部消费都不足以达到最小高度，就将当前可用距离全部消费掉
                            currentHeightPx += available.y
                            return Offset(x = 0f, y = available.y)
                        } else {  // 如果当前可用的滑动距离足够达到最小高度，就只消费掉需要的距离。剩余的给到子组件。
                            currentHeightPx += dH
                            return Offset(x = 0f, y = dH)
                        }
                    } else { //手指从上往下滑动
                        val dH = maxHeightPx - currentHeightPx  // 向下滑动过程中，还差多少达到最大高度
                        if (available.y < dH) {  // 如果当前可用的滑动距离全部消费都不足以达到最大高度，就将当前可用距离全部消费掉
                            currentHeightPx += available.y
                            return Offset(x = 0f, y = available.y)
                        } else {  // 如果当前可用的滑动距离足够达到最大高度，就只消费掉需要的距离。剩余的给到子组件。
                            currentHeightPx += dH
                            return Offset(x = 0f, y = dH)
                        }
                    }
                } else { // 其他情况，不处理
                    return Offset.Zero
                }
            }

            /**
             * 获取子布局处理后的滑动事件
             * consumed：之前消费的所有滑动事件偏移量
             * available：当前剩下还可用的滑动事件偏移量
             * source：滑动事件的类型
             * 返回值：当前组件消费的滑动事件偏移量，如果不想消费可返回 Offset.Zero ，则剩下偏移量会继续交由当前布局的父布局进行处理
             */
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "onPostScroll consumed: $consumed, available: $available")
                return Offset.Zero
            }

            /**
             * 获取 Fling 开始时的速度
             * available：Fling 开始时的速度
             * 返回值：当前组件消费的速度，如果不想消费可返回 Velocity.Zero
             */
            override suspend fun onPreFling(available: Velocity): Velocity {
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "onPreFling available: $available")
                return Velocity.Zero
            }

            /**
             * 获取 Fling 结束时的速度信息
             * consumed：之前消费的所有速度
             * available：当前剩下还可用的速度
             * 返回值：当前组件消费的速度，如果不想消费可返回Velocity.Zero，剩下速度会继续交由当前布局的父布局进行处理
             */
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                LogUtil.i(tag = "NestedScrollDemoPage", msg = "onPostFling consumed: $consumed, available: $available")
                return Velocity.Zero
            }
        }
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.Red)
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    with(density) {
                        currentHeightPx.toDp()
                    },
                )
                .background(Color.Green),
        )

        LazyColumn {
            repeat(50) {
                item {
                    Text(
                        text = "item --> $it",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun NestedScrollDemoPreview() {
    NestedScrollDemoPage(paramsBean = AppRoutePath.NestedScrollDemo())
}
