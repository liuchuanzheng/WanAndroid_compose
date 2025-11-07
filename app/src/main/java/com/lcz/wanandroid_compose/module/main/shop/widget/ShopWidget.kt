package com.lcz.wanandroid_compose.module.main.shop.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.widget.CoilImage
import kotlin.random.Random

/**
 * 作者:     刘传政
 * 创建时间:  15:50 2025/11/7
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun ShopWidget() {
    val products = remember {
        listOf(
            Product(
                id = 1,
                name = "智能手机",
                price = "¥2999.00",
                imageUrl = "https://picsum.photos/200/200?random=1",
                description = "高性能智能手机，6.5英寸屏幕",
            ),
            Product(
                id = 2,
                name = "无线耳机",
                price = "¥599.00",
                imageUrl = "https://picsum.photos/200/200?random=2",
                description = "蓝牙5.0，降噪功能",
            ),
            Product(
                id = 3,
                name = "智能手表",
                price = "¥1299.00",
                imageUrl = "https://picsum.photos/200/200?random=3",
                description = "运动健康监测，长续航",
            ),
            Product(
                id = 4,
                name = "笔记本电脑",
                price = "¥5999.00",
                imageUrl = "https://picsum.photos/200/200?random=4",
                description = "轻薄便携，高性能处理器",
            ),
            Product(
                id = 5,
                name = "平板电脑",
                price = "¥1999.00",
                imageUrl = "https://picsum.photos/200/200?random=5",
                description = "10英寸屏幕，适合学习和娱乐",
            ),
            Product(
                id = 6,
                name = "数码相机",
                price = "¥3999.00",
                imageUrl = "https://picsum.photos/200/200?random=6",
                description = "高像素，专业摄影",
            ),
            Product(
                id = 7,
                name = "游戏手柄",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=7",
                description = "无线连接，震动反馈",
            ),
            Product(
                id = 8,
                name = "机械键盘",
                price = "¥499.00",
                imageUrl = "https://picsum.photos/200/200?random=8",
                description = "RGB背光，青轴手感",
            ),
            Product(
                id = 9,
                name = "显示器",
                price = "¥1599.00",
                imageUrl = "https://picsum.photos/200/200?random=9",
                description = "27英寸4K分辨率",
            ),
            Product(
                id = 10,
                name = "移动电源",
                price = "¥199.00",
                imageUrl = "https://picsum.photos/200/200?random=10",
                description = "20000mAh大容量",
            ),
            Product(
                id = 11,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=11",
                description = "语音助手，智能控制",
            ),
            Product(
                id = 12,
                name = "智能电视",
                price = "¥3999.00",
                imageUrl = "https://picsum.photos/200/200?random=12",
                description = "4K分辨率，智能控制",
            ),
            Product(
                id = 13,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=13",
                description = "语音助手，智能控制",
            ),
            Product(
                id = 14,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=14",
                description = "语音助手，智能控制",
            ),
            Product(
                id = 15,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=15",
                description = "语音助手，智能控制",
            ),
            Product(
                id = 16,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=16",
                description = "语音助手，智能控制",
            ),
            Product(
                id = 17,
                name = "智能音箱",
                price = "¥299.00",
                imageUrl = "https://picsum.photos/200/200?random=17",
                description = "语音助手，智能控制",
            ),

            )
    }
    val isGridLayout = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "商品列表", modifier = Modifier.padding(start = 16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isGridLayout.value) "双列网格布局" else "双列瀑布流布局",
                    modifier = Modifier.padding(start = 15.dp),
                )
                IconButton(
                    onClick = { isGridLayout.value = !isGridLayout.value },
                ) {
                    Icon(
                        imageVector = if (isGridLayout.value) Icons.Default.GridView else Icons.Default.Stream,
                        contentDescription = "切换布局",
                    )
                }
            }

        }
        AnimatedContent(
            targetState = isGridLayout.value,
            transitionSpec = {
                //ai生成的动画，我其实不会写
                fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) togetherWith
                        fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
        ) { targetState ->
            if (targetState) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),//整体内容的padding
                    verticalArrangement = Arrangement.spacedBy(8.dp),//垂直方向的间距
                    horizontalArrangement = Arrangement.spacedBy(8.dp),//水平方向的间距
                ) {
                    items(products.size) {
                        ProductItem(product = products[it], isGridLayout = isGridLayout.value)
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(products.size) { product ->
                        ProductItem(product = products[product], isGridLayout = isGridLayout.value)
                    }
                }
            }
        }


    }
}

@Composable
fun ProductItem(product: Product, isGridLayout: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isGridLayout) 200.dp else product.randomHeight.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        CoilImage(
            model = product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(text = product.name, fontSize = 14.sp, color = Color.Black)
        Text(text = product.description, fontSize = 12.sp, color = Color.Gray)
        Text(text = product.price, color = Color.Red, fontSize = 16.sp)
    }
}

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val imageUrl: String,
    val description: String,
    val randomHeight: Int = Random.nextInt(200, 400),//随机高度，模拟瀑布流效果
)

@Preview
@Composable
fun ShopWidgetPreview() {
    ShopWidget()
}
