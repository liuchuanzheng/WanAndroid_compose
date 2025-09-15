package com.lcz.wanandroid_compose.module.coin

import android.R.attr.version
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.coin.data.MyCoinHistoryResponseBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 作者:     刘传政
 * 创建时间:  11:13 2025/9/15
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class MyCoinHistoryPageViewModel : BaseViewModel() {
    private val _coinList = MutableStateFlow<List<MyCoinHistoryResponseBean?>?>(emptyList())
    val coinList = _coinList.asStateFlow()


    fun netGetMyCoinHistory(isRefresh: Boolean) {
        safeLaunchWithPage(
            showLoadingDialog = false,
            tryBlock = {
                CommonRepository.getMyCoinHistory(it, _pageState.value.PAGE_SIZE)
            },
            onSuccessBlock = {

                if (isRefresh){
                    _coinList.value = it
                } else {
                    _coinList.value = _coinList.value?.plus(it)
                }
            },
            isRefresh = isRefresh,
        )
    }
}