package com.lcz.wanandroid_compose.data.repository

import com.lcz.wanandroid_compose.base.BaseNetResponseBean
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.net.CommonService
import com.lcz.wanandroid_compose.net.RetrofitManager
import kotlin.getValue
import kotlin.jvm.java

/**
 * 作者:     刘传政
 * 创建时间:  10:59 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 *          数据仓库的作用是将数据从数据源中获取并提供给应用程序使用。
 *          数据仓库可以是本地数据库、远程服务器或其他数据源。
 *          数据仓库的作用是隐藏数据的来源和获取方式，使应用程序可以方便地访问和使用数据。
 */
object CommonRepository {
    private val commonService by lazy {
        RetrofitManager.getService(CommonService::class.java)
    }

    suspend fun login(username: String, password: String): BaseNetResponseBean<LoginResponseBean> {
        return commonService.login(username, password)
    }

}