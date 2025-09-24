package com.lcz.wanandroid_compose.module.search.viewmodel

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.data.repository.SearchHistoryManager
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.search.data.HotSearch
import com.lcz.wanandroid_compose.util.SPUtil.Companion.init
import com.lcz.wanandroid_compose.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.plus

/**
 * 作者:     刘传政
 * 创建时间:  10:14 2025/9/22
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class SearchPageViewModel(private val isPreview: Boolean = false) : BaseViewModel() {
    private val _hotSearchList = MutableStateFlow<List<HotSearch>>(emptyList())
    val hotSearchList = _hotSearchList
    private val _searchHistoryList = MutableStateFlow(emptyList<String>())
    val searchHistoryList = _searchHistoryList.asStateFlow()
    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()
    private val _articleList = MutableStateFlow<List<Article>>(emptyList())
    val articleList = _articleList.asStateFlow()
    private val _showResult = MutableStateFlow(false)
    val showResult = _showResult.asStateFlow()

    init {
        //模拟数据
//        _hotSearchList.value = listOf(
//            HotSearch(1, "111", "面试", 1, 1),
//            HotSearch(2, "222", "Android", 2, 2),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//            HotSearch(3, "333", "Kotlin", 3, 3),
//        )
        if (!isPreview) {
            spGetSearchHistory()
        } else {
            // 预览模式使用模拟数据
            _searchHistoryList.value = listOf("Android", "Kotlin", "Compose")
            _hotSearchList.value = listOf(
                HotSearch(1, "", "热门搜索1", 1, 1),
                HotSearch(2, "", "热门搜索2", 2, 1)
            )
        }
    }

    /** 输入框文本改变 */
    fun inputTextChange(text: String) {
        _inputText.value = text
    }

    /** 切换搜索结果显示状态 */
    fun updateShowResult(showResult: Boolean) {
        _showResult.value = showResult
    }

    /** 获取热门搜索数据 */
    fun netGetHotSearchList() {
        safeLaunch(tryBlock = {
            val response = CommonRepository.getHotSearchList()
            checkResponseCode(
                response
            ) {
                _hotSearchList.value = it ?: emptyList()
            }
        })

    }

    private fun netSearchDataByKey(isRefresh: Boolean, key: String) {
        safeLaunchWithPage(
            showLoadingDialog = false,
            startPage = 0,
            tryBlock = {
                val responseBean = CommonRepository.searchDataByKey(it, _pageState.value.PAGE_SIZE, key)
                responseBean
            },
            onSuccessBlock = {
                _showResult.value = true
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

    fun spGetSearchHistory() {
        _searchHistoryList.value = SearchHistoryManager.getInstance().getSearchHistory()
    }

    fun netSearch(isRefresh: Boolean, searchName: String) {
        if (searchName.isNullOrEmpty()) {
            ToastUtil.showShort("请输入搜索内容")
            return
        }
        //更新输入框文本
        inputTextChange(searchName)
        //更新搜索历史
        spUpdateSearchHistory(searchName)
        //网络请求
        netSearchDataByKey(isRefresh, searchName)
    }

    fun spUpdateSearchHistory(searchName: String) {
        //当前的搜索历史
        var searchHistory = SearchHistoryManager.getInstance().getSearchHistory().toMutableList()
        //判断是否存在
        if (searchHistory.contains(searchName)) {
            //存在则删除
            searchHistory.remove(searchName)
        }
        //添加到最前面
        searchHistory.add(0, searchName)
        //更新搜索历史
        SearchHistoryManager.getInstance().saveSearchHistory(searchHistory)
        //更新flow
        spGetSearchHistory()
    }
}