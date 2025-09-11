package com.lcz.wanandroid_compose.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  10:01 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
abstract class BaseViewModel : ViewModel() {

    // 加载状态管理
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // 错误信息管理
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // 页面状态管理
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // 分页加载状态
    private val _pageState = MutableStateFlow(PageState())
    val pageState: StateFlow<PageState> = _pageState

    // 安全执行协程
    protected fun launch(
        showLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        try {
            if (showLoading) _loading.value = true
            block()
        } catch (e: Exception) {
            _error.value = e.parseError()
            _uiState.value = UiState.Error(e)
        } finally {
            if (showLoading) _loading.value = false
        }
    }

    // 页面状态定义
    sealed class UiState {
        object Loading : UiState()
        object Empty : UiState()
        class Error(val exception: Throwable) : UiState()
        object Success : UiState()
    }

    // 分页状态
    data class PageState(
        val currentPage: Int = 1,
        val hasMore: Boolean = false
    )
}

// 错误信息解析扩展函数
private fun Throwable.parseError(): String = when (this) {
    is java.net.UnknownHostException -> "网络连接异常"
    is java.net.SocketTimeoutException -> "请求超时"
    else -> message ?: "未知错误"
}