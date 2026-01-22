package com.lcz.wanandroid_compose.module.demo.calendar

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import java.util.*

object CalendarReminderUtil {
    // 日历账户名称（自定义）
    private const val CALENDAR_ACCOUNT_NAME = "家人用药提醒"
    private const val CALENDAR_ACCOUNT_TYPE = "com.example.medicinereminder"
    private const val CALENDAR_DISPLAY_NAME = "用药提醒日历"

    /**
     * 向系统日历添加用药提醒事件
     * @param context 上下文
     * @param drugName 药物名称
     * @param dosage 服用剂量
     * @param remindTime 提醒时间（精确到分钟）
     * @param提前提醒分钟数（默认0，即准时提醒）
     * @return 事件ID（失败返回-1）
     */
    fun addCalendarReminder(
        context: Context,
        drugName: String,
        dosage: String,
        remindTime: Calendar,
        advanceMinutes: Int = 0
    ): Long {
        // 1. 检查权限
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return -1
        }

        val contentResolver = context.contentResolver

        // 2. 获取/创建日历账户（必须有账户才能插入事件）
        val calendarId = getOrCreateCalendarAccount(contentResolver)
        if (calendarId == -1L) return -1

        // 3. 构建日程事件
        val eventValues = ContentValues().apply {
            // 日历ID
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            // 事件标题
            put(CalendarContract.Events.TITLE, "用药提醒：$drugName")
            // 事件描述（包含剂量）
            put(CalendarContract.Events.DESCRIPTION, "服用剂量：$dosage")
            // 事件开始时间（毫秒）
            put(CalendarContract.Events.DTSTART, remindTime.timeInMillis - advanceMinutes * 60 * 1000)
            // 事件结束时间（开始后15分钟）
            put(CalendarContract.Events.DTEND, remindTime.timeInMillis - advanceMinutes * 60 * 1000 + 15 * 60 * 1000)
            // 时区
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            // 事件类型（默认）
            put(CalendarContract.Events.EVENT_LOCATION, "家中")
            // 允许修改/删除
//            put(CalendarContract.Events.CAN_EDIT, 1)
            // 账户信息
//            put(CalendarContract.Events.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
//            put(CalendarContract.Events.ACCOUNT_TYPE, CALENDAR_ACCOUNT_TYPE)
            // 设置为全天事件（可选，避免时间显示问题）
            put(CalendarContract.Events.ALL_DAY, 0)
            put(CalendarContract.Events.HAS_ALARM, 1) //设置有闹钟提醒
        }

        // 4. 插入事件到日历
        val eventUri = CalendarContract.Events.CONTENT_URI.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACCOUNT_TYPE)
            .build()
        val insertedUri: Uri? = contentResolver.insert(eventUri, eventValues)
        val eventId = insertedUri?.lastPathSegment?.toLongOrNull() ?: -1L
        if (eventId == -1L) return -1

        // 5. 为事件添加系统提醒（关键：触发声音/弹窗）
        val reminderValues = ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId)
            // 提醒时间（相对于事件开始时间，这里设为0即准时提醒）
            put(CalendarContract.Reminders.MINUTES, 0)
            // 提醒方式：ALERT（弹窗+声音）+ EMAIL（可选）
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)

        return eventId
    }

    /**
     * 删除日历中的用药提醒
     * @param context 上下文
     * @param eventId 事件ID
     */
    fun deleteCalendarReminder(context: Context, eventId: Long) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED || eventId == -1L
        ) {
            return
        }
        val contentResolver = context.contentResolver
        // 删除事件
        contentResolver.delete(
            CalendarContract.Events.CONTENT_URI.buildUpon()
                .appendPath(eventId.toString())
                .build(),
            null,
            null
        )
    }

    /**
     * 获取或创建日历账户
     */
    private fun getOrCreateCalendarAccount(contentResolver: ContentResolver): Long {
        // 1. 查询是否已有账户
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ?"
        val selectionArgs = arrayOf(CALENDAR_ACCOUNT_NAME, CALENDAR_ACCOUNT_TYPE)

        val cursor = contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
            }
        }

        // 2. 没有账户则创建
        val calendarValues = ContentValues().apply {
            put(CalendarContract.Calendars.NAME, CALENDAR_DISPLAY_NAME)
            put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACCOUNT_TYPE)
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_DISPLAY_NAME)
            put(CalendarContract.Calendars.CALENDAR_COLOR, -0x10000) // 红色（醒目）
            put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDAR_ACCOUNT_NAME)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        }

        // 创建日历账户需要添加特殊URI参数
        val uri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACCOUNT_TYPE)
            .build()

        val newUri = contentResolver.insert(uri, calendarValues)
        return newUri?.lastPathSegment?.toLongOrNull() ?: -1L
    }
}


