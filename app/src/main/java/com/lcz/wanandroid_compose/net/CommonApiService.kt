package com.lcz.wanandroid_compose.net

import com.lcz.wanandroid_compose.base.BaseNetResponseBean
import com.lcz.wanandroid_compose.base.BasePageResponseBean
import com.lcz.wanandroid_compose.constant.MyConstant
import com.lcz.wanandroid_compose.module.coin.data.MyCoinHistoryResponseBean
import com.lcz.wanandroid_compose.module.login.bean.LoginResponseBean
import com.lcz.wanandroid_compose.module.main.mine.bean.MyCoinResponseBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 作者:     刘传政
 * 创建时间:  14:13 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
interface CommonApiService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): BaseNetResponseBean<LoginResponseBean>

    /** 获取个人积分 */
    @GET("lg/coin/userinfo/json")
    suspend fun getMyCoin(): BaseNetResponseBean<MyCoinResponseBean>

    /** 获取个人积分获取列表，需要登录后访问 */
    @GET("lg/coin/list/{pageNo}/json")
    suspend fun getMyCoinHistory(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") page_size: Int
    ): BaseNetResponseBean<BasePageResponseBean<MyCoinHistoryResponseBean>>

    /** 获取积分排行列表分页 */
    @GET("coin/rank/{pageNo}/json")
    suspend fun getCoinRankList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): BaseNetResponseBean<BasePageResponseBean<MyCoinResponseBean>>
}