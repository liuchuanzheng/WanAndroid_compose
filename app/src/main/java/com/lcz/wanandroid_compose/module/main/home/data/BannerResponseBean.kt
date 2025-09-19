package com.lcz.wanandroid_compose.module.main.home.data

/**
 * 作者:     刘传政
 * 创建时间:  10:54 2025/9/19
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class BannerResponseBean : ArrayList<BannerResponseBean.BannerResponseBeanItem>() {
    data class BannerResponseBeanItem(
        var desc: String? = "", // 我们支持订阅啦~
        var id: Int? = 0, // 30
        var imagePath: String? = "", // https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png
        var isVisible: Int? = 0, // 1
        var order: Int? = 0, // 2
        var title: String? = "", // 我们支持订阅啦~
        var type: Int? = 0, // 0
        var url: String? = "" // https://www.wanandroid.com/blog/show/3352
    )
}