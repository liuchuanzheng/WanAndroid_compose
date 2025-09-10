package com.lcz.wanandroid_compose.module.login

import android.R.attr.password
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lcz.wanandroid_compose.navigation.globalNavController

/**
 * 作者:     刘传政
 * 创建时间:  17:24 2025/9/10
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage() {
    // 添加输入状态
    val username = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "登录") }, navigationIcon = {
                    IconButton(onClick = {
                        globalNavController?.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    username.value = it
                },
                label = { Text(text = "账号") },
                singleLine = true,
                isError = username.value.isEmpty(), // 非空校验
                supportingText = {
                    if (username.value.length < 6) {
                        Text("密码至少6位", color = Color.Red)
                    }
                }
            )

        }
    }

}