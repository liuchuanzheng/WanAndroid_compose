package com.lcz.wanandroid_compose.util

import android.content.Context
import android.widget.Toast
import com.lcz.wanandroid_compose.MyApp

object ToastUtil {

    private val context: Context
        get() = MyApp.instance.applicationContext // 需要确保应用初始化时赋值

    fun showShort(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLong(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showShort(resId: Int) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
    }

    fun showLong(resId: Int) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG).show()
    }
    fun showNotDev() {
        showShort("暂未开发该功能")
    }
}