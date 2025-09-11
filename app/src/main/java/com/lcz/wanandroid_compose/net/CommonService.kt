package com.lcz.wanandroid_compose.net

import com.lcz.wanandroid_compose.base.BaseNetResponseBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 作者:     刘传政
 * 创建时间:  14:13 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
interface CommonService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): BaseNetResponseBean<*>
}