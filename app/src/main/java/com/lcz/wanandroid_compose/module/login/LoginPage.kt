package com.lcz.wanandroid_compose.module.login

import android.R.attr.label
import android.R.attr.password
import android.R.attr.singleLine
import android.R.attr.value
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.twotone.Visibility
import androidx.compose.material.icons.twotone.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme

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
    val password = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }
    val usernameHasInteracted = remember { mutableStateOf(false) }
    val passwordHasInteracted = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },// 禁用水波纹效果
                    indication = null // 禁用水波纹效果
                ) {
                    focusManager.clearFocus()
                }
                .padding(horizontal = 16.dp)
                .fillMaxSize()

        ) {
            Text(text = "你好，", fontSize = 24.sp, fontWeight = FontWeight.W800)
            Text(text = "欢迎登录", fontSize = 24.sp, fontWeight = FontWeight.W800)
            Spacer(modifier = Modifier.height(80.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username.value,
                onValueChange = {
                    username.value = it
                    usernameHasInteracted.value = true
                },
                label = { Text(text = "账号") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 下一个输入框
                ),
                singleLine = true,
                isError = usernameHasInteracted.value && (username.value.isEmpty() || username.value.length < 6), // 非空校验
                supportingText = {
                    if (usernameHasInteracted.value) {
                        if (username.value.isEmpty()) {
                            Text("账号不能为空", color = Color.Red)
                        } else if (username.value.length < 6) {
                            Text("账号至少6位", color = Color.Red)
                        }
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "密码") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 完成
                ),
                singleLine = true,
                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(), // 密码隐藏
                trailingIcon = {
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            imageVector = if (showPassword.value) Icons.TwoTone.Visibility
                            else Icons.TwoTone.VisibilityOff,
                            contentDescription = if (showPassword.value) "隐藏密码" else "显示密码"
                        )
                    }
                },
                isError = passwordHasInteracted.value && (password.value.isEmpty() || password.value.length < 6),
                supportingText = {
                    if (passwordHasInteracted.value) {
                        if (password.value.isEmpty()) {
                            Text("密码不能为空", color = Color.Red)
                        } else if (password.value.length < 6) {
                            Text("密码至少6位", color = Color.Red)
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // 登录逻辑
                }
            ) {
                Text(text = "登录")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "注册账号", fontSize = 16.sp,
                fontWeight = FontWeight.Normal, color = Color.Gray
            )

        }
    }

}

@Preview
@Composable
private fun LoginPagePreview() {
    WanAndroid_composeTheme {
        LoginPage()
    }

}