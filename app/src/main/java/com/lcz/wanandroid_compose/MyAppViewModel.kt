package com.lcz.wanandroid_compose

import com.lcz.wanandroid_compose.base.BaseViewModel
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
class MyAppViewModel : BaseViewModel() {
    private val _user = MutableStateFlow(UserManager.getInstance().getUser())
    val user = _user.asStateFlow()
    fun updateUser(user: LoginResponseBean) {
        _user.value = user
    }
}