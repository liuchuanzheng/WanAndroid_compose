package com.lcz.wanandroid_compose.module.takeout.wdiget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//这个组件的适配性不好，很多值都是写死的。不过绘制思路值得学习。

@Composable
fun CouponBorderBox(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0xFFF64C36),
    notchColor: Color = Color(0xFFF64C36),
    backgroundColor: Color = Color(0xFFFBEFF3),
    dividerX: Float = 0f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
//        .background(backgroundColor)
            .clipToBounds() // 添加这个修饰符不允许绘制溢出
            .drawWithContent {
                val cornerRadius = 8.dp.toPx()
                clipPath(
                    Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(0f, 0f, size.width, size.height),
                                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                            )
                        )
                    }
                ) {

                    val width = size.width
                    val height = size.height
                    val radius = 8.dp.toPx()
                    // 绘制圆角背景
                    drawRoundRect(
                        color = backgroundColor,
                        cornerRadius = CornerRadius(cornerRadius)
                    )
                    // 绘制原始内容
                    this@drawWithContent.drawContent()

                    // 绘制边框
                    drawRoundRect(
                        topLeft = Offset(0f, 1f),
                        color = borderColor,
                        size = Size(size.width, size.height - 2),
                        cornerRadius = CornerRadius(cornerRadius),
                        style = Stroke(width = 1.dp.toPx())
                    )
                    // 绘制虚线
                    val dashPath = Path().apply {
                        moveTo(dividerX, cornerRadius)
                        lineTo(dividerX, height - cornerRadius)
                    }
                    drawPath(
                        path = dashPath,
                        color = borderColor,
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                        )
                    )
                    //

                    // 绘制上下凹口
                    val notchRadius = 3.dp.toPx()
                    val strokeWidth = 1.dp.toPx()

                    // 上凹口
                    drawCircle(
                        color = notchColor,
                        radius = notchRadius,
                        center = Offset(dividerX, 0f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = notchRadius - 1f,
                        center = Offset(dividerX, 0f),
                    )
                    // 下凹口
                    drawCircle(
                        color = notchColor,
                        radius = notchRadius,
                        center = Offset(dividerX, height),
                        style = Stroke(width = strokeWidth)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = notchRadius - 1f,
                        center = Offset(dividerX, height),
                    )
                }


            }
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun CouponBorderBoxPreview() {
    Box(
        modifier = Modifier
            .height(200.dp)
            .background(Color.White)
            .onSizeChanged { size -> // 新增尺寸监听
                val width = size.width
                val height = size.height
                // 这里可以处理尺寸数据
            }
    ) {
        CouponBorderBox(
            dividerX = 100f,
            modifier = Modifier
                .fillMaxWidth()

                .height(100.dp)
        ) {
            // 这里放优惠券内容
            Text("满30减5", modifier = Modifier.padding(8.dp))
        }
    }
}
// 使用示例（可以放在需要显示优惠券的地方）：
/*
CouponBorderBox(
    modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(16.dp)
) {
    // 这里放优惠券内容
    Text("满30减5", modifier = Modifier.padding(8.dp))
}
*/