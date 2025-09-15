package com.lcz.wanandroid_compose.module.main.mine.viewmodel

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 作者:     刘传政
 * 创建时间:  9:38 2025/9/15
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class MineWidgetViewModel : BaseViewModel() {
    private val _coinCount = MutableStateFlow(0)
    val coinCount: StateFlow<Int> = _coinCount

    fun netGetMyCoin() {
        safeLaunch(
            tryBlock = {
                var responseBean = CommonRepository.getMyCoin()
                checkResponseCode(
                    responseBean,
                ) {
                    it?.let {
                        _coinCount.value = it.coinCount ?: 0
                    }
                }
            }
        )
    }
}