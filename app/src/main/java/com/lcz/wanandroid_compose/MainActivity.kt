package com.lcz.wanandroid_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.lcz.wanandroid_compose.navigation.AppNavGraph
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //默认情况下Android 会根据 深色/浅色模式 自动选择文字颜色：
        //浅色模式：状态栏文字为 深色（黑色）
        //深色模式：状态栏文字为 浅色（白色）

        //如果需要手动设置状态栏文字颜色（例如，在深色模式下显示浅色文字），可以使用以下代码：
        /*// 设置状态栏背景颜色（示例：白色背景）
        window.statusBarColor = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //设置状态栏文字颜色为深色（适用于浅色背景）
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            //设置状态栏文字颜色为浅色（适用于深色背景）
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }*/
        setContent {
            // 0 默认的日间模式和夜间模式自动切换 1自定义的主题，不自动切换
            var themeType = MyApp.myAppViewModel.themeType.collectAsState()
            WanAndroid_composeTheme(themeType = themeType.value) {
                //第一个compose页面就是导航图,剩下的都是导航图控制的。这样逻辑清晰
                AppNavGraph()
            }
        }
    }
}