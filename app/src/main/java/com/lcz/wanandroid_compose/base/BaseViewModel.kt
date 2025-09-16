package com.lcz.wanandroid_compose.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcz.wanandroid_compose.constant.MyConstant
import com.lcz.wanandroid_compose.util.DataWrapper
import com.lcz.wanandroid_compose.util.LogUtil
import com.lcz.wanandroid_compose.util.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 作者:     刘传政
 * 创建时间:  10:01 2025/9/11
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
abstract class BaseViewModel : ViewModel() {

    // loading框状态管理，一般就是ui层监听，showDialog
    protected val _showLoadingDialog = MutableStateFlow(false)
    val showLoadingDialog: StateFlow<Boolean> = _showLoadingDialog
    //当连续发送相同值时会被合并,即使对象是新建的。要让相同值不合并，可以通过添加版本号或时间戳包装数据
    protected var version = 0 // 版本计数器
    // 错误信息管理,一般就是ui层监听，toast提示错误信息
    //MutableStateFlow我记得是用来保存状态，并且每次更新都会把最新的值发送给收集者。它必须有初始值，而且每次emit的时候，如果值和当前的一样，可能不会触发更新。这适合用来表示UI的状态，比如加载中的状态。
    //
    //MutableSharedFlow则不同，它更像是一个事件流，可以没有初始值。可以配置额外的参数，比如replay，让新订阅者收到之前发送的几个值。它适合用来处理一次性事件，比如显示Toast或者导航事件，这些事件不需要保留状态，只需要处理一次。
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error


    // 分页状态管理
    protected val _pageState = MutableStateFlow(PageState())
    val pageState = _pageState.asStateFlow()
    init {
        viewModelScope.launch {
            _error
                .collect {
                ToastUtil.showShort(it)
            }
        }
    }

    // 安全执行协程
    protected fun safeLaunch(
        showLoadingDialog: Boolean = true,
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Exception, String) -> Unit = { e, msg -> },
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ) = viewModelScope.launch {
        try {
            if (showLoadingDialog) _showLoadingDialog.value = true
            tryBlock()
        } catch (e: Exception) {
            _error.emit(e.parseError())
            catchBlock(e, e.parseError())
            LogUtil.e(
                tr = e,
                tag = "BaseViewModel",
                msg = e.message ?: "未知错误"
            )
        } finally {
            if (showLoadingDialog) _showLoadingDialog.value = false
            finallyBlock()
        }
    }

    fun <T> safeLaunchWithPage(
        showLoadingDialog: Boolean = true,
        isRefresh: Boolean,//是否是下拉刷新
        tryBlock: suspend CoroutineScope.(Int) -> BaseNetResponseBean<BasePageResponseBean<T>>,
        catchBlock: suspend CoroutineScope.(Exception, String) -> Unit = { e, msg -> },
        finallyBlock: suspend CoroutineScope.() -> Unit = {},
        onSuccessBlock: (List<T>) -> Unit = {},
    ) = viewModelScope.launch {
        try {
            if (showLoadingDialog) _showLoadingDialog.value = true
            if (isRefresh) {
                if (_pageState.value.isRefreshing) {
                    return@launch
                }
                _pageState.value = _pageState.value.copy(isRefreshing = true)
            } else {
                if (_pageState.value.isLoadingMore || !_pageState.value.hasMore || _pageState.value.isRefreshing) {
                    return@launch
                }
                _pageState.value = _pageState.value.copy(isLoadingMore = true)
            }
            var pageNo = _pageState.value.currentPage //这是临时页码。是否更新pageState的页面要看请求结果是否符合要求。
            if (isRefresh) {
                pageNo = 1
            } else {
                pageNo++
            }
            var responseBean = tryBlock(pageNo)
            checkResponseCode(
                responseBean,
            ) {
                it?.let {
                    if (it.datas?.isNotEmpty() == true) {
                        //列表不空
                        if (isRefresh) {
                            _pageState.value = _pageState.value.copy(
                                currentPage = pageNo,
                                hasMore = true,
                            )
                        } else {
                            _pageState.value = _pageState.value.copy(
                                currentPage = pageNo,
                            )
                        }
                        onSuccessBlock(it.datas)
                    } else {
                        if (isRefresh) {

                        } else {
                            _pageState.value = _pageState.value.copy(
                                hasMore = false,
                            )
                        }
                    }


                }
            }
        } catch (e: Exception) {
            _error.emit(e.parseError())
            catchBlock(e, e.parseError())
            LogUtil.e(
                tr = e,
                tag = "BaseViewModel",
                msg = e.message ?: "未知错误"
            )
        } finally {
            if (showLoadingDialog) _showLoadingDialog.value = false
            _pageState.value = _pageState.value.copy(isRefreshing = false, isLoadingMore = false)
            finallyBlock()
        }
    }



    // 分页状态管理
    data class PageState(
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true,
        val currentPage: Int = 1,
        val PAGE_SIZE: Int = 10,
    )

    fun <T> checkResponseCode(
        responseBean: BaseNetResponseBean<T>,
        errorBlock: () -> Unit = {},
        okBlock: (T?) -> Unit
    ) {
        if (responseBean.errorCode == MyConstant.Net.CODE_SUCCESS) {
            okBlock.invoke(responseBean.data)
        } else {
            errorBlock.invoke()
        }
    }

}

// 错误信息解析扩展函数
private fun Throwable.parseError(): String = when (this) {
    is UnknownHostException -> "网络连接异常"
    is SocketTimeoutException -> "请求超时"
    else -> message ?: "未知错误"
}