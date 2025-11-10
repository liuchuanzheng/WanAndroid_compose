package com.lcz.wanandroid_compose.navigation

import kotlinx.serialization.Serializable

object AppRoutePath {
    @Serializable
    data class Main(val description: String = "主页", val from: String = "")

    @Serializable
    data class Test(val description: String = "测试页", val from: String = "")

    @Serializable
    data class Test2(val description: String = "测试页2", val from: String = "")

    @Serializable
    data class Login(val description: String = "登录页", val from: String = "")

    @Serializable
    data class MyCoinHistory(val description: String = "我的积分", val from: String = "")

    @Serializable
    data class CoinRank(val description: String = "积分排行榜", val from: String = "")
    @Serializable
    data class Setting(val description: String = "设置页", val from: String = "")
    @Serializable
    data class Search(val description: String = "搜索页", val from: String = "")
    @Serializable
    data class Sign(val description: String = "签名页", val from: String = "")
    @Serializable
    data class Demo(val description: String = "Demo页", val from: String = "")
    @Serializable
    data class SliderDemo(val description: String = "滑动条Demo页", val from: String = "")
    
    @Serializable
    data class TickTok(val description: String = "抖音视频页", val from: String = "")
    @Serializable
    data class TickTokProgressBar(val description: String = "抖音风格视频进度条页", val from: String = "")
    @Serializable
    data class NetCache(val description: String = "网络缓存页", val from: String = "")
    @Serializable
    data class Web(val description: String = "网页", val from: String = "", val url: String = "")
    @Serializable
    data class NestedScrollDemo(val description: String = "嵌套滚动Demo页", val from: String = "")
    @Serializable
    data class CustomerService(val description: String = "客服聊天页", val from: String = "")
}