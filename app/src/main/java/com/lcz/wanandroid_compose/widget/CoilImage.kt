package com.lcz.wanandroid_compose.widget

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State

/**
 * 作者:     刘传政
 * 创建时间:  16:31 2025/8/27
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun CoilImage(
    model: Any?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    contentDescription: String? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality

) {
    AsyncImage(
        model = model,
        modifier = modifier,
        contentDescription = contentDescription,
//        placeholder = placeholder ?: painterResource(id = (R.drawable.ic_menu_report_image)),
        placeholder = placeholder ?: rememberVectorPainter(Icons.Default.HourglassEmpty),
        error = error,
        fallback = fallback,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality

    )
}
