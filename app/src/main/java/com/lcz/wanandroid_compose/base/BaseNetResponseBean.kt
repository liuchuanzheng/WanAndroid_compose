package com.lcz.wanandroid_compose.base

/**
 * 作者:     刘传政
 * 创建时间:  14:18 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
data class BaseNetResponseBean<T>(
    var data: T? = null,
    var errorCode: Int = 0,
    var errorMsg: String? = null,
)
