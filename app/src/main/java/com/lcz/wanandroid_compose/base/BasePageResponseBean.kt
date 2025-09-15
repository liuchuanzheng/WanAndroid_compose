package com.lcz.wanandroid_compose.base

/**
 * 分页实体
 *
 */
data class BasePageResponseBean<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)