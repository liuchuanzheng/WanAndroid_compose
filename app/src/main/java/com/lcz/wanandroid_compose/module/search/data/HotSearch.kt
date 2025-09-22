package com.lcz.wanandroid_compose.module.search.data

/**
 * 热门搜索
 * @author LTP  2022/4/19
 */
data class HotSearch(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
)