package com.lcz.wanandroid_compose.module.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcz.wanandroid_compose.MyApp
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.constant.MyConstant
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.data.repository.UserManager
import com.lcz.wanandroid_compose.navigation.globalNavController
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.util.ToastUtil
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
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun login() {
        viewModelScope.launch {
            _isLoading.update { true }
            val userName = _userName.value
            val password = _password.value
            try {
                var responseBean = CommonRepository.login(userName, password)
                if (responseBean.errorCode == MyConstant.Net.CODE_SUCCESS) {
                    //这里写死头像  因为登录后返回的头像是个空字符串
                    responseBean.data?.icon = "https://img1.baidu.com/it/u=1221952588,3009131272&fm=253&app=138&f=JPEG?w=500&h=500"
                    UserManager.getInstance().saveUser(responseBean.data)
                    MyApp.myAppViewModel.updateUser(responseBean.data!!)
                    ToastUtil.showShort("登录成功")
                    globalNavController?.popBackStack()
                } else {
                    ToastUtil.showShort("登录失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtil.showLong("${e.message}")
            } finally {
                _isLoading.update { false }
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