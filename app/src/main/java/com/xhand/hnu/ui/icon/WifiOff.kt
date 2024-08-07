package com.xhand.hnu.ui.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberWifiOff(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "wifi_off",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 34.458f)
                quadToRelative(-1.542f, 0f, -2.646f, -1.104f)
                quadToRelative(-1.104f, -1.104f, -1.104f, -2.646f)
                quadToRelative(0f, -1.5f, 1.104f, -2.625f)
                reflectiveQuadTo(20f, 26.958f)
                quadToRelative(1.542f, 0f, 2.646f, 1.125f)
                quadToRelative(1.104f, 1.125f, 1.104f, 2.625f)
                quadToRelative(0f, 1.542f, -1.104f, 2.646f)
                quadToRelative(-1.104f, 1.104f, -2.646f, 1.104f)
                close()
                moveTo(35.25f, 17f)
                quadToRelative(-3.292f, -2.75f, -7.125f, -4.333f)
                quadToRelative(-3.833f, -1.584f, -8.125f, -1.584f)
                quadToRelative(-1.333f, 0f, -2.458f, 0.146f)
                quadToRelative(-1.125f, 0.146f, -1.959f, 0.396f)
                lineToRelative(-3.291f, -3.292f)
                quadToRelative(1.75f, -0.583f, 3.687f, -0.895f)
                quadToRelative(1.938f, -0.313f, 4.021f, -0.313f)
                quadToRelative(5.167f, 0f, 9.771f, 1.896f)
                quadToRelative(4.604f, 1.896f, 8.271f, 5.187f)
                quadToRelative(0.625f, 0.542f, 0.625f, 1.354f)
                quadToRelative(0f, 0.813f, -0.625f, 1.396f)
                quadToRelative(-0.542f, 0.542f, -1.354f, 0.563f)
                quadToRelative(-0.813f, 0.021f, -1.438f, -0.521f)
                close()
                moveToRelative(-6.333f, 6.917f)
                quadToRelative(-0.834f, -0.709f, -1.563f, -1.209f)
                quadToRelative(-0.729f, -0.5f, -1.646f, -1f)
                lineToRelative(-5f, -4.958f)
                quadToRelative(3.084f, 0.125f, 5.709f, 1.229f)
                quadToRelative(2.625f, 1.104f, 4.875f, 3.021f)
                quadToRelative(0.583f, 0.5f, 0.604f, 1.292f)
                quadToRelative(0.021f, 0.791f, -0.604f, 1.416f)
                quadToRelative(-0.459f, 0.5f, -1.146f, 0.542f)
                quadToRelative(-0.688f, 0.042f, -1.229f, -0.333f)
                close()
                moveToRelative(3.416f, 12.125f)
                lineTo(17.208f, 20.958f)
                quadToRelative(-1.666f, 0.375f, -3.083f, 1.104f)
                quadToRelative(-1.417f, 0.73f, -2.625f, 1.688f)
                quadToRelative(-0.667f, 0.542f, -1.458f, 0.5f)
                quadToRelative(-0.792f, -0.042f, -1.375f, -0.583f)
                quadToRelative(-0.584f, -0.584f, -0.563f, -1.375f)
                quadToRelative(0.021f, -0.792f, 0.604f, -1.292f)
                quadToRelative(1.125f, -1f, 2.417f, -1.771f)
                quadToRelative(1.292f, -0.771f, 2.917f, -1.437f)
                lineToRelative(-4.167f, -4.167f)
                quadToRelative(-1.417f, 0.667f, -2.688f, 1.542f)
                quadToRelative(-1.27f, 0.875f, -2.437f, 1.833f)
                quadToRelative(-0.625f, 0.542f, -1.438f, 0.521f)
                quadToRelative(-0.812f, -0.021f, -1.395f, -0.604f)
                quadToRelative(-0.584f, -0.584f, -0.584f, -1.375f)
                quadToRelative(0f, -0.792f, 0.625f, -1.334f)
                quadToRelative(1.167f, -1.041f, 2.396f, -1.937f)
                quadToRelative(1.229f, -0.896f, 2.563f, -1.604f)
                lineTo(4f, 7.708f)
                quadToRelative(-0.375f, -0.333f, -0.375f, -0.896f)
                quadToRelative(0f, -0.562f, 0.417f, -0.937f)
                quadToRelative(0.333f, -0.375f, 0.896f, -0.375f)
                quadToRelative(0.562f, 0f, 0.979f, 0.375f)
                lineToRelative(28.291f, 28.333f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.938f)
                quadToRelative(0f, 0.562f, -0.375f, 0.896f)
                quadToRelative(-0.375f, 0.416f, -0.937f, 0.416f)
                quadToRelative(-0.563f, 0f, -0.938f, -0.416f)
                close()
            }
        }.build()
    }
}