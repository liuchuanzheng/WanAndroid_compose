package com.lcz.wanandroid_compose.module.main.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.main.home.data.Article
import com.lcz.wanandroid_compose.module.main.home.widget.ProjectChildWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.plus

/**
 * 作者:     刘传政
 * 创建时间:  9:39 2025/10/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
// ViewModel工厂（用于注入分类ID）
class PageViewModelFactory(private val categoryId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectChildWidgetViewModel::class.java)) {
            return ProjectChildWidgetViewModel(categoryId = categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProjectChildWidgetViewModel(private val isPreview: Boolean = false, private val categoryId: Int) :
    BaseViewModel() {
    private val _projectList = MutableStateFlow<List<Article>>(emptyList())
    val projectList = _projectList.asStateFlow()

    init {
        if (isPreview) {
            //模拟预览数据
        }
    }

    /** 获取项目列表分页数据 */
    fun netGetProjectPageList(
        isRefresh: Boolean,
    ) {
        safeLaunchWithPage(
            showLoadingDialog = false,
            tryBlock = {
                CommonRepository.getProjectPageList(it, _pageState.value.PAGE_SIZE, categoryId)
            },
            onSuccessBlock = {

                if (isRefresh) {
                    _projectList.value = it
                } else {
                    _projectList.value = _projectList.value.plus(it)
                }
            },
            isRefresh = isRefresh,
        )

    }


}
