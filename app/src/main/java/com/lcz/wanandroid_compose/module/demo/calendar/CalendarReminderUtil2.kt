package com.lcz.wanandroid_compose.module.demo.calendar

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.text.TextUtils
import android.util.Log
import android.util.Log.e
import java.lang.Exception
import java.util.ArrayList
import java.util.Calendar
import java.util.TimeZone
import java.util.*

/**
 * 日历详情
 * @author jerome
 */
class ScheduleDetail {
    var id: String? = null
    var name: String? = null//标题
    var location: String? = null//位置信息
    var startTimestamp: Long = 0//日历事件开始时间
    var endTimestamp: Long = 0//日历事件结束时间
    var description: String? = null//描述
    var previousMinutes: Int = 0//日历提前提醒时间
}

object CalendarReminderUtil2 {
    private const val CALENDER_URL = "content://com.android.calendar/calendars"

    //    private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"
    private val CALENDER_EVENT_URL = CalendarContract.Events.CONTENT_URI.toString()

    //    private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"
    private val CALENDER_REMINDER_URL = CalendarContract.Reminders.CONTENT_URI.toString()
    private const val CALENDARS_NAME = "android"
    private const val CALENDARS_ACCOUNT_NAME = "android.project.cn"
    private const val CALENDARS_ACCOUNT_TYPE = "com.android.project"
    private const val CALENDARS_DISPLAY_NAME = "android"
    private const val PREFIX_CALENDAR_ID = "93285712"
    var isCalendarHandle = false

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private fun checkAndAddCalendarAccount(context: Context): Int {
        val oldId = checkCalendarAccount(context)
        return if (oldId >= 0) {
            oldId
        } else {
            val addId = addCalendarAccount(context)
            if (addId >= 0) {
                checkCalendarAccount(context)
            } else {
                -1
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    @SuppressLint("Range")
    private fun checkCalendarAccount(context: Context): Int {
        val userCursor =
            context.contentResolver.query(Uri.parse(CALENDER_URL), null, null, null, null)
        return try {
            if (userCursor == null) {
                return -1
            }
            val count = userCursor.count
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst()
                userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
            } else {
                -1
            }
        } finally {
            userCursor?.close()
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private fun addCalendarAccount(context: Context): Long {
        if (!isCalendarHandle) {
            isCalendarHandle = true
            val timeZone = TimeZone.getDefault()
            val value = ContentValues()
            value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
            value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME)
            value.put(CalendarContract.Calendars.VISIBLE, 1)
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
            value.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER
            )
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME)
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
            var calendarUri = Uri.parse(CALENDER_URL)
            calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(
                    CalendarContract.Calendars.ACCOUNT_NAME,
                    CALENDARS_ACCOUNT_NAME
                )
                .appendQueryParameter(
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CALENDARS_ACCOUNT_TYPE
                )
                .build()
            val result = context.contentResolver.insert(calendarUri, value)
            isCalendarHandle = false
            return if (result == null) -1 else ContentUris.parseId(result)
        }
        return -1
    }

    /**
     * 添加日历事件
     */
    fun addCalendarEvent(context: Context?, item: ScheduleDetail): Long {
        try {
            if (context == null) {
                return -1
            }
            val calId = checkAndAddCalendarAccount(context)
            if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
                return -1
            }
            if (!isCalendarHandle) {
                isCalendarHandle = true
                val calendarTitle: String = item.name.toString()
                //添加日历事件
                val mCalendar = Calendar.getInstance()
                mCalendar.timeInMillis = item.startTimestamp
                val start = mCalendar.time.time
                mCalendar.timeInMillis = item.endTimestamp
                val end = mCalendar.time.time
                val event = ContentValues()
                event.put(CalendarContract.Events.TITLE, calendarTitle)
                event.put(CalendarContract.Events.EVENT_LOCATION, item.location)
                event.put(CalendarContract.Events.DESCRIPTION, item.description)
//                    event.put(CalendarContract.Events.ORIGINAL_ID, PREFIX_CALENDAR_ID+ item.id) //插入数据的id
                event.put(CalendarContract.Events.CALENDAR_ID, calId) //插入账户的id
                event.put(CalendarContract.Events.DTSTART, start)
                event.put(CalendarContract.Events.DTEND, end)
                event.put(CalendarContract.Events.HAS_ALARM, 1) //设置有闹钟提醒
//                    event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")
                event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                val newEvent = context.contentResolver.insert(Uri.parse(CALENDER_EVENT_URL), event)
                if (newEvent == null) {
                    return -1
                }
                val eventId = ContentUris.parseId(newEvent)
                //事件提醒的设定
                val values = ContentValues()
                values.put(CalendarContract.Reminders.EVENT_ID, eventId)
                values.put(
                    CalendarContract.Reminders.MINUTES,
                    item.previousMinutes
                ) // 提前previousDate分钟有提醒
                values.put(
                    CalendarContract.Reminders.METHOD,
                    CalendarContract.Reminders.METHOD_ALERT
                )
                val uri = context.contentResolver.insert(Uri.parse(CALENDER_REMINDER_URL), values)
                if (uri == null) {
                    return -1
                }
                isCalendarHandle = false
                return eventId
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            isCalendarHandle = false
        }
        return -1
    }

    /**
     * 删除日历事件
     */
    @SuppressLint("Range")
    fun deleteCalendarEvent(context: Context?, dataIdList: ArrayList<String>?) {
        if (context == null || dataIdList == null) {
            return
        }
        val eventCursor =
            context.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
        try {
            if (eventCursor == null) {
                return
            }
            if (!isCalendarHandle) {
                isCalendarHandle = true
                if (eventCursor.count > 0) {
                    //遍历所有事件，找到id跟需要查询的id一样的项
                    eventCursor.moveToFirst()
                    while (!eventCursor.isAfterLast) {
                        val eventId =
                            eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.ORIGINAL_ID))
                        for (item in dataIdList) {
                            val itemId = PREFIX_CALENDAR_ID + item
                            if (!TextUtils.isEmpty(itemId) && itemId == eventId) {
                                val id =
                                    eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) //取得id
                                val deleteUri = ContentUris.withAppendedId(
                                    Uri.parse(CALENDER_EVENT_URL),
                                    id.toLong()
                                )
                                val rows = context.contentResolver.delete(deleteUri, null, null)
                                if (rows == -1) {
                                    Log.e("jerome", "日历事件删除失败：$itemId")
                                }
                            }
                        }
                        eventCursor.moveToNext()
                    }
                }
                isCalendarHandle = false
            }

        } finally {
            eventCursor?.close()
            isCalendarHandle = false
        }
    }

    /**
     * 删除[startTime],[endTime]时间段日历事件
     */
    @SuppressLint("Range")
    fun deleteCalendarEvent(
        context: Context?,
        startTime: Long,
        endTime: Long,
        callBack: DeleteCalendarCallBack
    ) {
        if (context == null) {
            callBack.onDeleteComplete()
            return
        }
        try {
            val eventCursor =
                context.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            eventCursor.use { eventCursor ->
                if (eventCursor == null) {
                    callBack.onDeleteComplete()
                    return
                }
                if (!isCalendarHandle) {
                    isCalendarHandle = true
                    if (eventCursor.count > 0) {
                        eventCursor.moveToFirst()
                        while (!eventCursor.isAfterLast) {
                            val eventId =
                                eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.ORIGINAL_ID))
                            val dtStart =
                                eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART))
                            val dtEnd =
                                eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTEND))
                            if (dtEnd > startTime && dtStart < endTime) {
                                //日历事件在[startTime],[endTime]时间段内
                                if (eventId.startsWith(PREFIX_CALENDAR_ID)) {
                                    //删除添加的日历
                                    val id = eventCursor.getInt(
                                        eventCursor.getColumnIndex(CalendarContract.Calendars._ID)
                                    )
                                    val deleteUri = ContentUris.withAppendedId(
                                        Uri.parse(CALENDER_EVENT_URL),
                                        id.toLong()
                                    )
                                    val rows = context.contentResolver.delete(deleteUri, null, null)
                                    if (rows == -1) {
                                        Log.e("jerome", "日历事件删除失败：$eventId")
                                    } else {
                                        e("jerome", "日历事件删除成功：$eventId")
                                    }
                                }
                            }
                            eventCursor.moveToNext()
                        }
                    }
                    isCalendarHandle = false
                }
                callBack.onDeleteComplete()
            }
        } catch (e: Exception) {
            isCalendarHandle = false
            callBack.onDeleteComplete()
            Log.d("jerome", e.toString())
        }
    }

    interface DeleteCalendarCallBack {
        fun onDeleteComplete()
    }

}