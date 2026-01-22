package com.lcz.wanandroid_compose.module.demo.calendar

import android.Manifest
import android.R.attr.text
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.lcz.wanandroid_compose.ui.theme.WanAndroid_composeTheme
import com.lcz.wanandroid_compose.util.ToastUtil
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * 添加事件到手机的日历app中。保证了提醒的可靠性
 */
class CalendarActivity : ComponentActivity() {
    // 权限申请回调
    private val requestCalendarPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val writeGranted = permissions[Manifest.permission.WRITE_CALENDAR] ?: false
        val readGranted = permissions[Manifest.permission.READ_CALENDAR] ?: false
        if (writeGranted && readGranted) {
            // 权限已授予，可添加日历提醒
            ToastUtil.showLong("权限已授予，可添加日历提醒")
        } else {
            // 权限被拒绝，提示用户
            // 可弹框引导用户到设置页开启权限
            ToastUtil.showLong("权限被拒绝，无法添加日历提醒")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroid_composeTheme {
                Scaffold() {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {

                        Button(onClick = {
                            // 检查权限
                            if (ContextCompat.checkSelfPermission(
                                    this@CalendarActivity,
                                    Manifest.permission.WRITE_CALENDAR
                                )
                                != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(
                                    this@CalendarActivity,
                                    Manifest.permission.READ_CALENDAR
                                )
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                // 申请权限
                                requestCalendarPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.WRITE_CALENDAR,
                                        Manifest.permission.READ_CALENDAR
                                    )
                                )
                            } else {
                                // 权限已授予
                                ToastUtil.showLong("权限已授予，可添加日历提醒")
                            }
                        }) {
                            Text("申请权限")
                        }
                        Button(onClick = {
//                            addDrugReminderToCalendar()
                            addCalendarReminder2(this@CalendarActivity)
                        }) {
                            Text("添加事件到日历")
                        }
                        Button(onClick = {
                            if (eventId != -1L) {
                                deleteCalendarReminder(eventId)
                            } else {
                                ToastUtil.showLong("请先添加事件到日历")
                            }
                        }) {
                            Text("删除事件")
                        }
                    }
                }

            }
        }
    }
    private fun addCalendarReminder2(context: Context){
        val scheduleDetail = ScheduleDetail().apply {
            name = "用药提醒2"
            location = "用药地点"
            startTimestamp = System.currentTimeMillis()+10*1000
            endTimestamp = System.currentTimeMillis() + 60 * 1000
            description = "请按时服药"
            previousMinutes = 0 // 准时提醒
        }
         eventId = CalendarReminderUtil2.addCalendarEvent(context, scheduleDetail)
        ToastUtil.showLong("添加成功，事件ID: $eventId")
    }
    var eventId: Long = -1L
    /**
     * 添加用药提醒到系统日历
     */
    private fun addDrugReminderToCalendar() {
        // 示例：设置今天18:00的提醒
        val remindTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 18)
            set(Calendar.SECOND, 0)
            // 如果时间已过，设为明天
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // 调用工具类添加日历提醒
        val eventId = CalendarReminderUtil.addCalendarReminder(
            context = this,
            drugName = "降压药",
            dosage = "1片/次",
            remindTime = remindTime,
            advanceMinutes = 0 // 准时提醒
        )

        // 保存eventId，后续可用于删除提醒
        // 例如：存入Room数据库
        if (eventId != -1L) {
            this.eventId = eventId
            lifecycleScope.launch {
                ToastUtil.showLong("添加成功，事件ID: $eventId")
            }
        }
    }
    /**
     * 示例：删除日历提醒
     */
    private fun deleteCalendarReminder(eventId: Long) {
        CalendarReminderUtil.deleteCalendarReminder(this, eventId)
    }

}