package com.smart.htu.screens.setting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.component.imageVectors.appLogo
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.math.sqrt
import kotlin.random.Random

// 1. 定义一个数据类来表示每个静态的Emoji粒子
private data class StaticEmoji(
    val x: Float, val y: Float, val size: Float, // 代表Emoji的像素大小
    val emoji: String
)

// 辅助类，用于圆盘填充算法
private data class Circle(var x: Float, var y: Float, val radius: Float)


@Composable
fun EmojiEasterEgg() {
    // --- 在这里自定义你的Emoji列表！ ---
    val emojiSets = remember {
        listOf(
            listOf("😂", "😍", "🤔", "😭", "😡", "👍", "🎉", "🚀", "💯", "❤️"),
            listOf("🍎", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🍈", "🍒", "🍑"),
            listOf("🐶", "🐱", "🐭", "🐰", "🦊", "🐻", "🐼", "🐨", "🦁", "🐯"),
            listOf("📚", "✏️", "📝", "🖌️", "📖", "📒", "✂️", "📏", "🔬", "🎓"),
            listOf("💻", "📱", "🖥️", "⌨️", "🖱️", "💾", "📷", "🎮", "🤖", "🔌"),
            listOf("✈️", "🗺️", "🧳", "🌍", "⛰️", "🏖️", "🗽", "🚌", "🚤", "🏕️"),
            listOf("🌸", "🌹", "🌻", "🌷", "🌼", "🌺", "🌿", "🌱", "🍃", "🌵"),
            listOf("🍰", "🧁", "🍩", "🍪", "🍫", "🍬", "🍦", "🍮", "🥐", "🍯"),
            listOf("⚽", "🏀", "🏈", "🎾", "🏐", "🏓", "🏸", "🥊", "🏒", "🏹"),
            listOf("🎨", "🖼️", "🎭", "🎬", "🎤", "🎸", "🎹", "🎻", "📽️", "🎨"),
            listOf("🌞", "🌙", "⭐", "☁️", "🌈", "⚡", "❄️", "🌪️", "☔", "🌊"),
            listOf("🚗", "🚀", "🚲", "🛵", "🚂", "🚁", "🛹", "🚤", "🚜", "🚑"),
            listOf("🦋", "🐞", "🐝", "🦗", "🕷️", "🦂", "🐜", "🦟", "🐌", "🕸️"),
            listOf("🎁", "🎉", "🎈", "🎀", "🎂", "🎄", "🎃", "🎗️", "🎟️", "🎫"),
            listOf("👻", "💀", "🎃", "🕸️", "🕷️", "👽", "👾", "🤖", "🧟‍♂️", "🧛‍♀️"),
            listOf("🍔", "🍟", "🌭", "🍕", "🥪", "🌮", "🌯", "🍗", "🍖", "🍤")
        )
    }

    val textColor = MiuixTheme.colorScheme.onSurface.toArgb()
    // 当前选中的Emoji主题
    var currentThemeIndex by remember { mutableStateOf(0) }
    // 存储所有在屏幕上的背景Emoji
    var backgroundEmojis by remember { mutableStateOf<List<StaticEmoji>>(emptyList()) }
    val logoSize = 120.dp

    // 用于绘制Emoji的Paint对象
    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            color = textColor // 设置文本颜色
        }
    }


    Scaffold {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = constraints.maxWidth.toFloat()
            val screenHeight = constraints.maxHeight.toFloat()
            val logoRadiusPx = with(LocalDensity.current) { (logoSize / 2).toPx() }

            // 3. 当主题或屏幕尺寸变化时，重新生成背景Emoji
            LaunchedEffect(currentThemeIndex, screenWidth, screenHeight) {
                if (screenWidth > 0 && screenHeight > 0) {
                    backgroundEmojis = generatePackedEmojis(
                        width = screenWidth,
                        height = screenHeight,
                        logoCenterX = screenWidth / 2,
                        logoCenterY = screenHeight / 2,
                        logoRadius = logoRadiusPx,
                        emojiSet = emojiSets[currentThemeIndex]
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // 4. 手势检测：单击切换主题
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                currentThemeIndex = (currentThemeIndex + 1) % emojiSets.size
                            })
                    }) {
                // 5. 使用Canvas绘制所有背景Emoji
                Canvas(modifier = Modifier.fillMaxSize()) {
                    backgroundEmojis.forEach { emoji ->
                        // 调整Y坐标以使Emoji在视觉上居中
                        val yOffset = emoji.y + emoji.size / 3
                        textPaint.textSize = emoji.size
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.drawText(emoji.emoji, emoji.x, yOffset, textPaint)
                        }
                    }
                }

                // 6. 在屏幕中央显示App的Logo
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(logoSize),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = appLogo(),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(logoSize)
                    )
                }

                // 在底部显示操作提示
                Text(
                    text = "单击切换背景",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp),
                    style = TextStyle(
                        color = MiuixTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

    }
}

/**
 * 使用圆盘填充算法生成一屏静态的、不重叠的Emoji
 */
private fun generatePackedEmojis(
    width: Float,
    height: Float,
    logoCenterX: Float,
    logoCenterY: Float,
    logoRadius: Float,
    emojiSet: List<String>
): List<StaticEmoji> {
    val placedCircles = mutableListOf<Circle>()

    // 1. 将Logo视为一个固定的、不可侵犯的圆形障碍物
    placedCircles.add(Circle(logoCenterX, logoCenterY, logoRadius))

    val minRadius = 25f  // Emoji的最小半径（像素）
    val maxRadius = 250f  // Emoji的最大半径（像素）
    val maxPlacementAttempts = 100 // 为单个Emoji寻找位置的最大尝试次数
    val totalCirclesToTry = 600   // 尝试放置的总Emoji数量，以确保屏幕被填满

    for (i in 0 until totalCirclesToTry) {
        val radius = Random.nextFloat() * (maxRadius - minRadius) + minRadius
        var bestPosition: Circle? = null

        // 2. 尝试为新Emoji寻找一个有效位置
        for (j in 0 until maxPlacementAttempts) {
            // 生成一个完全在屏幕边界内的随机位置
            val x = Random.nextFloat() * (width - 2 * radius) + radius
            val y = Random.nextFloat() * (height - 2 * radius) + radius
            val potentialCircle = Circle(x, y, radius)

            // 3. 检查是否与任何已存在的圆（包括Logo）重叠
            val isOverlapping = placedCircles.any { existingCircle ->
                val dx = existingCircle.x - potentialCircle.x
                val dy = existingCircle.y - potentialCircle.y
                val distance = sqrt(dx * dx + dy * dy)
                // 如果两圆心距离小于半径之和，则重叠
                distance < existingCircle.radius + potentialCircle.radius
            }

            if (!isOverlapping) {
                bestPosition = potentialCircle
                break // 找到有效位置，停止尝试
            }
        }

        // 4. 如果找到了有效位置，就将其加入列表
        bestPosition?.let {
            placedCircles.add(it)
        }
    }

    // 5. 从列表中移除Logo障碍物，只保留用于绘制的Emoji
    placedCircles.removeAt(0)

    // 6. 将内部使用的Circle对象转换为用于绘制的StaticEmoji对象
    return placedCircles.map { circle ->
        StaticEmoji(
            x = circle.x, y = circle.y, size = circle.radius * 2, // StaticEmoji使用直径作为size
            emoji = emojiSet.random()
        )
    }
}
