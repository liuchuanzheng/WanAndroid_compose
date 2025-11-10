package com.lcz.wanandroid_compose.module.customerservice.page

import android.R.attr.textColor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcz.wanandroid_compose.module.main.shop.widget.ShopWidget
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.util.ToastUtil
import com.lcz.wanandroid_compose.widget.CoilImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 客服聊天页面
 */
@Composable
fun CustomerServicePage(paramsBean: AppRoutePath.CustomerService) {
    val context = LocalContext.current
    var messageText by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var showMorePicker by remember { mutableStateOf(false) }
    val messages = remember { mutableStateOf(listOf<ChatMessage>()) }
    val lazyListState = rememberLazyListState()

    // 添加一些初始消息
    LaunchedEffect(Unit) {
        messages.value = listOf(
            ChatMessage(
                id = 1,
                content = "您好，欢迎使用客服服务！请问有什么可以帮助您的？",
                isUser = false,
                timestamp = System.currentTimeMillis() - 300000,
                userName = "客服",
            ),
            ChatMessage(
                id = 2,
                content = "我想知道如何注册账号",
                isUser = true,
                timestamp = System.currentTimeMillis() - 200000,
                userName = "我",
            ),
            ChatMessage(
                id = 3,
                content = "你猜猜",
                isUser = false,
                timestamp = System.currentTimeMillis() - 100000,
                userName = "客服",
            ),
        )
    }

    // 自动滚动到底部
    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.value.size - 1)
        }
    }
    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0
    val focusManager = LocalFocusManager.current // 添加焦点管理器

    // 在需要收起键盘的地方调用
    fun hideKeyboard() {
        focusManager.clearFocus()
    }
    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            // 键盘弹出时的操作
            lazyListState.animateScrollToItem(messages.value.size - 1)
            showEmojiPicker = false
            showMorePicker = false
        } else {
            // 键盘收起时的操作
        }
    }
    // 面板高度动画
    val showEmojiPickerHeight by animateDpAsState(
        targetValue = if (showEmojiPicker) 200.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "showEmojiPickerHeight",
    )
    val showMorePickerHeight by animateDpAsState(
        targetValue = if (showMorePicker) 150.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "showMorePickerHeight",
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding(),
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            color = Color.White,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "客服中心",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        // 聊天消息列表
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F4FB))
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            //触摸列表区域，收起键盘等
                            hideKeyboard()
                            showMorePicker = false
                            showEmojiPicker = false
                            awaitRelease()
                        },
                    )
                }
                .padding(horizontal = 8.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(messages.value) { message ->
                    ChatMessageBubble(message = message)
                }
            }
        }
        // 输入区域
        Column(
            modifier = Modifier
                .background(Color.White),
        ) {
            // 输入区域
            Column(
                modifier = Modifier.background(Color.White),
            ) {
                // 输入框和按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    Spacer(modifier = Modifier.width(8.dp))

                    // 文字输入框
                    BasicTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 40.dp, max = 120.dp) // 设置高度范围
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {

                                }
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        textStyle = TextStyle(fontSize = 16.sp),
                        maxLines = 4, // 设置最大行数
                        cursorBrush = SolidColor(Color.Green), // 设置光标颜色
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                if (messageText.isEmpty()) {
                                    Text(
                                        text = "请输入消息...",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    // 表情按钮
                    IconButton(
                        onClick = {
                            showEmojiPicker = !showEmojiPicker
                            if (showEmojiPicker) {
                                hideKeyboard()
                                showMorePicker = false
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEmotions,
                            contentDescription = "表情",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    AnimatedContent(
                        targetState = messageText.isNotBlank(),
                    ) {
                        if (it) {
                            // 发送按钮
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color(0xff07C160))
                                    .clickable {
                                        if (messageText.isNotBlank()) {
                                            val newMessage = ChatMessage(
                                                id = messages.value.size + 1,
                                                content = messageText,
                                                isUser = true,
                                                timestamp = System.currentTimeMillis(),
                                                userName = "我",
                                            )
                                            messages.value = messages.value + newMessage
                                            messageText = ""

                                            // 模拟客服回复
                                            simulateCustomerReply(messages)
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 3.dp),
                            ) {
                                Text(
                                    text = "发送",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    showMorePicker = !showMorePicker
                                    if (showMorePicker) {
                                        showEmojiPicker = false
                                        hideKeyboard()
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "拍视频",
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        }
                    }


                }
                if (showEmojiPickerHeight > 0.dp) {
                    // 表情选择器
                    EmojiPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(showEmojiPickerHeight),
                    ) { emoji ->
                        messageText += emoji
                    }
                }
                if (showMorePickerHeight > 0.dp) {
                    // 更多选择器
                    MorePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(showMorePickerHeight),
                        onMorePickerSelected = {
                            when (it) {
                                0 -> {
                                    // 拍照片
                                    ToastUtil.showShort("拍照片")
                                }

                                1 -> {
                                    // 拍视频
                                    ToastUtil.showShort("拍视频")
                                }
                            }
                        },
                    )
                }

            }
        }
    }
}

@Composable
fun MorePicker(
    modifier: Modifier = Modifier,
    onMorePickerSelected: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4),
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onMorePickerSelected(0)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "拍照片",
                    modifier = Modifier.size(48.dp),
                )
                Text(text = "拍照片")
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onMorePickerSelected(1)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.VideoCameraFront,
                    contentDescription = "拍视频",
                    modifier = Modifier.size(48.dp),
                )
                Text(text = "拍视频")
            }
        }
    }

}

/**
 * 聊天消息气泡
 */
@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    val bubbleColor = if (isUser) Color(0xFF95EC69) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Row() {
            if (!isUser) {
                CoilImage(
                    model = "https://q4.itc.cn/images01/20250106/7dcdbbe94db8492b9ac7abae62628ce3.png",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column(
                modifier = Modifier
                    .padding(top = 8.dp),
                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            ) {
                Surface(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    shape = if (isUser) RoundedCornerShape(
                        10.dp,
                        0.dp,
                        10.dp,
                        10.dp,
                    )
                    else RoundedCornerShape(
                        0.dp,
                        10.dp,
                        10.dp,
                        10.dp,
                    ),
                    color = bubbleColor,
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        SelectionContainer() {
                            Text(
                                text = message.content,
                                color = Color.Black,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }

                Text(
                    text = formatTime(message.timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
            if (isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                CoilImage(
                    model = "https://img1.baidu.com/it/u=1221952588,3009131272&fm=253&app=138&f=JPEG?w=500&h=500",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            }
        }

    }
}

@Preview(
    backgroundColor = 0xFF888888,
    showBackground = true,
)
@Composable
fun ChatMessageBubblePreview() {
    Column() {
        ChatMessageBubble(
            message = ChatMessage(
                id = 1,
                content = "这是一条测试消息",
                isUser = true,
                timestamp = System.currentTimeMillis(),
                userName = "我",
            ),
        )
        ChatMessageBubble(
            message = ChatMessage(
                id = 1,
                content = "这是一条测试消息",
                isUser = false,
                timestamp = System.currentTimeMillis(),
                userName = "客服",
            ),
        )
    }

}

/**
 * 表情选择器
 */
@Composable
fun EmojiPicker(modifier: Modifier, onEmojiSelected: (String) -> Unit) {
    val emojis = listOf(
        "😀", "😂", "😍", "😎", "👍",
        "❤️", "🙏", "🎉", "🔥", "⭐",
        "😊", "🤔", "😭", "👋", "🎈",
        "🍎", "⚽", "🚗", "💰", "📱",
        "🎁", "✈️", "⌚", "🌙", "🌈",
        "🌍", "🍔", "☕", "🎸", "🎮",
    )

    Surface(
        modifier = modifier,
        color = Color.White,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(emojis.size) { index ->
                Text(

                    text = emojis[index],
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier


                        .aspectRatio(4 / 3f)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .wrapContentSize(Alignment.Center)  // 添加包裹内容居中
                        .clickable { onEmojiSelected(emojis[index]) },
                )
            }
        }

    }
}

/**
 * 模拟客服回复
 */
private fun simulateCustomerReply(messages: MutableState<List<ChatMessage>>) {
    // 延迟2秒后回复
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(
        {
            val replies = listOf(
                "好的，我了解了您的问题。",
                "请稍等，我正在为您查询相关信息。",
                "这个问题我们可以这样解决：",
                "感谢您的反馈，我们会尽快处理。",
                "请问您还有其他问题吗？",
            )
            val randomReply = replies.random()

            val replyMessage = ChatMessage(
                id = messages.value.size + 1,
                content = randomReply,
                isUser = false,
                timestamp = System.currentTimeMillis() + 1000,
                userName = "客服",
            )
            messages.value = messages.value + replyMessage
        },
        2000,
    )
}

/**
 * 格式化时间
 */
private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

/**
 * 聊天消息数据类
 */
data class ChatMessage(
    val id: Int,
    val content: String,
    val isUser: Boolean, // true表示用户发送，false表示客服发送
    val timestamp: Long,
    val userName: String,
)

@Preview
@Composable
fun CustomerServicePagePreview() {
    CustomerServicePage(AppRoutePath.CustomerService())
}
