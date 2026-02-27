package com.lcz.wanandroid_compose.navigation

import kotlinx.serialization.Serializable


//页面路径
//密封类可以把所有的子类都列出来，方便在when表达式中使用。每个类可以有自己不同的参数。与枚举不同
sealed class AppRoutePath {
    //主页
    @Serializable
    data class Main(val description: String = "主页", val from: String = "") : AppRoutePath()
    //测试页
    @Serializable
    data class Test(val description: String = "测试页", val from: String = "") : AppRoutePath()
    //测试页2
    @Serializable
    data class Test2(val description: String = "测试页2", val from: String = "") : AppRoutePath()
    //登录页
    @Serializable
    data class Login(val description: String = "登录页", val from: String = "") : AppRoutePath()
    //我的积分
    @Serializable
    data class MyCoinHistory(val description: String = "我的积分", val from: String = "") :
        AppRoutePath()
    //积分排行榜
    @Serializable
    data class CoinRank(val description: String = "积分排行榜", val from: String = "") :
        AppRoutePath()
    //设置页
    @Serializable
    data class Setting(val description: String = "设置页", val from: String = "") : AppRoutePath()
    //搜索页
    @Serializable
    data class Search(val description: String = "搜索页", val from: String = "") : AppRoutePath()
    //签名页
    @Serializable
    data class Sign(val description: String = "签名页", val from: String = "") : AppRoutePath()
    //Demo页
    @Serializable
    data class Demo(val description: String = "Demo页", val from: String = "") : AppRoutePath()
    //滑动条Demo页
    @Serializable
    data class SliderDemo(val description: String = "滑动条Demo页", val from: String = "") :
        AppRoutePath()
    //抖音视频页
    @Serializable
    data class TickTok(val description: String = "抖音视频页", val from: String = "") :
        AppRoutePath()
    //抖音风格视频进度条页
    @Serializable
    data class TickTokProgressBar(
        val description: String = "抖音风格视频进度条页",
        val from: String = ""
    ) : AppRoutePath()
    //网络缓存页
    @Serializable
    data class NetCache(val description: String = "网络缓存页", val from: String = "") :
        AppRoutePath()
    //网页
    @Serializable
    data class Web(
        val description: String = "网页", val from: String = "",
        val url: String//这里不写默认值，就是为了提醒调用者必须传入url
    ) : AppRoutePath()
    //嵌套滚动Demo页
    @Serializable
    data class NestedScrollDemo(val description: String = "嵌套滚动Demo页", val from: String = "") :
        AppRoutePath()
    //客服聊天页
    @Serializable
    data class CustomerService(val description: String = "客服聊天页", val from: String = "") :
        AppRoutePath()
}