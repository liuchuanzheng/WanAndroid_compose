package com.lcz.wanandroid_compose.util

import android.util.Log

/**
 * 通用日志工具类
 * - 支持开关控制（生产环境可关闭日志）
 * - 支持默认全局Tag和自定义Tag
 * - 包含常用日志级别（v/d/i/w/e）
 */
object LogUtil {
    // 是否启用日志（建议生产环境设为 false）
    var isLogEnabled = true
    // 默认全局Tag（可通过方法参数自定义单个日志Tag）
    var globalTag = "WanAndroid"

    /**
     * Verbose 级别日志
     * @param tag 日志标签（默认使用全局Tag）
     * @param msg 日志内容
     */
    fun v(tag: String = globalTag, msg: String) {
        if (isLogEnabled) Log.v(tag, msg)
    }

    /**
     * Debug 级别日志
     * @param tag 日志标签（默认使用全局Tag）
     * @param msg 日志内容
     */
    fun d(tag: String = globalTag, msg: String) {
        if (isLogEnabled) Log.d(tag, msg)
    }

    /**
     * Info 级别日志
     * @param tag 日志标签（默认使用全局Tag）
     * @param msg 日志内容
     */
    fun i(tag: String = globalTag, msg: String) {
        if (isLogEnabled) Log.i(tag, msg)
    }

    /**
     * Warn 级别日志
     * @param tag 日志标签（默认使用全局Tag）
     * @param msg 日志内容
     */
    fun w(tag: String = globalTag, msg: String) {
        if (isLogEnabled) Log.w(tag, msg)
    }

    /**
     * Error 级别日志（支持异常信息）
     * @param tag 日志标签（默认使用全局Tag）
     * @param msg 日志内容
     * @param tr 异常对象（可选）
     */
    fun e(tag: String = globalTag, msg: String, tr: Throwable? = null) {
        if (isLogEnabled) {
            tr?.let { Log.e(tag, msg, it) } ?: Log.e(tag, msg)
        }
    }
}