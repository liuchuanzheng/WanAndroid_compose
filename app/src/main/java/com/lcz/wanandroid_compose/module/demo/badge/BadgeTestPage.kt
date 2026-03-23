package com.lcz.wanandroid_compose.module.demo.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.globalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeTestPage(paramsBean: AppRoutePath.BadgeTest) {
    // 状态管理：控制Badge的显示与隐藏
    var showBadge by remember { mutableStateOf(true) }
    // 状态管理：控制Badge的数值
    var badgeNumber by remember { mutableIntStateOf(5) }
    // 状态管理：控制Badge的可见性（是否显示数字）
    var showBadgeContent by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("官方Badge组件测试") },
                navigationIcon = {
                    IconButton(onClick = { globalNavController?.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 样式测试：图标上的Badge
                    BadgedBox(
                        badge = {
                            if (showBadge) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                ) {
                                    if (showBadgeContent && badgeNumber > 0) {
                                        Text(badgeNumber.toString(), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { /* 点击事件 */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "通知")
                        }
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
            // ================= 1. 基础Badge样式测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("基础Badge样式测试", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 默认样式Badge
                    BadgedBox(
                        badge = { Badge { Text("9") } }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("默认样式")
                        }
                    }

                    // 自定义颜色Badge
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ) {
                                Text("99+")
                            }
                        }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("自定义颜色")
                        }
                    }

                    // 无内容Badge（小红点）
                    BadgedBox(
                        badge = { Badge(containerColor = MaterialTheme.colorScheme.error) }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("小红点")
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // ================= 2. 自定义形状Badge测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("自定义形状Badge测试", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 圆形Badge
                    BadgedBox(
                        badge = {
                            Badge(
//                                shape = CircleShape,
                                containerColor = Color(0xFFFF6B6B),
                                contentColor = Color.White
                            ) {
                                Text("1", fontSize = 12.sp)
                            }
                        }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("圆形")
                        }
                    }

                    // 圆角矩形Badge
                    BadgedBox(
                        badge = {
                            Badge(
//                                shape = RoundedCornerShape(8.dp),
                                containerColor = Color(0xFF4ECDC4),
                                contentColor = Color.White
                            ) {
                                Text("24", fontSize = 12.sp)
                            }
                        }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("圆角矩形")
                        }
                    }

                    // 大尺寸Badge
                    BadgedBox(
                        badge = {
                            Badge(
                                modifier = Modifier.size(32.dp),
                                containerColor = Color(0xFF95E1D3),
                                contentColor = Color.Black
                            ) {
                                Text("NEW", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Button(onClick = { /* 点击事件 */ }) {
                            Text("大尺寸")
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // ================= 3. 事件测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("事件测试", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 动态控制Badge显示/隐藏
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Badge显示状态: ${if (showBadge) "显示" else "隐藏"}")
                        Switch(checked = showBadge, onCheckedChange = { showBadge = it })
                    }

                    // 动态控制Badge内容显示/隐藏
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Badge内容显示: ${if (showBadgeContent) "显示" else "隐藏"}")
                        Switch(checked = showBadgeContent, onCheckedChange = { showBadgeContent = it })
                    }

                    // 动态修改Badge数值
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("当前Badge数值: $badgeNumber")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { if (badgeNumber > 0) badgeNumber-- }) {
                                Text("减少")
                            }
                            Button(onClick = { badgeNumber++ }) {
                                Text("增加")
                            }
                            Button(onClick = { badgeNumber = 0 }) {
                                Text("重置")
                            }
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // ================= 4. 高级用法测试 =================
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("高级用法测试", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 文本上的Badge
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White
                            ) {
                                Text("3")
                            }
                        }
                    ) {
                        Text("消息", fontSize = 18.sp)
                    }

                    // 图标按钮上的Badge
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White
                            )
                        }
                    ) {
                        IconButton(onClick = { /* 点击事件 */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "通知")
                        }
                    }

                    // 自定义布局中的Badge
                    BadgedBox(
                        badge = {
                            Badge(
//                                shape = RoundedCornerShape(4.dp),
                                containerColor = Color(0xFFFF6B6B),
                                contentColor = Color.White
                            ) {
                                Text("HOT", fontSize = 10.sp)
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("商品", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}