package com.lcz.wanandroid_compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 作者:     刘传政
 * 创建时间:  16:18 2025/9/9
 * QQ:      1052374416
 * 电话:     18501231486
 * 描述:
 */
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    items: List<BottomBarItem>,
    currentSelectIndex: Int,
    onSelectListener: (Int) -> Unit
) {
    Column {
        // 顶部边框
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
//                .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                //            .shadow(elevation = 8.dp)
//                            .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 5.dp)
                .navigationBarsPadding()
        ) {
            items.forEachIndexed { index, it ->
                var selected = currentSelectIndex == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onSelectListener(index)
                        }
                ) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it.text,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

class BottomBarItem(
    val icon: ImageVector,
    val text: String
)
