package com.lcz.wanandroid_compose.module.main.project.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.module.main.project.viewmodel.ProjectWidgetViewModel
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  9:36 2025/10/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectWidget(viewModel: ProjectWidgetViewModel = viewModel()) {
    val projectTitleList by viewModel.projectTitleList.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0) { projectTitleList.size }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.netGetProjectTitleList()
    }
    //顶部状态栏的滚动跟随行为。一共三种，自己挑
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        ) {

        it
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Red)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                if (projectTitleList.isNotEmpty()) {
                    ScrollableTabRow(
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 10.dp,
                        divider = {},
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        //                    indicator = { tabPositions ->
                        //                        TabRowDefaults.Indicator(
                        //                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        //                            color = MaterialTheme.colorScheme.onPrimary
                        //                        )
                        //                    }
                        indicator = { tabPositions ->
                            val currentTab = tabPositions[pagerState.currentPage]
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 5.dp)
                                    .tabIndicatorOffset(currentTab)
                                    .width(currentTab.width * 0.8f)  // 80% 宽度
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Red)

                            )
                        },
                    ) {
                        projectTitleList.forEachIndexed { index, s ->
                            Tab(
                                modifier = Modifier
                                    .height(52.dp)
                                    .padding(horizontal = 10.dp),
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },

                                ) {
                                Text(
                                    text = s.name,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

            }
            HorizontalPager(
                state = pagerState,
                key = { projectTitleList!![it].id }) { page ->
//                    ProjectChildPage(categoryId = projectTitleList!![page].id)
                val categoryId = projectTitleList!![page].id
                ProjectChildWidget(categoryId = categoryId)
            }
        }
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ProjectWidgetPreview() {
    ProjectWidget(ProjectWidgetViewModel(isPreview = true))
}
