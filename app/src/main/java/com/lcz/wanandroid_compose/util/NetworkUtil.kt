package com.lcz.wanandroid_compose.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

/**
 * 网络工具类
 *
 * @author LTP 2022/4/14
 */
object NetworkUtil {
    /**
     * 网络是否可用
     *
     * @param context Context
     * @return  网络是否可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkAvailable(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo?.isAvailable == true
    }


    /**
     * 是否连接Wifi
     *
     * @param context
     * @return boolean
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isWifi(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo?.type == ConnectivityManager.TYPE_WIFI
    }
}