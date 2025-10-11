package com.lcz.wanandroid_compose.module.ticktok.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.math.Quantiles.scale
import com.lcz.wanandroid_compose.module.search.page.SearchResult
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.widget.CoilImage
import kotlinx.coroutines.launch

/**
 * 作者:     刘传政
 * 创建时间:  10:22 2025/10/10
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun BoxScope.Cover(
    videoBean: VideoBean,
    onLikeChange: (Boolean) -> Unit,
    onCollectChange: (Boolean) -> Unit,
    onFollowChange: (Boolean) -> Unit,
    onShareClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .align(Alignment.BottomStart),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 10.dp
                    )
            ) {
                Text(text = videoBean.author, color = Color.White)
                Text(text = videoBean.title, color = Color.White)
            }
            Cover_right(
                videoBean,
                onLikeChange,
                onCollectChange,
                onFollowChange,
                onShareClick,
            )

        }

    }
}

@Composable
fun Cover_right(
    videoBean: VideoBean,
    onLikeChange: (Boolean) -> Unit,
    onCollectChange: (Boolean) -> Unit,
    onFollowChange: (Boolean) -> Unit,
    onShareClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 10.dp)) {
        //头像
        Box {
            Column {
                CoilImage(
                    model = videoBean.authorIconUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color.White, CircleShape)
                        .clip(CircleShape)

                )
                Spacer(modifier = Modifier.height(9.dp))
            }
            this@Column.AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = !videoBean.isFollow,
                enter = scaleIn(tween(500)) + fadeIn(tween(500)),
                exit = scaleOut(tween(500)) + fadeOut(tween(500))
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = Color.Red)
                        .clickable {
                            onFollowChange(!videoBean.isFollow)
                        }

                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        val scale = remember {
            Animatable(1f)
        }
        val scope = rememberCoroutineScope()
        //点赞
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
            onLikeChange(!videoBean.isLiked)
            scope.launch {
                scale.animateTo(
                    targetValue = 1.3f,
                    animationSpec =  spring(
                        dampingRatio = 0.4f,
                        stiffness = 1800f
                    )
                )
                // 然后弹性恢复到原大小
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.3f,
                        stiffness = 800f
                    )
                )

            }


        }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = if (videoBean.isLiked) Color.Red else Color.White,
                modifier = Modifier.size(35.dp)
                    .scale(scale.value) // 应用缩放动画
            )
            Text(text = videoBean.likeCount.toString(), color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        //收藏
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
            onCollectChange(!videoBean.isCollect)
        }) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (videoBean.isCollect) Color.Yellow else Color.White,
                modifier = Modifier.size(35.dp)
            )
            Text(text = videoBean.collectCount.toString(), color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        //分享
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
            onShareClick()
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Text(text = videoBean.shareCount.toString(), color = Color.White, fontSize = 12.sp)
        }

    }
}