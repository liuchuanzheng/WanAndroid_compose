package com.lcz.wanandroid_compose.module.login.bean

data class LoginResponseBean(
    var admin: Boolean? = false, // false
    var chapterTops: List<Any?>? = listOf(),
    var coinCount: Int? = 0, // 990
    var collectIds: List<Int?>? = listOf(),
    var email: String? = "", // 1052374416@qq.com
    var icon: String? = "",
    var id: Int? = 0, // 6321
    var nickname: String? = "", // 多弗朗明哥11111
    var password: String? = "",
    var publicName: String? = "", // 多弗朗明哥11111
    var token: String? = "",
    var type: Int? = 0, // 0
    var username: String? = "" // 18501231486
)