package com.lcz.wanandroid_compose.module.demo.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingTestPage(paramsBean: AppRoutePath.LoadingTest) {
    // 状态管理：控制不确定进度条的显示与隐藏
    var isLoading by remember { mutableStateOf(false) }
    // 状态管理：控制确定进度条的具体进度数值（0.0f - 1.0f）
    var progress by remember { mutableFloatStateOf(0.1f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("官方Loading组件测试") },
                navigationIcon = {
                    IconButton(onClick = { globalNavController?.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ================= 1. 不确定进度 (Indeterminate) 测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("不确定进度条 (Indeterminate)")
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, // 样式测试：颜色
                        strokeWidth = 4.dp // 样式测试：线条宽度
                    )
                } else {
                    Text("未加载状态", color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("不确定线性进度条")
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant // 样式测试：轨道底色
                    )
                } else {
                    Text("未加载状态", color = Color.Gray)
                }
            }

            // 事件测试：通过按钮控制显示/隐藏加载状态
            Button(onClick = { isLoading = !isLoading }) {
                Text(if (isLoading) "停止加载" else "开始加载")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // ================= 2. 确定进度 (Determinate) 测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("确定进度条 (Determinate): ${(progress * 100).toInt()}%")
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(64.dp), // 样式测试：修改尺寸
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("确定线性进度条: ${(progress * 100).toInt()}%")
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp), // 样式测试：修改高度
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            // 事件测试：通过按钮动态修改进度数值
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { if (progress >= 0.1f) progress -= 0.1f }) {
                    Text("减少进度")
                }
                Button(onClick = { if (progress <= 0.9f) progress += 0.1f }) {
                    Text("增加进度")
                }
            }
        }
    }
}