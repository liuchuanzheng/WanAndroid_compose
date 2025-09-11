package com.lcz.wanandroid_compose.module.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  10:10 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class LoginViewModel : ViewModel() {
    private val _userName = MutableStateFlow("")//一个私有
    val userName = _userName.asStateFlow()//一个公共的 这个转换类型是为了不让外界修改
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    fun login() {
        viewModelScope.launch {
            val userName = _userName.value
            val password = _password.value
            try {
                var responseBean = CommonRepository.login(userName, password)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }
        }

    }
    fun updateUsername(value: String) {
        _userName.update { value }
    }

    fun updatePassword(value: String) {
        _password.update { value }
    }
}