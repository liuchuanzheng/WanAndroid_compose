package com.lcz.wanandroid_compose.module.main.mine.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.twotone.ArrowForwardIos
import androidx.compose.material.icons.twotone.Chat
import androidx.compose.material.icons.twotone.ChatBubbleOutline
import androidx.compose.material.icons.twotone.ControlCamera
import androidx.compose.material.icons.twotone.DeveloperMode
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.SlowMotionVideo
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lcz.wanandroid_compose.MyApp
import com.lcz.wanandroid_compose.MyAppViewModel
import com.lcz.wanandroid_compose.R
import com.lcz.wanandroid_compose.module.login.LoginPage
import com.lcz.wanandroid_compose.module.login.LoginViewModel
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.module.main.mine.viewmodel.MineWidgetViewModel
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToLogin
import com.lcz.wanandroid_compose.navigation.app_navigateToMyCoinHistory
import com.lcz.wanandroid_compose.navigation.app_navigateToSetting
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
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
    // 添加 ViewModel 实例
    val viewModel: MineWidgetViewModel = viewModel()
    val coinCount = viewModel.coinCount.collectAsState()
    val user = MyApp.myAppViewModel.user.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.netGetMyCoin()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.value != null) {
                CoilImage(
                    model = user.value?.icon,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(70.dp)
                        .border(1.dp, Color.Yellow, CircleShape)
                        .clip(CircleShape)
                )


            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .border(1.dp, Color.Yellow, CircleShape)
                        .clip(CircleShape)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable {
                        if (user.value == null) {
                            globalNavController?.app_navigateToLogin(AppRoutePath.Login())
                        } else {
                        }
                    }
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = user.value?.nickname ?: "未登录",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (user.value == null) {
                    Text(text = "点击登录账号", fontSize = 14.sp, color = Color.Gray)
                } else {
                    Text(text = "id：${user.value?.id ?: "-"}", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .clickable {
                        globalNavController?.app_navigateToMyCoinHistory(AppRoutePath.MyCoinHistory())
                    }

            ) {
                Icon(imageVector = Icons.TwoTone.ControlCamera, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "积分", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${coinCount.value}", fontSize = 14.sp, fontWeight = FontWeight.W400)
                Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 5.dp)


            ) {
                Icon(imageVector = Icons.TwoTone.ChatBubbleOutline, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "客服", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 5.dp)


            ) {
                Icon(imageVector = Icons.TwoTone.SlowMotionVideo, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "抖音", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 5.dp)


            ) {
                Icon(imageVector = Icons.TwoTone.DeveloperMode, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "demo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null)
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .clickable {
                        globalNavController?.app_navigateToSetting(AppRoutePath.Setting())
                    }


            ) {
                Icon(imageVector = Icons.TwoTone.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "设置", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null)
            }
        }

    }
}

//这里预览失败。需要研究下带有viewmodel的preview如何加载。
@Preview
@Composable
private fun MineWidgetPreview() {
    MyApp.myAppViewModel = viewModel<MyAppViewModel>()
    // 添加预览用的模拟数据
    val user = LoginResponseBean().apply {
        id = 123
        nickname = "预览用户"
        icon = ""
    }
    MyApp.myAppViewModel.updateUser(user)
    WanAndroid_composeTheme {
        MineWidget()
    }
}
