package com.lcz.wanandroid_compose.util

//Flow当连续发送相同值时会被合并,即使对象是新建的。要让相同值不合并，可以通过添加版本号或时间戳包装数据
data class DataWrapper<T>(
    val data: T,
    val version: Int = 0 // 每次更新递增版本号
)