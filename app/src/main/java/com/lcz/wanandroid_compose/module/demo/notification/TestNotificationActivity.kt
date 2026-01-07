package com.lcz.wanandroid_compose.module.demo.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lcz.wanandroid_compose.R

class TestNotificationActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID_BASIC = "basic_channel"
        const val CHANNEL_ID_HIGH = "high_priority_channel"
        const val CHANNEL_ID_CONVERSATION = "conversation_channel"
        const val NOTIFICATION_ID_BASIC = 1
        const val NOTIFICATION_ID_HIGH = 2
        const val NOTIFICATION_ID_CONVERSATION = 3
        const val KEY_TEXT_REPLY = "key_text_reply"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createNotificationChannels()

        val container = findViewById<LinearLayout>(R.id.main)

        // 基本通知
        val basicBtn = Button(this).apply {
            text = "发送基本通知"
            setOnClickListener { sendBasicNotification() }
        }
        container.addView(basicBtn)

        // 高优先级通知
        val highPriorityBtn = Button(this).apply {
            text = "发送高优先级通知"
            setOnClickListener { sendHighPriorityNotification() }
        }
        container.addView(highPriorityBtn)

        // 对话式通知
        val conversationBtn = Button(this).apply {
            text = "发送对话式通知"
            setOnClickListener { sendConversationNotification() }
        }
        container.addView(conversationBtn)

        // 大图通知
        val bigPictureBtn = Button(this).apply {
            text = "发送大图通知"
            setOnClickListener { sendBigPictureNotification() }
        }
        container.addView(bigPictureBtn)

        // 进度条通知
        val progressBtn = Button(this).apply {
            text = "发送进度条通知"
            setOnClickListener { sendProgressNotification() }
        }
        container.addView(progressBtn)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 基本通知渠道
            val basicChannel = NotificationChannel(
                CHANNEL_ID_BASIC,
                "基本通知",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "基本通知渠道"
            }

            // 高优先级通知渠道
            val highPriorityChannel = NotificationChannel(
                CHANNEL_ID_HIGH,
                "高优先级通知",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "高优先级通知渠道"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }

            // 对话式通知渠道
            val conversationChannel = NotificationChannel(
                CHANNEL_ID_CONVERSATION,
                "对话通知",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "对话式通知渠道"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(basicChannel, highPriorityChannel, conversationChannel))
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendBasicNotification() {
        val intent = Intent(this, TestNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_BASIC)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("基本通知")
            .setContentText("这是一个基本通知示例")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_BASIC, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendHighPriorityNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_HIGH)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("高优先级通知")
            .setContentText("这是一个高优先级通知，会发出声音和振动")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_HIGH, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendConversationNotification() {
        // 创建回复意图
        val replyIntent = Intent(this, TestNotificationActivity::class.java)
        val replyPendingIntent = PendingIntent.getActivity(
            this, 0, replyIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // 远程输入（用于直接回复）
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("回复消息")
            .build()

        // 回复动作
        val action = NotificationCompat.Action.Builder(
            IconCompat.createWithResource(this, R.drawable.ic_reply),
            "回复",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        // 创建对话人物
        val person = Person.Builder()
            .setName("系统消息")
            .setIcon(IconCompat.createWithResource(this, R.drawable.ic_person))
            .build()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_CONVERSATION)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("对话式通知")
            .setContentText("这是一条对话式通知，可以直接回复")
            .setStyle(NotificationCompat.MessagingStyle(person)
                .addMessage("您好，有什么可以帮助您的？", System.currentTimeMillis(), person))
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_CONVERSATION, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendBigPictureNotification() {
        val bigPicture = NotificationCompat.BigPictureStyle()
            .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.ic_large_image))
            .setBigContentTitle("大图通知")
            .setSummaryText("这是一张大图通知")

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_BASIC)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("大图通知")
            .setContentText("点击查看大图")
            .setStyle(bigPicture)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_BASIC + 100, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendProgressNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_BASIC)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("进度通知")
            .setContentText("下载中...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, 0, false)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_BASIC + 200, notification)
    }
}