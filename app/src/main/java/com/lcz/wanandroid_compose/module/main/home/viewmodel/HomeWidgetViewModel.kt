package com.lcz.wanandroid_compose.module.main.home.viewmodel

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.main.home.data.Article
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


    fun netGetArticleList(isRefresh: Boolean) {
        //先处理置顶文章
        if (isRefresh) {
            safeLaunch(
                showLoadingDialog = false,
                tryBlock = {
                    var topArticleListResponseBean = CommonRepository.getArticleTopList()
                    checkResponseCode(topArticleListResponseBean, okBlock = {
                        var topArticles = it?.map { article ->
                            article.isTop = true
                            article
                        }
                        netGetArticleList2(isRefresh, topArticles)
                    }, errorBlock = {
                        netGetArticleList2(isRefresh, null)
                    })

                })
        } else {
            netGetArticleList2(isRefresh, null)
        }

    }

    private fun netGetArticleList2(isRefresh: Boolean, topArticles: List<Article>?) {
        safeLaunchWithPage(
            showLoadingDialog = false,
            tryBlock = {
                val responseBean = CommonRepository.getArticleList(it, _pageState.value.PAGE_SIZE)
                responseBean
            },
            onSuccessBlock = {

                if (isRefresh) {

                    if (topArticles.isNullOrEmpty()) {
                        _articleList.value = it
                    } else {
                        _articleList.value = topArticles + it
                    }
                } else {
                    _articleList.value = _articleList.value.plus(it)
                }
            },
            isRefresh = isRefresh,
        )
    }
}