package com.lcz.wanandroid_compose.appwidget

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.annotation.RequiresPermission
import com.lcz.wanandroid_compose.MainActivity
import com.lcz.wanandroid_compose.R
import com.lcz.wanandroid_compose.util.LogUtil
import kotlin.jvm.java

class WanAndroidWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        //appWidgetIds 是一个整数数组，包含所有要更新的小部件的 ID.因为桌面可能添加多个相同的小组件
        LogUtil.i("小组件", "onUpdate${appWidgetIds.contentToString()}")
        // 更新所有小部件实例
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onEnabled(context: Context) {
        // 第一个小部件被添加到桌面时调用
        LogUtil.i("小组件", "onEnabled")
    }

    override fun onDisabled(context: Context) {
        // 最后一个小部件从桌面移除时调用
        LogUtil.i("小组件", "onDisabled")
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // 创建 RemoteViews 对象
        val views = RemoteViews(context.packageName, R.layout.app_widget_layout)
        
        // 设置小部件内容
        views.setTextViewText(R.id.widget_title, "WanAndroid")
        views.setTextViewText(R.id.widget_content, "最新文章已加载")
        
        // 设置按钮点击事件
        val intent = Intent(context, WanAndroidWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        }
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent)
        // 设置打开APP按钮点击事件
//        val openIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wanandroid.com"))
        val openIntent = Intent(context, MainActivity::class.java)
        val openPendingIntent = android.app.PendingIntent.getActivity(
            context,
            appWidgetId,
            openIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_button_open, openPendingIntent)
        
        // 更新小部件
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}