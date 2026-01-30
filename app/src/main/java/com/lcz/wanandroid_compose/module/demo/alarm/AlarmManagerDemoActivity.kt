package com.lcz.wanandroid_compose.module.demo.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import java.util.Calendar
import kotlin.jvm.java

class AlarmManagerDemoActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1001
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroid_composeTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Button(onClick = { checkAndRequestAlarmPermission() }) {
                        Text("请求精确闹钟权限")
                    }
                    Button(onClick = { openAlarmPermissionSettings() }) {
                        Text("打开闹钟权限设置")
                    }
                    Button(onClick = { checkAndRequestNotificationPermission() }) {
                        Text("请求通知权限")
                    }
                    Button(onClick = { setOneTimeAlarm() }) {
                        Text("设置一次性闹钟（5秒后）")
                    }
                    Button(onClick = { setRepeatingAlarm() }) {
                        Text("设置重复闹钟（每分钟）")
                    }
                    Button(onClick = { setOneTimeAlarmOpenActivity() }) {
                        Text("设置一次性闹钟打开Activity（5秒后）")
                    }
                    Button(onClick = { cancelAlarm() }) {
                        Text("取消所有闹钟")
                    }
                }
            }
        }
    }

    private fun setOneTimeAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "ALARM_ACTION"
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 5)

//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun setOneTimeAlarmOpenActivity() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmOpenActivity::class.java)

        // 添加FLAG_ACTIVITY_NEW_TASK以确保Activity能正常启动
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 2, intent, pendingIntentFlags
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 5)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ 使用setExactAndAllowWhileIdle
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Toast.makeText(this, "已设置闹钟（5秒后打开Activity）", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "无精确闹钟权限", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 旧版本Android
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Toast.makeText(this, "已设置闹钟（5秒后打开Activity）", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setRepeatingAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "REPEATING_ALARM_ACTION"
        val pendingIntent = PendingIntent.getBroadcast(
            this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 1)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            60 * 1000, // 每分钟
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun openAlarmPermissionSettings() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        startActivity(intent)
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 请求通知权限
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            } else {
                Toast.makeText(this, "已有通知权限", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Android 12及以下版本不需要运行时请求通知权限
            Toast.makeText(this, "当前Android版本无需请求通知权限", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // 请求权限
                requestPermissions(
                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                    REQUEST_CODE_SCHEDULE_EXACT_ALARM
                )
            } else {
                Toast.makeText(this, "已有精确闹钟权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        when (requestCode) {
            REQUEST_CODE_SCHEDULE_EXACT_ALARM -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "精确闹钟权限已授予", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "精确闹钟权限被拒绝", Toast.LENGTH_SHORT).show()
                }
            }

            REQUEST_CODE_POST_NOTIFICATIONS -> { // 新增通知权限处理
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "通知权限已授予", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "通知权限被拒绝", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 取消一次性闹钟
        val intent1 = Intent(this, AlarmReceiver::class.java)
        intent1.action = "ALARM_ACTION"
        val pendingIntent1 = PendingIntent.getBroadcast(
            this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent1)

        // 取消重复闹钟
        val intent2 = Intent(this, AlarmReceiver::class.java)
        intent2.action = "REPEATING_ALARM_ACTION"
        val pendingIntent2 = PendingIntent.getBroadcast(
            this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent2)
        val intent3 = Intent(this, AlarmOpenActivity::class.java)
        val pendingIntent3 = PendingIntent.getActivity(
            this, 2, intent3, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent3)
    }
}