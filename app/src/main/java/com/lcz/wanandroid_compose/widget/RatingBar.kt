package com.lcz.wanandroid_compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 作者:     刘传政
 * 创建时间:  14:34 2025/10/24
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float = 0f,// 0-5分
    activeColor: Color = Color(0xFFFF7700),
    inactiveColor: Color = Color(0xFFBDBDBD),
) {
// 获取当前密度，用于dp转px
    Row(modifier = modifier) {
        repeat(5) { index ->
            val fillProgress = (rating - index).coerceIn(0f, 1f)
            Box(
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    tint = inactiveColor,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )
                // 前景星（根据进度裁剪）
                // 创建自定义矩形形状
                val customRectangle = GenericShape { size, _ ->
                    // 定义矩形的四个角点
                    val left = 0f
                    val top = 0f
                    val right = size.width * fillProgress
                    val bottom = size.height

                    // 创建矩形路径
                    moveTo(left, top)
                    lineTo(right, top)
                    lineTo(right, bottom)
                    lineTo(left, bottom)
                    close()
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(customRectangle)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = activeColor,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }

        }
    }

}

@Preview
@Composable
fun RatingBarPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        RatingBar(rating = 3.2f)
    }
}
