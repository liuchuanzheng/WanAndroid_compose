package com.lcz.wanandroid_compose.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  16:21 2025/8/27
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    images: List<Any>,
    autoScroll: Boolean = true,
    autoScrollInterval: Long = 3000L,
    indicatorModifier: Modifier = Modifier,
    indicatorSize: Int = 6,
    pagerState: PagerState? = null,
    onBannerItemClick: ((index: Int) -> Unit)? = null
) {
    var loopCount = Int.MAX_VALUE
    var startIndex = loopCount / 2
    val curPagerState = pagerState ?: rememberPagerState(initialPage = startIndex) { loopCount }

    /** 计算实际索引 */
    fun pageMapper(index: Int): Int {
        return (index - startIndex).floorMod(images.count())
    }

    var currentIndex = remember {
        derivedStateOf {
            pageMapper(curPagerState.currentPage)
        }
    }
    val scope = rememberCoroutineScope()  // 添加协程作用域
    LaunchedEffect(true) {
        while (autoScroll && images.isNotEmpty()) {
            delay(autoScrollInterval)
            // 手指滑动时不轮播
            if (!curPagerState.isScrollInProgress) {
                curPagerState.animateScrollToPage(pageMapper(currentIndex.value + 1))
            }
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        HorizontalPager(state = curPagerState) {
            val index = it % images.size // 取模运算获取实际的索引
            CoilImage(
                model = images[index],
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onBannerItemClick != null) {
                        onBannerItemClick?.invoke(index)
                    })
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = indicatorModifier
        ) {
            images.forEachIndexed { index, _ ->
                Box(modifier = Modifier.clickable {
                    val targetPage = pageMapper(startIndex + index)

                    scope.launch {
                        curPagerState.animateScrollToPage(targetPage)
                    }
                }) {
                    PagerIndicator(
                        selected = currentIndex.value == index,
                        indicatorSize = indicatorSize,
                    )
                }
                if (index != images.size - 1) {
                    Spacer(modifier = Modifier.width(4.dp)) // 间隔
                }

            }
        }


    }

}

@Composable
fun PagerIndicator(selected: Boolean = false, indicatorSize: Int = 6) {
    var color = if (selected) Color.Red else Color.Gray
    val size = if (selected) (indicatorSize + 3).dp else indicatorSize.dp
    Box(
        modifier = Modifier
            .size(size)
            .background(color, shape = RoundedCornerShape(20))
    )
}

/** 计算除法后取余 */
fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}