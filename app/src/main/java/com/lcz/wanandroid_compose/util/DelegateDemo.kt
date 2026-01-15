package com.lcz.wanandroid_compose.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SPDelegate<T>(
    private val key: String,
    private val defaultValue: T,
    private val spUtil: SPUtil = SPUtil.getInstance()
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is String -> spUtil.getString(key, defaultValue) as T
            is Int -> spUtil.getInt(key, defaultValue) as T
            is Boolean -> spUtil.getBoolean(key, defaultValue) as T
            is Float -> spUtil.getFloat(key, defaultValue) as T
            is Long -> spUtil.getLong(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        spUtil.put(key, value)
    }
}
//声明spName，交给委托类SPDelegate处理
var spName: String by SPDelegate("name", "")
// 设置spName
//spName = "张三"
//获取spName
//val myname = spName
