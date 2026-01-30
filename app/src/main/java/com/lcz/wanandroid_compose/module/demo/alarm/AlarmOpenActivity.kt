package com.lcz.wanandroid_compose.module.demo.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme

class AlarmOpenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroid_composeTheme {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("闹钟时间到了，打开了此页面")
                    Button(onClick = {
                        finish()
                    }) {
                        Text("关闭闹钟")
                    }
                }
            }
        }
    }

}