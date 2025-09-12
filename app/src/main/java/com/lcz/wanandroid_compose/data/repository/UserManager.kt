package com.lcz.wanandroid_compose.data.repository

import com.google.gson.Gson
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.util.SPUtil
import kotlin.jvm.java

class UserManager private constructor() {
    private val spUtil = SPUtil.getInstance()
    private val gson = Gson()

    // 用户登录状态
    fun isLoggedIn() = spUtil.getBoolean(KEY_IS_LOGIN, false)

    // 保存用户信息
    fun saveUser(user: LoginResponseBean?) {
        user?.let {
            spUtil.put(KEY_USER_INFO, gson.toJson(it))
            spUtil.put(KEY_IS_LOGIN, true)
        }
    }

    // 获取用户信息
    fun getUser(): LoginResponseBean? {
        return spUtil.getString(KEY_USER_INFO)?.takeIf {
            it.isNotEmpty()
        }?.let {
            gson.fromJson(it, LoginResponseBean::class.java)
        }
    }

    // 清除用户信息
    fun clearUser() {
        spUtil.remove(KEY_USER_INFO)
        spUtil.put(KEY_IS_LOGIN, false)
    }

    companion object {
        private const val KEY_IS_LOGIN = "user_login_status"
        private const val KEY_USER_INFO = "user_info"

        @Volatile
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager().also { instance = it }
            }
        }
    }
}