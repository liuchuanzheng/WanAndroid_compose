package com.lcz.wanandroid_compose.module.main.home.viewmodel

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.main.home.data.BannerResponseBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 作者:     刘传政
 * 创建时间:  11:13 2025/9/15
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class HomeWidgetViewModel : BaseViewModel() {
    private val _articleList = MutableStateFlow<List<Article>>(emptyList())
    val articleList = _articleList.asStateFlow()
    private val _bannerList = MutableStateFlow<List<BannerResponseBean.BannerResponseBeanItem>>(emptyList())
    val bannerList = _bannerList.asStateFlow()

    fun netGetBanner() {
        safeLaunch(
            showLoadingDialog = false,
            tryBlock = {
                var responseBean = CommonRepository.getBanner()
                checkResponseCode(responseBean, okBlock = {
                    it?.let {
                        _bannerList.value = it
                    }
                })

            })
    }

    fun netGetTopList() {
        safeLaunch(
            showLoadingDialog = false,
            tryBlock = {
                var topArticleListResponseBean = CommonRepository.getArticleTopList()
                checkResponseCode(topArticleListResponseBean, okBlock = {
                    var topArticles = it?.map { article ->
                        article.isTop = true
                        article
                    } ?: emptyList()
                    var nonTopArticles = _articleList.value.filter {
                        it.isTop.not()
                    }
                    _articleList.value = topArticles + nonTopArticles
                }, errorBlock = {

                })

            })
    }

    fun netGetArticleList(isRefresh: Boolean) {
        if (isRefresh) {
            netGetTopList()
        }
        safeLaunchWithPage(
            showLoadingDialog = false,
            tryBlock = {
                val responseBean = CommonRepository.getArticleList(it, _pageState.value.PAGE_SIZE)
                responseBean
            },
            onSuccessBlock = {

                if (isRefresh) {
                    var hasTopArticle = _articleList.value.any {
                        it.isTop
                    }
                    if (!hasTopArticle) {
                        _articleList.value = it
                    } else {
                        _articleList.value = _articleList.value + it
                    }
                } else {
                    _articleList.value = _articleList.value.plus(it)
                }
            },
            isRefresh = isRefresh,
        )
    }
}