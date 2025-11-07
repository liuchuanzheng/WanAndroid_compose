package com.lcz.wanandroid_compose.module.main.project.viewmodel

import com.lcz.wanandroid_compose.base.BaseViewModel
import com.lcz.wanandroid_compose.data.repository.CommonRepository
import com.lcz.wanandroid_compose.module.main.home.data.ProjectTitle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 作者:     刘传政
 * 创建时间:  9:39 2025/10/16
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
class ProjectWidgetViewModel(private val isPreview: Boolean = false) : BaseViewModel() {
    private val _projectTitleList = MutableStateFlow<List<ProjectTitle>>(emptyList())
    val projectTitleList = _projectTitleList.asStateFlow()

    init {
        if (isPreview) {
            _projectTitleList.value = listOf(
                ProjectTitle(name = "项目分类1"),
                ProjectTitle(name = "项目分类2"),
                ProjectTitle(name = "项目分类3"),
                ProjectTitle(name = "项目分类4"),
                ProjectTitle(name = "项目分类5"),
                ProjectTitle(name = "项目分类6"),
                ProjectTitle(name = "项目分类7"),
                ProjectTitle(name = "项目分类8"),
                ProjectTitle(name = "项目分类9"),
                ProjectTitle(name = "项目分类10"),
            )
        }
    }

    fun netGetProjectTitleList() {
        safeLaunch(
            showLoadingDialog = false,
            tryBlock = {
                var projectTitleListResponseBean = CommonRepository.getProjectTitleList()
                checkResponseCode(projectTitleListResponseBean, okBlock = {
                    it?.let {
                        _projectTitleList.value = it
                    }
                })

            })
    }


}