package com.lcz.wanandroid_compose.module.main.project.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.main.project.viewmodel.PageViewModelFactory
import com.lcz.wanandroid_compose.module.main.project.viewmodel.ProjectChildWidgetViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.PageJumpManager
import com.lcz.wanandroid_compose.widget.CoilImage
import com.lcz.wanandroid_compose.widget.RefreshableList

/**
 * 作者:     刘传政
 * 创建时间:  10:52 2025/10/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */

@Composable
fun ProjectChildWidget(
    categoryId: Int,
    viewModel: ProjectChildWidgetViewModel = viewModel(key = "$categoryId", factory = PageViewModelFactory(categoryId)),
) {

    val projectList by viewModel.projectList.collectAsState()
    val pageState by viewModel.pageState.collectAsState()
    LaunchedEffect(Unit) {
        if (projectList.isEmpty()) {
            viewModel.netGetProjectPageList(true)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RefreshableList(
            items = projectList,
            isRefreshing = pageState.isRefreshing,
            isLoadingMore = pageState.isLoadingMore,
            allowRefresh = true,
            allowLoadMore = true,
            hasMore = pageState.hasMore,
            onRefresh = {
                viewModel.netGetProjectPageList(true)
            },
            onLoadMore = {
                viewModel.netGetProjectPageList(false)
            },
            itemContent = { index, item ->
                ProjectArticleItem(article = item) {
                    PageJumpManager.navigateToWeb(AppRoutePath.Web(url = item.link ?: ""))
                }
            }
        )
    }
}

@Composable
fun ProjectArticleItem(
    article: Article,
    onArticleClick: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .clickable { onArticleClick.invoke(article) },
        shape = RoundedCornerShape(8.dp), // 添加圆角形状
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (article.author?.isEmpty() == true) article.shareUser
                        .toString() else article.author.toString(),
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
                Text(
                    text = article.niceDate ?: "",
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                CoilImage(
                    model = article.envelopePic,
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)

                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = Color.Green,
                            shape = RoundedCornerShape(8.dp)  // 添加圆角形状参数
                        ),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = article.title ?: "",
                        color = LocalContentColor.current.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = article.desc ?: "",
                        modifier = Modifier.padding(top = 12.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${article.superChapterName}·${article.chapterName}",
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
                Icon(
                    imageVector = if (article.collect == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.clickable {
                    }
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
//    showSystemUi = true
)
@Composable
fun ProjectArticleItemPreview() {
    ProjectChildWidget(categoryId = 100, viewModel = ProjectChildWidgetViewModel(isPreview = true, categoryId = 100))
}
