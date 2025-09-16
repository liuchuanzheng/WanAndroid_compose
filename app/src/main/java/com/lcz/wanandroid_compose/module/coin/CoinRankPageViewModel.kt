package com.lcz.wanandroid_compose.module.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.main.mine.bean.MyCoinResponseBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 作者:     刘传政
 * 创建时间:  16:22 2025/9/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class CoinRankPageViewModel : BaseViewModel() {
    private val _coinRankList = MutableStateFlow<List<MyCoinResponseBean?>?>(emptyList())
    val coinRankList = _coinRankList.asStateFlow()
    private val _myCoin = MutableStateFlow(MyCoinResponseBean())
    val myCoin = _myCoin.asStateFlow()
    fun netGetCoinRankList(isRefresh: Boolean) {
        safeLaunchWithPage(
            showLoadingDialog = false,
            tryBlock = {
                var rankList = CommonRepository.getCoinRankList(it, _pageState.value.PAGE_SIZE)
                rankList
            },
            onSuccessBlock = {

                if (isRefresh) {
                    _coinRankList.value = it
                } else {
                    _coinRankList.value = _coinRankList.value?.plus(it)
                }
            },
            isRefresh = isRefresh,
        )
    }

    fun netGetMyCoin() {
        safeLaunch(
            tryBlock = {
                var responseBean = CommonRepository.getMyCoin()
                checkResponseCode(
                    responseBean,
                ) {
                    it?.let {
                        _myCoin.value = it
                    }
                }
            }
        )
    }
}