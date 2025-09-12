package com.lcz.wanandroid_compose.util

import android.content.Context
import androidx.core.content.edit

class SPUtil private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun put(key: String, value: Any?) {
        when (value) {
            is String -> sharedPreferences.edit { putString(key, value) }
            is Int -> sharedPreferences.edit { putInt(key, value) }
            is Boolean -> sharedPreferences.edit { putBoolean(key, value) }
            is Float -> sharedPreferences.edit { putFloat(key, value) }
            is Long -> sharedPreferences.edit { putLong(key, value) }
            null -> sharedPreferences.edit { remove(key) }
        }
    }

    fun getString(key: String, default: String = "") = sharedPreferences.getString(key, default) ?: default
    fun getInt(key: String, default: Int = 0) = sharedPreferences.getInt(key, default)
    fun getBoolean(key: String, default: Boolean = false) = sharedPreferences.getBoolean(key, default)
    fun getFloat(key: String, default: Float = 0f) = sharedPreferences.getFloat(key, default)
    fun getLong(key: String, default: Long = 0L) = sharedPreferences.getLong(key, default)

    fun remove(key: String) = sharedPreferences.edit { remove(key) }
    fun clear() = sharedPreferences.edit { clear() }

    companion object {
        @Volatile private var instance: SPUtil? = null
        
        fun init(context: Context) {
            instance = SPUtil(context.applicationContext)
        }

        fun getInstance(): SPUtil {
            return instance ?: throw IllegalStateException("SPUtil must be initialized first")
        }
    }
}