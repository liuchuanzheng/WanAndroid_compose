package com.lcz.wanandroid_compose.module.takeout.wdiget

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Beenhere
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.R
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.widget.Banner
import com.lcz.wanandroid_compose.widget.CoilImage
import com.lcz.wanandroid_compose.widget.RatingBar
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  10:57 2025/10/20
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述: ui仿照美团外卖的点餐页
 * 所有数据都是本地模拟的
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeoutWdiget() {
    Scaffold(
    ) {
        it
        var headerHeight by remember { mutableStateOf(0) }
        var titileBarHeight by remember { mutableStateOf(0) }
        var currentOffsetY by remember { mutableStateOf(0f) }//顶部偏移距离
        val scrollProgress = remember(currentOffsetY) {
            // 计算滚动进度（0-1范围）
            val progress = if (headerHeight > 0) {
                (-currentOffsetY / headerHeight / 3).coerceIn(0f, 1f)
            } else 0f
            progress
        }
        val density = LocalDensity.current
        val parentConnection = remember {
            object : NestedScrollConnection {
                // 处理 Pre-scroll (子组件滑动前)
                // 直接调用父的实现，父不优先消耗
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    //在这里我只处理手指从下往上滑动，而不处理从上往下滑动。从上往下滑动在onPostScroll中处理，因为要检测滑到头的情况。
                    //总体gu是手指从下往上滑动优先顶部，而从上往下滑动优先底部
                    LogUtil.i(
                        tag = "TakeoutWdiget",
                        msg = "父组件获取到 onPreScroll available: $available source: $source",
                    )
                    var originOffsetY = currentOffsetY
                    var dh = 0f
                    if (currentOffsetY > -headerHeight && currentOffsetY < 0f) {
                        //还没完全收起，全部消费掉

                        if (originOffsetY + available.y >= -headerHeight.toFloat()) {
                            //当前滑动全部消费掉，头部也没完全隐藏
                            dh = available.y
                        } else {
                            //消费此次滑动的部分距离，就会让头部完全隐藏
                            dh = available.y + (originOffsetY - (-headerHeight.toFloat()))
                        }
                    } else {
                        //已经完全收起了
                        dh = 0f
                    }



                    if (source == NestedScrollSource.UserInput || source == NestedScrollSource.SideEffect) {
                        //滑动或者惯性滑动
                        if (available.y < 0) { //手指从下往上滑动
                            currentOffsetY = (currentOffsetY + available.y).coerceIn((-headerHeight.toFloat()), 0f)
                            return Offset(available.x, dh)
                        } else { //手指从上往下滑动

                        }
//                        return Offset(available.x, dh)
                    }

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
                    LogUtil.e(
                        tag = "TakeoutWdiget",
                        msg = "父组件获取到 onPostScroll consumed: $consumed, available: $available",
                    )
                    if (source == NestedScrollSource.UserInput || source == NestedScrollSource.SideEffect) {
                        //滑动或者惯性滑动
                        if (available.y < 0) { //手指从下往上滑动

                        } else { //手指从上往下滑动
                            if (consumed.y == 0f) {
                                //说明子组件没消费滑动，也就是滑到头了
                                currentOffsetY = (currentOffsetY + available.y).coerceIn((-headerHeight.toFloat()), 0f)

                            }
                        }

                    }
//                    return Offset(x = 0f, (available.y))
                    return super.onPostScroll(consumed, available, source)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .nestedScroll(parentConnection),
        ) {
            Column(
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                DiancanCard(
                    modifier = Modifier
                        .padding(
                            top = with(density) {
                                (headerHeight + currentOffsetY).coerceIn(
                                    minimumValue = titileBarHeight.toFloat(),
                                    maximumValue = headerHeight.toFloat(),
                                ).toDp()
                            },
                        )
                        .fillMaxWidth()
                        .fillMaxSize(),
                )
            }

            Box(
            ) {

                // 添加高度测量修饰符
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            headerHeight = coordinates.size.height
                        }
                        .offset(
                            0.dp,
                            with(density) {
                                currentOffsetY.toDp()
                            },
                        ),
                ) {
                    HeaderCard()
                }
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            titileBarHeight = coordinates.size.height
                        },
                ) {
                    TitileBar(currentOffsetY, headerHeight)
                }

            }


        }


    }
}

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
    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(color = Color.White),
    ) {
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
                                ) {
                                    selectedIndex = it
                                }
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
    }
}

@Composable
fun FoodItemWidget(bean: FoodItem, index: Int, totalSize: Int, selectedIndex: Int, onClick: (index: Int) -> Unit) {
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


@Composable
fun HeaderCard() {

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
            .padding(bottom = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF80262F),
                            Color(0xFF80262F).copy(alpha = 1f),
                            Color.White,
                        ),
                        //                    startY = 0f,
                        //                    endY = 100f
                    ),
                )
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 40.dp),
        ) {
            Image(

                painter = painterResource(id = R.mipmap.takeout_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)

                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),


                )

        }
        Spacer(modifier = Modifier.height(10.dp))
        //店铺信息
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
        ) {
            Image(

                painter = painterResource(id = R.mipmap.takeout_head_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp)),


                )
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    text = "蜜雪冰城（礼贤店）",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.padding(end = 10.dp),
                    ) {
                        Text(
                            "评分",
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                        Text(
                            "4.5分",
                            color = Color.Black,
                            fontSize = 12.sp,
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray,
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp),
                    ) {
                        Text(
                            "月售",
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                        Text(
                            "3000+",
                            color = Color.Black,
                            fontSize = 12.sp,
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray,
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp),
                    ) {
                        Text(
                            "美团全城送",
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                        Text(
                            "约63分钟",
                            color = Color.Black,
                            fontSize = 12.sp,
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray,
                    )
                    Column(
                        modifier = Modifier.padding(start = 10.dp),
                    ) {
                        Text(
                            "大兴区/奶茶",
                            maxLines = 1,
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                        Text(
                            "复购榜第1名",
                            maxLines = 1,
                            color = Color.Black,
                            fontSize = 12.sp,
                        )
                    }


                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    2.dp,
                    Color.Yellow,
                    RoundedCornerShape(50),
                )
                .padding(4.dp),
        ) {
            val peisongfangshi = remember { mutableStateOf(0) } //0: 外送 1: 自取
            // 外送按钮背景动画
            val waisongColor by animateColorAsState(
                targetValue = if (peisongfangshi.value == 0) Color.Yellow else Color.White,
                label = "waisongColor",
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing,
                ),
            )
            // 自取按钮背景动画
            val ziquColor by animateColorAsState(
                targetValue = if (peisongfangshi.value == 1) Color.Yellow else Color.White,
                label = "ziquColor",
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing,
                ),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "外送",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier


                        .weight(1f)
                        .padding(horizontal = 1.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            waisongColor,
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { peisongfangshi.value = 0 }
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically),// 垂直居中


                )
                Text(
                    text = "自取",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier

                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            ziquColor,
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { peisongfangshi.value = 1 }
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically),// 垂直居中

                )
            }


        }
        Spacer(modifier = Modifier.height(10.dp))
        val density = LocalDensity.current
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(25.dp),
        ) {
            item {
                CouponBorderBox(
                    dividerX = with(density) {
                        92.dp.toPx()
                    },
                    modifier = Modifier
                        .height(25.dp)
                        .width(120.dp),
                ) {
                    // 这里放优惠券内容
                    Row(
                        modifier = Modifier.fillMaxHeight(),

                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box {
                            Row(modifier = Modifier.padding(start = 33.dp)) {
                                Text(
                                    "¥6",
                                    color = Color(0xFFF64C36),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .wrapContentHeight()
                                        .offset(y = 1.dp),
                                )
                                Text(

                                    "满25可用",
                                    color = Color(0xFFF64C36),
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .wrapContentHeight(),
                                )
                            }

                            Text(
                                "神券",
                                color = Color(0xFFF64C36),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W900,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic, // 斜体设置
                                modifier = Modifier
                                    .width(30.dp)

                                    // 修改这里：只保留右下角圆角
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 0.dp,
                                            bottomEnd = 15.dp,
                                            bottomStart = 0.dp,
                                        ),
                                    )
                                    .background(Color(0xFFF6DDD9))
                                    .fillMaxHeight()

                                    .wrapContentHeight(Alignment.CenterVertically),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(

                            "已领",
                            color = Color(0xFFF64C36),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight()
                                .padding(end = 5.dp),
                        )
                    }

                }
            }
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            Color(0xFFF64C36),
                            RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 5.dp),
                ) {
                    Text(

                        "50减1",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(),
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            Color(0xFFF64C36),
                            RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 5.dp),
                ) {
                    Text(

                        "满3单",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier

                            .fillMaxHeight()


                            .drawBehind {
                                // 绘制虚线
                                val dashPath = Path().apply {
                                    moveTo(
                                        size.width,
                                        0f,
                                    )
                                    lineTo(
                                        size.width,
                                        size.height,
                                    )
                                }
                                drawPath(
                                    path = dashPath,
                                    color = Color(0xFFF64C36),
                                    style = Stroke(
                                        width = 1.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                10f,
                                                5f,
                                            ),
                                            0f,
                                        ),
                                    ),
                                )
                            }
                            .wrapContentHeight()
                            .padding(end = 5.dp),
                    )
                    Text(

                        "返2元券",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .padding(start = 5.dp),
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            Color(0xFFF64C36),
                            RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 5.dp),
                ) {
                    Text(

                        "7折起",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(),
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            Color(0xFFF64C36),
                            RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 5.dp),
                ) {
                    Text(

                        "返1元券",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(),
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            Color(0xFFF64C36),
                            RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 5.dp),
                ) {
                    Text(

                        "新客减1",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(),
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            fontSize = 8.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing.fractionOfContainer(1f / 5f),
                ),
            text = "亲亲看这里哦！如果收到餐品有任何漏放吸管勺子、餐品错误或破损漏撒等问题不要着急不要生气，请立刻拨打门店电话给我们，我们将竭诚为您服务！感谢您的支持！比心心~！",
        )

    }


}


@Composable
fun TitileBar(currentOffsetY: Float, headerHeight: Int) {
    var backgroundColor by remember { mutableStateOf(Color(0xFF80262F)) }
    var isWhite by remember { mutableStateOf(false) }
    // 添加滚动进度计算
    var progress = 0f

    if (headerHeight > 0) {
        if (-currentOffsetY <= headerHeight / 4) {
            //先渐渐半透明
            progress = (-currentOffsetY / headerHeight / 4).coerceIn(0f, 1f)
            backgroundColor = Color(0xFF80262F).copy(alpha = 1 - progress)
            isWhite = false
        } else {
            //再渐渐变白
            backgroundColor = Color.White.copy(alpha = 1f)
            isWhite = true
        }

    }
    // 背景色动画过渡
    val backgroundColorAnim by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 200),
    )
    Column {
        //状态栏高度
        Box(
            modifier = Modifier
                .background(backgroundColorAnim)
                .fillMaxWidth()
                .statusBarsPadding(),
        ) {
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(backgroundColorAnim)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "返回",
                tint = if (isWhite) Color.Black else Color.White,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "搜索",
                tint = if (isWhite) Color.Black else Color.White,
            )
            Spacer(
                modifier = Modifier.width(10.dp),
            )
            Icon(
                imageVector = Icons.Filled.StarOutline,
                contentDescription = "收藏",
                tint = if (isWhite) Color.Black else Color.White,
            )
            Spacer(
                modifier = Modifier.width(10.dp),
            )
            Box(
                modifier = Modifier


                    .clip(RoundedCornerShape(50))
                    .background(if (isWhite) Color.White else Color(0xFF682125))
                    .border(
                        1.dp,
                        if (isWhite) Color.Black else Color.White,
                        RoundedCornerShape(50),
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 2.dp,
                    ),

                ) {
                Text(
                    "预订",
                    color = if (isWhite) Color.Black else Color.White,
                )
            }
            Spacer(
                modifier = Modifier.width(10.dp),
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isWhite) Color.White else Color(0xFF682125))
                    .border(
                        1.dp,
                        if (isWhite) Color.Black else Color.White,
                        RoundedCornerShape(50),
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 2.dp,
                    ),

                ) {
                Text(
                    "拼单",
                    color = if (isWhite) Color.Black else Color.White,
                )
            }
            Spacer(
                modifier = Modifier.width(10.dp),
            )
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "更多",
                tint = if (isWhite) Color.Black else Color.White,
            )
        }
    }
}

@Preview()
@Composable
fun TakeoutWdigetPreview() {
    TakeoutWdiget()
}