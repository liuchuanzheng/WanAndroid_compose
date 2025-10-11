package com.lcz.wanandroid_compose.module.ticktok.widget

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.module.ticktok.bean.VideoBean
import com.lcz.wanandroid_compose.widget.CoilImage

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
            if (!videoBean.isFollow) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = Color.Red)
                        .align(Alignment.BottomCenter)
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
        //点赞
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
            onLikeChange(!videoBean.isLiked)
        }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = if (videoBean.isLiked) Color.Red else Color.White,
                modifier = Modifier.size(35.dp)
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