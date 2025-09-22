package com.lcz.wanandroid_compose.data.repository

import com.google.gson.Gson
import com.lcz.wanandroid_compose.util.SPUtil

/**
 * 作者:     刘传政
 * 创建时间:  16:12 2025/9/19
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class SearchHistoryManager private constructor() {
    private val spUtil = SPUtil.getInstance()
    private val gson = Gson()

    // 保存搜索历史
    fun saveSearchHistory(history: List<String>) {
        var toJson = gson.toJson(history)
        spUtil.put(KEY_SEARCH_HISTORY, toJson)


    }

    // 获取搜索历史
    fun getSearchHistory(): List<String> {
        return spUtil.getString(KEY_SEARCH_HISTORY, "").takeIf {
            it.isNotEmpty()
        }?.let {
            gson.fromJson(it, Array<String>::class.java).toList()
        } ?: emptyList()
    }

    companion object {
        private const val KEY_SEARCH_HISTORY = "search_history" // 搜索历史

        @Volatile
        private var instance: SearchHistoryManager? = null

        fun getInstance(): SearchHistoryManager {
            return instance ?: synchronized(this) {
                instance ?: SearchHistoryManager().also { instance = it }
            }
        }
    }
}