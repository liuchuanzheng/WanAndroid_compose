package com.lcz.wanandroid_compose.module.takeout.wdiget

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Beenhere
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lcz.wanandroid_compose.R
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.util.ToastUtil
import com.lcz.wanandroid_compose.widget.Banner
import com.lcz.wanandroid_compose.widget.CoilImage
import com.lcz.wanandroid_compose.widget.RatingBar
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  9:51 2025/11/5
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun DiancanCard(modifier: Modifier = Modifier) {

    val curPagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 },
    )
    val selectedTabIndex = remember { mutableStateOf(0) }
    // 监听pager状态变化
    LaunchedEffect(curPagerState.currentPage) {
        selectedTabIndex.value = curPagerState.currentPage
    }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier

            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                ),
            )
            .background(Color.White)
            .padding(top = 10.dp),
    ) {
        Column {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedTabIndex.value = 0
                            scope.launch {
                                curPagerState.animateScrollToPage(0)
                            }
                        },
                ) {
                    Text(
                        text = "点菜",
                        color = if (selectedTabIndex.value == 0) Color.Black else Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTabIndex.value == 0) FontWeight.W700 else FontWeight.W400,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AnimatedVisibility(
                        visible = selectedTabIndex.value == 0,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Yellow),
                        )
                    }

                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .clickable {
                            selectedTabIndex.value = 1
                            scope.launch {
                                curPagerState.animateScrollToPage(1)
                            }
                        },
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            text = "评价",
                            color = if (selectedTabIndex.value == 1) Color.Black else Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex.value == 1) FontWeight.W700 else FontWeight.W400,
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "1473",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    AnimatedVisibility(
                        visible = selectedTabIndex.value == 1,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Yellow),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedTabIndex.value = 2
                            scope.launch {
                                curPagerState.animateScrollToPage(2)
                            }
                        },
                ) {
                    Text(
                        text = "商家",
                        color = if (selectedTabIndex.value == 2) Color.Black else Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTabIndex.value == 2) FontWeight.W700 else FontWeight.W400,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AnimatedVisibility(
                        visible = selectedTabIndex.value == 2,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Yellow),
                        )
                    }
                }
            }
            HorizontalPager(state = curPagerState) {
                if (it == 0) {
                    DiancanContent()
                } else if (it == 1) {
                    PingjiaContent()
                } else {
                    ShangjiaContent()
                }

            }
        }
    }
}

// 新增数据类
data class FoodCategory(
    val id: Int,
    val title: String,
    val tips: String = "",
    val items: List<FoodItem>,
)

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val monthlySales: Int = 0,
    val isNewGoods: Boolean = false,//是否是新商品
    val iconUrl: String = "https://pics5.baidu.com/feed/0b7b02087bf40ad1138fdc686d975ad6a8ecce12.jpeg@f_auto?token=ae83e21118afba0473df112732a4e82e",
)

val foodCategories = listOf(
    FoodCategory(
        0,
        "推荐",
        items = listOf(
            FoodItem(
                0,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                1,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                2,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                3,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                4,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                5,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),

            ),
    ),
    FoodCategory(
        1,
        "雪王上新",
        items = listOf(
            FoodItem(
                10,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                11,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                12,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                13,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                14,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                15,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                16,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                17,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        2,
        "鲜冰淇淋",
        items = listOf(
            FoodItem(
                20,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                21,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                22,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                23,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                24,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                25,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                26,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                27,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        3,
        "清爽果茶",
        items = listOf(
            FoodItem(
                30,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                31,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                32,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                33,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                34,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                35,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                36,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                37,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        4,
        "纯茶奶盖",
        items = listOf(

            FoodItem(
                40,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                41,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                42,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                43,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                44,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                45,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                46,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                47,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        5,
        "现煮奶茶",
        items = listOf(

            FoodItem(
                50,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                51,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                52,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                53,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                54,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                55,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                56,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                57,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        6,
        "鲜萃咖啡",
        items = listOf(

            FoodItem(
                60,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                61,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                62,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                63,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                64,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                65,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                66,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                67,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        7,
        "欢享套餐",
        items = listOf(

            FoodItem(
                70,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                71,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                72,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                73,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                74,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                75,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                76,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                77,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        8,
        "零食周边",
        items = listOf(

            FoodItem(
                80,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                81,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                82,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                83,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                84,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                85,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                86,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                87,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        9,
        "加点小料",
        items = listOf(

            FoodItem(
                90,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                91,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                92,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                93,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                94,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                95,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                96,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                97,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        10,
        "大单划算",
        items = listOf(

            FoodItem(
                100,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                101,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                102,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                103,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                104,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                105,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                106,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                107,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
    FoodCategory(
        11,
        "轻乳系列",
        items = listOf(

            FoodItem(
                110,
                "红豆奶布丁",
                10.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                111,
                "三拼霸霸奶茶（升级）",
                12.0,
                monthlySales = 1000,
                isNewGoods = true,
            ),
            FoodItem(
                112,
                "珍珠奶茶",
                8.0,
                monthlySales = 200,
                isNewGoods = false,
            ),
            FoodItem(
                113,
                "芋圆奶茶",
                15.0,
                monthlySales = 99,
                isNewGoods = true,
            ),
            FoodItem(
                114,
                "布丁奶茶",
                11.0,
                monthlySales = 100,
                isNewGoods = false,
            ),
            FoodItem(
                115,
                "椰果奶茶",
                10.0,
                monthlySales = 10,
                isNewGoods = false,
            ),
            FoodItem(
                116,
                "双拼奶茶",
                12.0,
                monthlySales = 20,
                isNewGoods = false,
            ),
            FoodItem(
                117,
                "红豆奶茶",
                9.0,
                monthlySales = 30,
                isNewGoods = false,
            ),
        ),
    ),
)
val foodHeaderBanners = listOf(
    "https://pic2.zhimg.com/v2-31d9d25265b1cf4077e2b4c60fbb1dd5_1440w.jpg",
    "https://img0.baidu.com/it/u=889855154,3673910636&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=623",
    "https://oss.tan8.com/yuepuku_dzq/92/46491/46491_prev.jpg?v=1625719482?v=1631609551",
)

// 辅助函数：计算分类起始位置
fun getCategoryStartIndex(categories: List<FoodCategory>, index: Int): Int {
    return categories.take(index).sumOf { it.items.size + 1 } + 1 // +1为header +1为banner
}


@Composable
fun FoodItemWidget(
    bean: FoodItem,
    index: Int,
    totalSize: Int,
    selectedIndex: Int,
    onClick: (index: Int) -> Unit,
    onAddClick: (FoodItem) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick.invoke(index)
            },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                model = bean.iconUrl,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = bean.name,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier,
                )
                Text(
                    text = "月售${bean.monthlySales}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        text = "¥${bean.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF06754),
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "选规格",
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(color = Color(0xFFFEDD00))
                            .clickable {
                                onAddClick.invoke(bean)
                            }
                            .padding(horizontal = 10.dp),
                    )

                }

            }
        }

    }
}

@Composable
fun CategoryItemWidget(
    bean: FoodCategory,
    index: Int,
    totalSize: Int,
    selectedIndex: Int,
    onClick: (index: Int) -> Unit,
) {

    val isSelected = index == selectedIndex
    val shape = if (index == (selectedIndex)) {
        RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
        )

    } else if (index == (selectedIndex - 1)) {
        RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 20.dp,
        )

    } else if (index == (selectedIndex + 1)) {
        RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 20.dp,
            bottomEnd = 0.dp,
        )
    } else {
        RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
        )

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
//            .clip(shape)
            .background(
                color = if (isSelected) Color.White else Color(0xFFF7F7F7),
                shape = shape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick.invoke(index)
            },
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = bean.title,
            color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.8f),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .wrapContentHeight(),

            )
    }
}

@Composable
fun PingjiaContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F6)),
    ) {
        LazyColumn {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 10.dp),
                ) {
                    Text(
                        "4.8",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.W700,
                        color = Color(0xFFFF7700),
                    )
                    Column {
                        Row {
                            RatingBar(rating = 4.8f)
                            Icon(
                                imageVector = Icons.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(12.dp)
                                    .align(Alignment.CenterVertically),
                            )
                        }
                        Text(
                            "高于81%商家",
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Gray),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "4.8",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                            color = Color(0xFFFF7700),
                        )
                        Text(
                            "商品质量",
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "4.8",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                            color = Color(0xFFFF7700),
                        )
                        Text(
                            "服务体验",
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                            ),
                        )
                        .background(Color.White),
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 10.dp),
                ) {
                    Text(
                        "外卖评价",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        "到店评价",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                    )
                }
            }
            items(20) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                    ) {
                        CoilImage(
                            model = "https://pics5.baidu.com/feed/0b7b02087bf40ad1138fdc686d975ad6a8ecce12.jpeg@f_auto?token=ae83e21118afba0473df112732a4e82e",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .clip(RoundedCornerShape(50)),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "用户${it + 1}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "2025-08-27",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            color = Color.Gray,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp),

                            )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Text(
                        "喜欢蜜雪冰城的所有奶茶哦，爱不释手，送货速度飞快",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                    )
                    CoilImage(
                        model = "https://img1.baidu.com/it/u=2855008894,2113642546&fm=253&app=138&f=JPEG?w=800&h=1066",
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.8f)),
                    )

                }
            }

        }
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun DiancanContent() {
    val myCategorys by remember { mutableStateOf(foodCategories) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val rightListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(rightListState.firstVisibleItemScrollOffset) {
        val firstVisibleIndex = rightListState.firstVisibleItemIndex
        myCategorys.forEachIndexed { index, category ->
            val categoryStartIndex = getCategoryStartIndex(
                myCategorys,
                index,
            )
            if (firstVisibleIndex >= categoryStartIndex) {
                selectedIndex = index
            }
        }
    }
    var selectedFoodItemList by remember { mutableStateOf(emptyList<FoodItem>()) }
    var showSpecDialog by remember { mutableStateOf(false) }
    var selectedFoodItem by remember { mutableStateOf<FoodItem?>(null) }
    var cartPosition by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(color = Color.White),
    ) {
        //这个一直不显示，跟后边的ui一样,只是为了获取一个红点位置。
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(60.dp)
                .alpha(0f)
                .padding(horizontal = 16.dp),
        ) {
            DiancanContentBottom(
                selectedFoodItemList = selectedFoodItemList,
                onCartPositionDetected = {
                    cartPosition = it
                },
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            // 左侧分类列表
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(color = Color.White),
            ) {
                LazyColumn {
                    itemsIndexed(
                        myCategorys,
                        key = { _, bean -> bean.id },
                    ) { index, bean ->
                        CategoryItemWidget(
                            bean,
                            index,
                            myCategorys.size,
                            selectedIndex,
                        ) {
                            selectedIndex = it
                            scope.launch {
                                rightListState.scrollToItem(
                                    index = getCategoryStartIndex(
                                        myCategorys,
                                        selectedIndex,
                                    ),
                                )
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .background(color = Color(0XFFFAFAFA)),
                        )
                    }
                }
            }
            // 右侧商品列表
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .background(color = Color.White),
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .padding(horizontal = 10.dp)
                                .background(color = Color.Gray),
                        )
                        Text(
                            "温馨提示：请适量点餐",
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .padding(horizontal = 10.dp)
                                .background(color = Color.Gray),
                        )
                    }
                    LazyColumn(state = rightListState) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            Color.Gray,
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                    ),
                            ) {
                                Banner(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    images = foodHeaderBanners,
                                    autoScroll = true,
                                    indicatorSize = 5,
                                    indicatorModifier = Modifier.padding(bottom = 10.dp),
                                    onBannerItemClick = {

                                    },
                                )
                            }
                        }
                        myCategorys.forEachIndexed { index, category ->
                            stickyHeader {
                                if (category.id == 0) {
                                    //推荐
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .background(color = Color.White)
                                            .padding(
                                                horizontal = 10.dp,
                                            ),
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .width(80.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(color = Color(0XFFF86D52))
                                                .padding(
                                                    vertical = 3.dp,
                                                    horizontal = 8.dp,
                                                ),
                                        ) {
                                            Text(
                                                text = category.title,
                                                fontSize = 14.sp,
                                                color = Color.White,
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .background(color = Color.White)
                                            .padding(
                                                horizontal = 10.dp,
                                            ),
                                    ) {
                                        Text(
                                            text = category.title,
                                            fontSize = 14.sp,
                                            color = Color.Black,
                                            modifier = Modifier,
                                        )
                                    }

                                }

                            }
                            itemsIndexed(
                                category.items,
                                key = { _, bean -> bean.id },
                            ) { index, item ->
                                FoodItemWidget(
                                    item,
                                    index,
                                    category.items.size,
                                    selectedIndex,
                                    onClick = {

                                    },
                                    onAddClick = {
                                        showSpecDialog = true
                                        selectedFoodItem = item
                                    },
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    "已经到底啦",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.TopCenter),
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedFoodItemList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = Color.White)
                    .drawBehind {
                        // 只绘制顶部边框
                        drawLine(
                            color = Color.Gray,
                            strokeWidth = 1.dp.toPx(),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                        )
                    }
                    .padding(horizontal = 16.dp),
            ) {
                DiancanContentBottom(
                    selectedFoodItemList = selectedFoodItemList,
                    onCartPositionDetected = {
                    },
                )
            }
        }

        // 自定义弹窗
        if (showSpecDialog) {
            AddToCartDialog(
                selectedFoodItem,
                cartPosition = cartPosition,
                onDismiss = { showSpecDialog = false },
                onAddToCart = {

                },
                onAnimationComplete = {
                    selectedFoodItem?.let { item ->
                        selectedFoodItemList = selectedFoodItemList.toMutableList().apply {
                            add(item)
                        }
                    }
                },
            )
        }
    }
}

//自定义弹窗
@Composable
fun AddToCartDialog(
    selectedFoodItem: FoodItem?,
    cartPosition: Offset,// 接收动态位置
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit,
    onAnimationComplete: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .background(Color.Transparent),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp))//clip会裁剪offset偏移出去的显示。导致红点飞出不显示
                    .background(Color.White),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 标题栏
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            selectedFoodItem?.name ?: "",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = { onDismiss() }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }

                    // 商品信息
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(color = Color.White),
                    ) {
                        Text(
                            "各种规格",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 10.dp),
                        )
                    }


                    // 操作按钮
                    Spacer(Modifier.height(24.dp))
                    Row {
                        Spacer(Modifier.weight(1f))

                        // 添加按钮和红点动画
                        AddToCartButtonWithAnimation(
                            cartPosition = cartPosition,
                            onAddToCart = {
                                onAddToCart()
                            },
                            onAnimationComplete = {
                                onAnimationComplete()
                            },
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun AddToCartButtonWithAnimation(
    cartPosition: Offset,// 接收动态位置
    onAddToCart: () -> Unit,
    onAnimationComplete: () -> Unit,
) {
    var isAnimating by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier,

        ) {
        Text(
            text = "+加入购物车",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(color = Color(0xFFFEDD00))
                .clickable {
                    onAddToCart()
                    isAnimating = true
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
        // 红点动画
        if (isAnimating) {
            RedDotAnimation(
                cartPosition = cartPosition,
                onAnimationComplete = {
                    isAnimating = false
                    onAnimationComplete()
                },
            )
        }
    }
}

@Composable
fun RedDotAnimation(
    cartPosition: Offset,// 接收动态位置
    onAnimationComplete: () -> Unit,
) {
    val animatedProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    var pointPosition by remember { mutableStateOf(Offset.Zero) }//红点的原始位置
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000),
            )
            onAnimationComplete()
        }
    }
    // 计算贝塞尔曲线路径
    val startOffset = Offset(0f, 0f)

    val endOffset = cartPosition - pointPosition // 调整终点位置
    val currentOffset = calculateBezierCurve(
        progress = animatedProgress.value,
        start = startOffset,
        end = endOffset,
    )

    // 红点
    Box(
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                // 获取按钮中心位置
                val position = layoutCoordinates.positionInWindow()
                val size = layoutCoordinates.size
                pointPosition = position
                LogUtil.i(tag = "RedDotAnimation", "pointPosition: $pointPosition")
            }
            .offset(
                x = with(density) { currentOffset.x.toDp() },
                y = with(density) { currentOffset.y.toDp() },
            )
            .size(16.dp)
            .clip(CircleShape)
            .background(Color.Red),
    )
}

// 计算贝塞尔曲线路径（二次贝塞尔曲线）
fun calculateBezierCurve(progress: Float, start: Offset, end: Offset): Offset {
    // 控制点 - 在起点和终点之间创建一个弧线
    val controlPoint = Offset(
        x = (start.x + end.x) / 2,
        y = start.y - 400f, // 向上弯曲，形成抛物线效果
    )

    // 二次贝塞尔曲线公式
    val x = (1 - progress) * (1 - progress) * start.x +
            2 * (1 - progress) * progress * controlPoint.x +
            progress * progress * end.x

    val y = (1 - progress) * (1 - progress) * start.y +
            2 * (1 - progress) * progress * controlPoint.y +
            progress * progress * end.y

    return Offset(x, y)
}

@Composable
fun DiancanContentBottom(

    modifier: Modifier = Modifier,
    selectedFoodItemList: List<FoodItem>,
    onCartPositionDetected: (Offset) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    modifier = Modifier
                        .onGloballyPositioned { layoutCoordinates ->
                            // 获取购物车图标中心位置
                            val position = layoutCoordinates.positionInWindow()
                            val size = layoutCoordinates.size
                            onCartPositionDetected(
                                Offset(
                                    x = position.x,
                                    y = position.y,
                                ),
                            )
                        },
                ) {
                    Text("${selectedFoodItemList.size}")
                }
            },

            ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Shopping cart",
                tint = if (selectedFoodItemList.size > 0) Color(0xFFFEDD00) else Color.Gray,
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "到手约¥12.4",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF06754),
            )
            Text(
                text = "预估全程送配送费¥12.4",
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
        Text(
            text = "去结算",
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(50))
                .background(color = Color(0xFFFEDD00))
                .clickable {
                    ToastUtil.showShort("去结算")
                }
                .padding(horizontal = 15.dp, vertical = 10.dp),
        )
    }
}

@Composable
fun ShangjiaContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F5)),
    ) {
        LazyColumn(
            modifier = Modifier,

            ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp,
                            ),
                        )
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box {
                        Image(
                            painter = painterResource(id = R.mipmap.takeout_banner),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(130.dp)
                                .height(90.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        )
                        Box(
                            modifier = Modifier
                                .width(130.dp)
                                .height(30.dp)
                                .align(Alignment.BottomStart)
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 10.dp,
                                        bottomEnd = 10.dp,
                                    ),
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Gray.copy(alpha = 1f),
                                        ),
                                        //                    startY = 0f,
                                        //                    endY = 100f
                                    ),
                                ),

                            ) {
                            Text(
                                text = "品牌故事",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier

                                    .padding(5.dp),
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                        )
                        Text(
                            text = "北京市大兴区礼贤镇敬贤家园中里A区一层112室",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 5.dp),
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .width(1.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Gray),

                            )
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Beenhere,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Column {
                            Text(
                                text = "服务设施",
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier,


                                )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.wrapContentHeight(),
                            ) {
                                Text(
                                    text = "到店自取、放心吃",
                                    color = Color.Black,
                                    fontSize = 13.sp,
                                    modifier = Modifier,

                                    )
                                Icon(
                                    imageVector = Icons.Outlined.HelpOutline,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .width(12.dp)
                                        .height(12.dp)
                                        .align(Alignment.CenterVertically),
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentHeight(),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .align(Alignment.CenterVertically),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "配送时间：09:30-22:00",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier,

                            )

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(16.dp),
                        )
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "商家档案",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier,

                        )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentHeight(),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Beenhere,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .align(Alignment.CenterVertically),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "商家经营牌照",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier,

                            )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp)
                                .align(Alignment.CenterVertically),
                        )


                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}