package com.lcz.wanandroid_compose.module.search.page

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.search.viewmodel.SearchPageViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.util.ToastUtil
import com.lcz.wanandroid_compose.util.toHtml
import com.lcz.wanandroid_compose.widget.RefreshableList

/**
 * 作者:     刘传政
 * 创建时间:  10:27 2025/9/15
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(paramsBean: AppRoutePath.Search, viewModel: SearchPageViewModel = viewModel()) {

    val showResult by viewModel.showResult.collectAsState()

    LaunchedEffect(Unit) {
        LogUtil.d("ProjectChildPage", "触发刷新")
    }
    //顶部状态栏的滚动跟随行为。一共三种，自己挑
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,

                    ),
                title = {
                    TitleBar(viewModel)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {

            AnimatedVisibility(
                visible = !showResult,
                enter = fadeIn() + slideInVertically(
                    animationSpec = spring(
                        stiffness = 120f,// 降低刚度值使动画更慢
                        dampingRatio = 0.4f// 调整阻尼比（0.6 是临界阻尼，值越小反弹越多）,
                    ),
                    initialOffsetY = { fullHeight -> fullHeight }),

                exit = slideOutVertically(  // 新增垂直滑动退出
                    animationSpec = tween(1000),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut(),
            ) {
                Recommend(viewModel)
            }
            AnimatedVisibility(
                visible = showResult,
                enter = fadeIn() + expandIn(
                    animationSpec = tween(1000),
                    expandFrom = Alignment.TopCenter,
                    initialSize = {
                        IntSize(it.width, 0)
                    }
                ),

                exit = shrinkOut(
                    animationSpec = tween(1000),
                    shrinkTowards = Alignment.TopCenter, // 收缩方向保持顶部
                    targetSize = {
                        IntSize(it.width, 0)
                    }
                ) + fadeOut(),
            ) {
                SearchResult(viewModel)
            }
        }

    }
}

@Composable
fun SearchResult(viewModel: SearchPageViewModel) {
    val articleList by viewModel.articleList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()
    val inputText = viewModel.inputText.collectAsState()
    RefreshableList(
        items = articleList ?: emptyList(),
        isRefreshing = pageState.isRefreshing,
        isLoadingMore = pageState.isLoadingMore,
        allowRefresh = true,
        allowLoadMore = true,
        hasMore = pageState.hasMore,
        onRefresh = {
            viewModel.netSearch(true, inputText.value)
        },
        onLoadMore = {
            viewModel.netSearch(false, inputText.value)
        },
        itemContent = { index, item ->
            SearchItemView(index, item, inputText.value)
        }
    )

}

@Composable
fun SearchItemView(index: Int, item: Article, inputText: String) {
    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .shadow(
                elevation = 4.dp,          // 提升阴影高度
                shape = MaterialTheme.shapes.medium,
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
//            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                BorderStroke(1.dp, color = if (item.isTop) Color.Red else Color.Green),
                shape = MaterialTheme.shapes.medium
            )
            .clickable {

            }
            .padding(horizontal = 16.dp, vertical = 8.dp)


    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    item.author?.ifEmpty { item.shareUser }?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (item.type == 1) Text(
                        text = "置顶",
                        color = Color.Red,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(1.dp, Color.Red, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 4.dp)
                    )
                    if (item.fresh == true) Text(
                        text = "新",
                        color = Color.Magenta,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(1.dp, Color.Magenta, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp)
                    )


                }
                Text(
                    text = item.niceDate.toString(),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildHighlightText(item.title?.toHtml().toString(), inputText),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.superChapterName.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = if (item.collect == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (item.collect == true) Color.Red else Color.Gray
                )
            }
        }
    }
}


// 新增高亮文本构建函数
private fun buildHighlightText(source: String, keyword: String?): AnnotatedString {
    val cleanText = source.replace(Regex("<.*?>"), "") // 去除HTML标签

    return buildAnnotatedString {
        if (keyword.isNullOrEmpty()) {
            append(cleanText)
            return@buildAnnotatedString
        }

        val lowerSource = cleanText.lowercase()
        val lowerKeyword = keyword.lowercase()
        var startIndex = 0

        while (true) {
            val matchIndex = lowerSource.indexOf(lowerKeyword, startIndex)
            if (matchIndex == -1) break

            // 添加非匹配部分
            if (matchIndex > startIndex) {
                append(cleanText.substring(startIndex, matchIndex))
            }
            // 添加高亮部分
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(cleanText.substring(matchIndex, matchIndex + keyword.length))
            }

            startIndex = matchIndex + keyword.length
        }

        // 添加剩余部分
        if (startIndex < cleanText.length) {
            append(cleanText.substring(startIndex))
        }
    }
}

@Composable
fun Recommend(viewModel: SearchPageViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Recommend_hot(viewModel)
        Recommend_history(viewModel)

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Recommend_hot(viewModel: SearchPageViewModel) {
    val hotSearchList = viewModel.hotSearchList.collectAsState()
    LaunchedEffect(Unit) {
        if (hotSearchList.value.isNullOrEmpty()) {
            viewModel.netGetHotSearchList()
        }
    }

    Column {
        Text(text = "热门推荐")
        FlowRow {
            hotSearchList.value.forEach {
                val randomColor = remember() {  // 根据ID记忆颜色
                    Color.hsv(
                        hue = (0..360).random().toFloat(),
                        saturation = 0.8f, //饱和度
                        value = 0.9f   // 明度
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(randomColor)
                        .clickable {
                            viewModel.netSearch(true, it.name)
                        }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(text = it.name)
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun Recommend_history(viewModel: SearchPageViewModel) {
    val searchHistoryList = viewModel.searchHistoryList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.spGetSearchHistory()
    }
    Column {
        Text(text = "搜索历史")
        FlowRow {
            searchHistoryList.value.forEach {
                val randomColor = remember() {  // 根据ID记忆颜色
                    Color.hsv(
                        hue = (0..360).random().toFloat(),
                        saturation = 0.8f, //饱和度
                        value = 0.9f   // 明度
                    )
                }
                Box(
                    modifier = Modifier
                        .widthIn(min = 80.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(CutCornerShape(25))
                        .background(randomColor)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, CutCornerShape(25))
                        .combinedClickable(onClick = {
                            viewModel.netSearch(true, it)
                        }, onLongClick = {
                            ToastUtil.showShort("删除逻辑，不实现了")
                        })
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
fun TitleBar(viewModel: SearchPageViewModel) {
    val roundedCornerShape = RoundedCornerShape(12.dp)
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta)
        )
    }
    val inputText = viewModel.inputText.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = null, modifier = Modifier.clickable {
            if (showResult) {
                viewModel.updateShowResult(false)
            } else {
                globalNavController?.popBackStack()
            }
        })

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp, vertical = 5.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clip(roundedCornerShape)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta)
                    ),
                    shape = roundedCornerShape
                )
                .padding(horizontal = 10.dp, vertical = 0.dp)
//                .height(55.dp)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
            TextField(
                textStyle = TextStyle(brush, fontSize = 15.sp, lineHeight = 15.sp), // 添加行高设置
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Red,
                    focusedIndicatorColor = Color.Transparent, // 隐藏下划线
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                value = inputText.value,
                singleLine = true,
                onValueChange = {
                    viewModel.inputTextChange(it)
                },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.netSearch(true, inputText.value)
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                placeholder = {
                    Text(
                        text = "请输入搜索内容",
                        fontSize = 14.sp,
                        lineHeight = 14.sp, // 添加行高设置
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                },
                modifier = Modifier
                    .padding(0.dp)
                    .weight(1f)
            )
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    viewModel.netSearch(true, inputText.value)
                }) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                ) {
                    Text(text = "搜索", fontSize = 15.sp)
                }
            }
        }

    }
}

@Preview
@Composable
fun ItemViewPreview() {
    WanAndroid_composeTheme {
        SearchItemView(
            1, Article(
                title = "这是标题",
                shareUser = "这是分享人",
                niceDate = "这是日期",
                superChapterName = "这是分类",
                collect = true,
                fresh = true,
                type = 1
            ), "这是"
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ItemViewPreview_Dark() {
    WanAndroid_composeTheme(darkTheme = true) {
        SearchItemView(
            1, Article(
                title = "这是标题",
                shareUser = "这是分享人",
                niceDate = "这是日期",
                superChapterName = "这是分类",
                collect = true,
                fresh = true,
                type = 1
            ), "这是"
        )
    }
}
/*
* 预览限制
由于预览在 Android Studio 中呈现的方式，它们非常轻巧，不需要整个 Android 框架来呈现。不过，这种方法存在以下限制：

无法访问网络
无法访问文件
有些 Context API 不一定完全可用
*
* 预览和 ViewModels
在可组合项中使用 ViewModel 时，预览功能会受到限制。预览系统无法构建传递给 ViewModel 的所有参数，例如代码库、使用情形、管理器或类似参数。此外，如果您的 ViewModel 参与依赖项注入（例如使用 Hilt），预览系统无法构建整个依赖项图来构造 ViewModel。

当您尝试使用 ViewModel 预览可组合项时，Android Studio 在渲染特定可组合项时会显示错误：
* */
@SuppressLint("ViewModelConstructorInComposable")
@PreviewLightDark()
//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchPagePreviewPreview() {
    WanAndroid_composeTheme {
        SearchPage(AppRoutePath.Search(), viewModel = SearchPageViewModel(true))
    }
}


