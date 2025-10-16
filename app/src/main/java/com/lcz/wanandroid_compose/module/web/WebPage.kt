package com.lcz.wanandroid_compose.module.web

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * 作者:     刘传政
 * 创建时间:  10:38 2025/9/5
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
// ... 已有导入 ...

@Composable
fun WebPage(
    url: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Boolean = { false }
) {
    // 获取当前 composable 的 Context
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportZoom(true)
            }
            webViewClient = WebViewClient()
        }
    }

    var loadingProgress by remember { mutableIntStateOf(0) }
    Scaffold {
        Column(modifier = modifier.padding(it)) {
            if (loadingProgress <= 100) {
                LinearProgressIndicator(
                    progress = { loadingProgress / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Blue,  // 进度条颜色
                    trackColor = Color.LightGray  // 背景轨道颜色
                )
            }

            AndroidView(
                factory = {
                    webView
                },
                update = { view ->
                    if (view.url != url) {
                        view.loadUrl(url)
                    }
                    view.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView, newProgress: Int) {
                            loadingProgress = newProgress
                        }
                    }
                }
            )
        }
    }


    // 处理返回键逻辑
    BackHandler(enabled = webView.canGoBack()) {
        if (!webView.canGoBack()) {
            webView.goBack()
        } else {
            onBackPressed()
        }
    }
}