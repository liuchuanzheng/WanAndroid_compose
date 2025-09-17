package com.lcz.wanandroid_compose.module.setting.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowForwardIos
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.navigation.app_navigateToSetting
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.util.ToastUtil
import com.lcz.wanandroid_compose.widget.ConfirmDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 作者:     刘传政
 * 创建时间:  15:50 2025/9/17
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:     设置页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(paramsBean: AppRoutePath.Setting) {
    val context = LocalContext.current
    val cacheSize = remember { mutableStateOf("0.00MB") }
    val showClearCacheDialog = remember { mutableStateOf(false) }
    // 缓存大小计算
    LaunchedEffect(Unit) {
        cacheSize.value = withContext(Dispatchers.IO) {
            val internalCache = context.cacheDir
            val externalCache = context.externalCacheDir ?: File("")
            val size = getDirSize(internalCache) + getDirSize(externalCache)
            "%.2fMB".format(size / (1024.0 * 1024.0))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "设置") },
                navigationIcon = {
                    IconButton(onClick = {
                        globalNavController?.popBackStack()
                    }) {
                        Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),

                )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(40.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.primary)

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "通用")
                }
                Card(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SettingItem(title = "清除缓存", trailingText = cacheSize.value, modifier = Modifier.clickable {
                        // 清除缓存逻辑
                        showClearCacheDialog.value = true
                    })
                }
            }
        }
        if (showClearCacheDialog.value) {
            ConfirmDialog(
                title = "清除缓存",
                content = "确定清除缓存吗？",
                onOk = {
                    showClearCacheDialog.value = false
                    val internalCache = context.cacheDir
                    val externalCache = context.externalCacheDir ?: File("")
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteDir(internalCache)
                        deleteDir(externalCache)
                        withContext(Dispatchers.Main) {

                            cacheSize.value = "0.00MB"
                            ToastUtil.showShort("缓存已清理")
                        }
                    }
                },
                onCancel = {
                    showClearCacheDialog.value = false
                },
                onDismiss = {
                    showClearCacheDialog.value = false
                }
            )
        }
    }
}

// 新增目录大小计算方法
private fun getDirSize(dir: File): Long {
    if (!dir.exists()) return 0L
    return dir.walk().filter { it.isFile }.sumOf { it.length() }
}
// 添加目录删除方法
private fun deleteDir(dir: File): Boolean {
    return if (dir.isDirectory) {
        dir.listFiles()?.forEach { child ->
            deleteDir(child)
        }
        dir.delete()
    } else {
        dir.delete()
    }
}

@Composable
private fun SettingItem(modifier: Modifier = Modifier, title: String, trailingText: String = "") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.White)
            .height(50.dp)
            .padding(horizontal = 16.dp, vertical = 5.dp)


    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.W400)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = trailingText, fontSize = 13.sp, fontWeight = FontWeight.W400)
        Icon(imageVector = Icons.TwoTone.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
    }
}
