package com.lcz.wanandroid_compose.module.demo.sign.page

import android.Manifest
import android.R.attr.strokeWidth
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.ui.theme.WanAndroid_composeTheme
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SignPage(paramsBean: AppRoutePath.Sign) {
    var context = LocalContext.current
    // 签名路径
    var path by remember { mutableStateOf(Path()) }
    // 路径历史记录（支持撤销）
    val paths = remember { mutableStateListOf<Path>() }
    // 当前画笔颜色
    var color by remember { mutableStateOf(Color.Black) }
    // 当前画笔宽度
    var strokeWidth by remember { mutableStateOf(5f) }
    // 当前触摸位置
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    // 在SignPage组件中添加截图状态
    var canvasBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 权限获取成功
            scope.launch { saveSignature(context, canvasBitmap) }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // 颜色选择
        ColorPicker { color = it }

        Spacer(modifier = Modifier.height(16.dp))

        // 画笔粗细选择
        Slider(
            value = strokeWidth,
            onValueChange = { strokeWidth = it },
            valueRange = 1f..20f,
            steps = 10,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        //预览线条
        Canvas(
            modifier = Modifier
                .size(100.dp, 100.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            // 绘制预览背景
            drawRect(Color.LightGray)

            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(10.dp.toPx(), 10.dp.toPx()),
                end = Offset(size.width - 10.dp.toPx(), 10.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(10.dp.toPx(), 30.dp.toPx()),
                end = Offset(size.width - 10.dp.toPx(), 30.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(10.dp.toPx(), 50.dp.toPx()),
                end = Offset(size.width - 10.dp.toPx(), 50.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(25.dp.toPx(), 60.dp.toPx()),
                end = Offset(25.dp.toPx(), size.height - 10.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(50.dp.toPx(), 60.dp.toPx()),
                end = Offset(50.dp.toPx(), size.height - 10.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // 绘制示例线条
            drawLine(
                color = color,
                start = Offset(75.dp.toPx(), 60.dp.toPx()),
                end = Offset(75.dp.toPx(), size.height - 10.dp.toPx()),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )


        }
        Spacer(modifier = Modifier.height(16.dp))

        // 签名区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.LightGray)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                // 获取 Canvas 实际尺寸
                                val width: Int = size.width
                                val height = size.height
                                // 限制坐标在画布范围内
                                val clampedOffset = Offset(
                                    x = offset.x.coerceIn(0f, width.toFloat()),
                                    y = offset.y.coerceIn(0f, height.toFloat())
                                )
                                path = Path().apply {
                                    moveTo(clampedOffset.x, clampedOffset.y)
                                }
                                currentPosition = clampedOffset
                            },
                            onDrag = { change, _ ->
                                // 限制坐标在画布范围内
                                val clampedX = change.position.x.coerceIn(0f, size.width.toFloat())
                                val clampedY = change.position.y.coerceIn(0f, size.height.toFloat())
                                val clampedPosition = Offset(clampedX, clampedY)

                                val newPath = Path().apply {
                                    addPath(path)
                                    lineTo(clampedPosition.x, clampedPosition.y)
                                }
                                path = newPath  // 关键：创建新Path对象触发重组
                                currentPosition = clampedPosition
                            },
                            onDragEnd = {
                                paths.add(path)
                                path = Path()
                                currentPosition = Offset.Unspecified
                            }
                        )
                    }
                    .drawWithContent {
                        drawContent()

                        // 将绘制内容转换为ImageBitmap（包含背景和所有路径）
                        canvasBitmap = ImageBitmap(size.width.toInt(), size.height.toInt()).apply {
                            val targetCanvas = Canvas(this)
                            // 1. 绘制背景（与Box背景一致：LightGray）
                            targetCanvas.drawRect(
                                rect = Rect(0f, 0f, size.width, size.height),
                                paint = Paint().apply { this.color = Color.LightGray }
                            )
                            // 2. 绘制历史路径
                            paths.forEach { path ->
                                targetCanvas.drawPath(
                                    path = path,
                                    paint = Paint().apply {
                                        this.color = color
                                        this.style = PaintingStyle.Stroke
                                        this.strokeWidth = strokeWidth
                                        this.strokeCap = StrokeCap.Round
                                    }
                                )
                            }
                            // 3. 绘制当前路径
                            targetCanvas.drawPath(
                                path = path,
                                paint = Paint().apply {
                                    this.color = color
                                    this.style = PaintingStyle.Stroke
                                    this.strokeWidth = strokeWidth
                                    this.strokeCap = StrokeCap.Round
                                }
                            )
                        }


                    }

            ) {
                // 绘制历史路径
                paths.forEach {
                    drawPath(
                        path = it,
                        color = color,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                // 绘制当前路径
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 操作按钮
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (paths.isNotEmpty()) paths.removeAt(paths.lastIndex)
            }) {
                Text("撤销")
            }

            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    scope.launch { saveSignature(context, canvasBitmap) }
                } else {
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }) {
                Text("保存签名")
            }
        }
    }
}

@Composable
private fun ColorPicker(onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Black, Color.Red, Color.Blue,
        Color.Green, Color.Magenta, Color.Cyan
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("颜色选择:", modifier = Modifier.padding(end = 8.dp))
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .padding(2.dp)
                    .background(color)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

// 保存签名函数
private fun saveSignature(context: Context, canvasBitmap: ImageBitmap?) {
    if (canvasBitmap == null) {
        return
    }

    // 创建Bitmap
    val bitmap = canvasBitmap.asAndroidBitmap()

    // 保存到相册
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "signature_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Signatures")
        }
    }

    try {
        context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )?.let { uri ->
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, "保存失败: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        bitmap.recycle()
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun SignPagePreview() {
    WanAndroid_composeTheme {
        SignPage(AppRoutePath.Sign())
    }
}