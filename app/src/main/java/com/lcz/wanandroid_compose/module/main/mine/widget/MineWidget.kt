package com.lcz.wanandroid_compose.module.main.mine.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToLogin
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.widget.CoilImage

/**
 * 作者:     刘传政
 * 创建时间:  16:08 2025/9/10
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun MineWidget() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                model = "https://img1.baidu.com/it/u=1221952588,3009131272&fm=253&app=138&f=JPEG?w=500&h=500",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(70.dp)
                    .border(1.dp, Color.Yellow, CircleShape)
                    .clip(CircleShape)


            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable {
                        globalNavController?.app_navigateToLogin(AppRoutePath.Login())
                    }
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "未登录",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = "点击登录账号", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}