package com.lcz.wanandroid_compose

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.ThemeManager
import com.lcz.wanandroid_compose.data.repository.UserManager
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 作者:     刘传政
 * 创建时间:  15:17 2025/9/12
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class MyAppViewModel(private val isPreview: Boolean = false) : BaseViewModel() {
    private val _user = MutableStateFlow<LoginResponseBean?>(LoginResponseBean())
    val user = _user.asStateFlow()
    private val _themeType = MutableStateFlow(0) // 0 默认的日间模式和夜间模式自动切换 1自定义的主题，不自动切换
    val themeType = _themeType.asStateFlow()

    init {
        if (isPreview) {
            // 添加预览用的模拟数据
            val user = LoginResponseBean().apply {
                id = 123
                nickname = "预览用户"
                icon = ""
            }
            _user.value = user
        } else {
            _user.value = UserManager.getInstance().getUser()
            _themeType.value = ThemeManager.getInstance().getThemeType()
        }
    }

    fun updateUser(user: LoginResponseBean) {
        _user.value = user
    }

    fun updateThemeType(type: Int) {
        _themeType.value = type
    }

}