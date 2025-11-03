package com.lcz.wanandroid_compose.navigation

import com.lcz.wanandroid_compose.util.LogUtil

/**
 * 作者:     刘传政
 * 创建时间:  11:01 2025/10/14
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:   所有的跳转都通过这个类来跳转，方便管理。而且通过点击就知道来源
 */
object PageJumpManager {
    fun navigateToTest(paramsBean: AppRoutePath.Test) {
        simpleNavigate(paramsBean)
    }


    fun navigateToTest2(paramsBean: AppRoutePath.Test2) {
        simpleNavigate(paramsBean)
    }

    fun navigateToLogin(paramsBean: AppRoutePath.Login) {
        simpleNavigate(paramsBean)
    }

    fun navigateToMyCoinHistory(paramsBean: AppRoutePath.MyCoinHistory) {
        simpleNavigate(paramsBean)
    }

    fun navigateToCoinRank(paramsBean: AppRoutePath.CoinRank) {
        simpleNavigate(paramsBean)
    }

    fun navigateToSetting(paramsBean: AppRoutePath.Setting) {
        simpleNavigate(paramsBean)
    }

    fun navigateToSearch(paramsBean: AppRoutePath.Search) {
        simpleNavigate(paramsBean)
    }

    fun navigateToDemo(paramsBean: AppRoutePath.Demo) {
        simpleNavigate(paramsBean)
    }

    fun navigateToSign(paramsBean: AppRoutePath.Sign) {
        simpleNavigate(paramsBean)
    }

    fun navigateToSliderDemo(paramsBean: AppRoutePath.SliderDemo) {
        simpleNavigate(paramsBean)
    }

    fun navigateToTickTok(paramsBean: AppRoutePath.TickTok) {
        simpleNavigate(paramsBean)
    }

    fun navigateToTickTokProgressBar(paramsBean: AppRoutePath.TickTokProgressBar) {
        simpleNavigate(paramsBean)
    }
    fun navigateToNetCache(paramsBean: AppRoutePath.NetCache) {
        simpleNavigate(paramsBean)
    }
    fun navigateToWeb(paramsBean: AppRoutePath.Web) {
        simpleNavigate(paramsBean)
    }
    fun navigateToNestedScrollDemo(paramsBean: AppRoutePath.NestedScrollDemo) {
        simpleNavigate(paramsBean)
    }
}

private fun <T : Any> simpleNavigate(route: T) {
    globalNavController?.navigate(route)
    LogUtil.i(TAG, "导航传递参数:${route}")
}