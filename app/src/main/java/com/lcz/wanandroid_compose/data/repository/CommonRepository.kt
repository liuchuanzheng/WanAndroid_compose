package com.lcz.wanandroid_compose.data.repository

import com.lcz.wanandroid_compose.base.BaseNetResponseBean
import com.lcz.wanandroid_compose.base.BasePageResponseBean
import com.lcz.wanandroid_compose.module.coin.data.MyCoinHistoryResponseBean
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.main.home.data.BannerResponseBean
import com.lcz.wanandroid_compose.module.main.mine.bean.MyCoinResponseBean
import com.lcz.wanandroid_compose.module.search.data.HotSearch
import com.lcz.wanandroid_compose.net.CommonApiService
import com.lcz.wanandroid_compose.net.RetrofitManager
import kotlin.getValue
import kotlin.jvm.java

/**
 * 作者:     刘传政
 * 创建时间:  10:59 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 *          数据仓库的作用是将数据从数据源中获取并提供给应用程序使用。
 *          数据仓库可以是本地数据库、远程服务器或其他数据源。
 *          数据仓库的作用是隐藏数据的来源和获取方式，使应用程序可以方便地访问和使用数据。
 */
object CommonRepository {
    private val commonService by lazy {
        RetrofitManager.getService(CommonApiService::class.java)
    }

    suspend fun login(username: String, password: String): BaseNetResponseBean<LoginResponseBean> {
        return commonService.login(username, password)
    }

    suspend fun getMyCoin(): BaseNetResponseBean<MyCoinResponseBean> {
        return commonService.getMyCoin()
    }

    suspend fun getMyCoinHistory(
        pageNo: Int,
        page_size: Int
    ): BaseNetResponseBean<BasePageResponseBean<MyCoinHistoryResponseBean>> {
        return commonService.getMyCoinHistory(pageNo, page_size)
    }

    suspend fun getCoinRankList(
        pageNo: Int,
        pageSize: Int
    ): BaseNetResponseBean<BasePageResponseBean<MyCoinResponseBean>> {
        return commonService.getCoinRankList(pageNo, pageSize)
    }

    suspend fun getArticleList(
        pageNo: Int,
        pageSize: Int,
        categoryId: Int? = null
    ): BaseNetResponseBean<BasePageResponseBean<Article>> {
        return commonService.getArticlePageList(pageNo, pageSize, categoryId)
    }

    /** 获取置顶文章集合数据 */
    suspend fun getArticleTopList(): BaseNetResponseBean<List<Article>> {
        return commonService.getArticleTopList()
    }

    /** 获取轮播图数据 */
    suspend fun getBanner(): BaseNetResponseBean<BannerResponseBean> {
        return commonService.getBanner()
    }

    /** 获取热门搜索数据 */
    suspend fun getHotSearchList(): BaseNetResponseBean<List<HotSearch>> {
        return commonService.getHotSearchList()
    }

    /** 根据关键词搜索数据 */
    suspend fun searchDataByKey(
        pageNo: Int,
        pageSize: Int,
        searchKey: String
    ): BaseNetResponseBean<BasePageResponseBean<Article>> {
        return commonService.searchDataByKey(pageNo, pageSize, searchKey)
    }
}