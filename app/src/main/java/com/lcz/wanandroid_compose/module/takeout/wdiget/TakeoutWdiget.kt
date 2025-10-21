package com.lcz.wanandroid_compose.module.takeout.wdiget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import com.google.common.math.LinearTransformation.horizontal
import com.lcz.wanandroid_compose.R
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.widget.CoilImage
import kotlinx.coroutines.delay

/**
 * 作者:     刘传政
 * 创建时间:  10:57 2025/10/20
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述: ui仿照美团外卖的点餐页
 * 所有数据都是本地模拟的
 */
@Composable
fun TakeoutWdiget() {
    Scaffold() {
        it
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        ) {

            HeaderCard()
            TitileBar()
        }

    }
}

@Composable
fun HeaderCard() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF80262F),
                            Color(0xFF80262F).copy(alpha = 1f),
                            Color.White
                        ),
                        //                    startY = 0f,
                        //                    endY = 100f
                    )
                )
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 40.dp)
        ) {
            Image(

                painter = painterResource(id = R.mipmap.takeout_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)

                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))


            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        //店铺信息
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Image(

                painter = painterResource(id = R.mipmap.takeout_head_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp))


            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "蜜雪冰城（礼贤店）",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text("评分", color = Color.Gray, fontSize = 12.sp)
                        Text("4.5分", color = Color.Black, fontSize = 12.sp)
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text("月售", color = Color.Gray, fontSize = 12.sp)
                        Text("3000+", color = Color.Black, fontSize = 12.sp)
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text("美团全城送", color = Color.Gray, fontSize = 12.sp)
                        Text("约63分钟", color = Color.Black, fontSize = 12.sp)
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text("大兴区/奶茶", maxLines = 1, color = Color.Gray, fontSize = 12.sp)
                        Text("复购榜第1名", maxLines = 1, color = Color.Black, fontSize = 12.sp)
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
                    2.dp, Color.Yellow,
                    RoundedCornerShape(50)
                )
                .padding(4.dp)
        ) {
            val peisongfangshi = remember { mutableStateOf(0) } //0: 外送 1: 自取
            // 外送按钮背景动画
            val waisongColor by animateColorAsState(
                targetValue = if (peisongfangshi.value == 0) Color.Yellow else Color.White,
                label = "waisongColor",
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing
                )
            )
            // 自取按钮背景动画
            val ziquColor by animateColorAsState(
                targetValue = if (peisongfangshi.value == 1) Color.Yellow else Color.White,
                label = "ziquColor",
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing
                )
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
                            waisongColor
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { peisongfangshi.value = 0 }
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)// 垂直居中


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
                            ziquColor
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { peisongfangshi.value = 1 }
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)// 垂直居中

                )
            }


        }
        Spacer(modifier = Modifier.height(10.dp))
        val density = LocalDensity.current
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(25.dp)
        ) {
            item {
                CouponBorderBox(
                    dividerX = with(density) {
                        92.dp.toPx()
                    },
                    modifier = Modifier
                        .height(25.dp)
                        .width(120.dp)
                ) {
                    // 这里放优惠券内容
                    Row(
                        modifier = Modifier.fillMaxHeight(),

                        verticalAlignment = Alignment.CenterVertically
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
                                        .offset(y = 1.dp)
                                )
                                Text(

                                    "满25可用",
                                    color = Color(0xFFF64C36),
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .wrapContentHeight()
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
                                            bottomStart = 0.dp
                                        )
                                    )
                                    .background(Color(0xFFF6DDD9))
                                    .fillMaxHeight()

                                    .wrapContentHeight(Alignment.CenterVertically)
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
                                .padding(end = 5.dp)
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
                            1.dp, Color(0xFFF64C36),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Text(

                        "50减1",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
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
                            1.dp, Color(0xFFF64C36),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp)
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
                                    moveTo(size.width, 0f)
                                    lineTo(size.width, size.height)
                                }
                                drawPath(
                                    path = dashPath,
                                    color = Color(0xFFF64C36),
                                    style = Stroke(
                                        width = 1.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                                    )
                                )
                            }
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                    )
                    Text(

                        "返2元券",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .padding(start = 5.dp)
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
                            1.dp, Color(0xFFF64C36),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Text(

                        "7折起",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
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
                            1.dp, Color(0xFFF64C36),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Text(

                        "返1元券",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
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
                            1.dp, Color(0xFFF64C36),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Text(

                        "新客减1",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFF64C36),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                    )
                }
            }

        }

        Text(
            fontSize = 8.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing.fractionOfContainer(1f / 5f)),
            text = "亲亲看这里哦！如果收到餐品有任何漏放吸管勺子、餐品错误或破损漏撒等问题不要着急不要生气，请立刻拨打门店电话给我们，我们将竭诚为您服务！感谢您的支持！比心心~！"
        )

    }


}


@Composable
fun TitileBar() {
    Column {
        //状态栏高度
        Box(
            modifier = Modifier
                .background(Color(0xFF80262F))
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFF80262F))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "返回",
                tint = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "搜索",
                tint = Color.White
            )
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Icon(
                imageVector = Icons.Filled.StarOutline,
                contentDescription = "收藏",
                tint = Color.White
            )
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Box(
                modifier = Modifier


                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF682125))
                    .border(
                        1.dp, Color.White,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 2.dp)

            ) {
                Text("预订", color = Color.White)
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF682125))
                    .border(
                        1.dp, Color.White,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 2.dp)

            ) {
                Text("拼单", color = Color.White)
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "更多",
                tint = Color.White
            )
        }
    }
}

@Preview()
@Composable
fun TakeoutWdigetPreview() {
    TakeoutWdiget()
}