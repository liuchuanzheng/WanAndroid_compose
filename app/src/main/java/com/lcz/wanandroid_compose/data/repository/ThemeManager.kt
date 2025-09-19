package com.lcz.wanandroid_compose.data.repository

import com.lcz.wanandroid_compose.util.SPUtil

/**
 * 作者:     刘传政
 * 创建时间:  16:12 2025/9/19
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class ThemeManager private constructor() {
    private val spUtil = SPUtil.getInstance()


    // 保存主题类型
    fun saveThemeType(themeType: Int) {
        spUtil.put(KEY_THEME_TYPE, themeType)
    }

    // 获取主题类型
    fun getThemeType(): Int {
        return spUtil.getInt(KEY_THEME_TYPE, 0)
    }


    companion object {
        private const val KEY_THEME_TYPE = "theme_type" // 0 默认的日间模式和夜间模式自动切换 1自定义的主题，不自动切换

        @Volatile
        private var instance: ThemeManager? = null

        fun getInstance(): ThemeManager {
            return instance ?: synchronized(this) {
                instance ?: ThemeManager().also { instance = it }
            }
        }
    }
}