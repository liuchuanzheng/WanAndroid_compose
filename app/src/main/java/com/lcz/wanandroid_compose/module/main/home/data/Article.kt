package com.lcz.wanandroid_compose.module.main.home.data

/**
 * 作者:     刘传政
 * 创建时间:  14:52 2025/9/18
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
data class Article(
    var adminAdd: Boolean? = false, // false
    var apkLink: String? = "",
    var audit: Int? = 0, // 1
    var author: String? = "",
    var canEdit: Boolean? = false, // false
    var chapterId: Int? = 0, // 502
    var chapterName: String? = "", // 自助
    var collect: Boolean? = false, // false
    var courseId: Int? = 0, // 13
    var desc: String? = "",
    var descMd: String? = "",
    var envelopePic: String? = "",
    var fresh: Boolean? = false, // false
    var host: String? = "",
    var id: Int? = 0, // 30768
//    var isAdminAdd: Boolean? = false, // false
    var link: String? = "", // https://juejin.cn/post/7541048326599622671
    var niceDate: String? = "", // 2025-08-22 10:19
    var niceShareDate: String? = "", // 2025-08-22 10:19
    var origin: String? = "",
    var prefix: String? = "",
    var projectLink: String? = "",
    var publishTime: Long? = 0, // 1755829147000
    var realSuperChapterId: Int? = 0, // 493
    var selfVisible: Int? = 0, // 0
    var shareDate: Long? = 0, // 1755829147000
    var shareUser: String? = "", // panoogunker@gmail.com
    var superChapterId: Int? = 0, // 494
    var superChapterName: String? = "", // 广场Tab
    var tags: List<Any?>? = listOf(),
    var title: String? = "", // Kotlin 老手怎么写代码？
    var type: Int? = 0, // 0
    var userId: Int? = 0, // 164286
    var visible: Int? = 0, // 1
    var zan: Int? = 0 // 0
){
    var isTop: Boolean = false// false
}