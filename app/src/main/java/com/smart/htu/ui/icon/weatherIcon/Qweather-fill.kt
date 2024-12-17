package com.smart.htu.ui.icon.weatherIcon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.smart.htu.ui.icon.WeatherIcon

public val WeatherIcon.`Qweather-fill`: ImageVector
    get() {
        if (`_qweather-fill` != null) {
            return `_qweather-fill`!!
        }
        `_qweather-fill` = Builder(name = "Qweather-fill", defaultWidth = 16.0.dp, defaultHeight =
                16.0.dp, viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.905f, 11.625f)
                arcToRelative(4.666f, 4.666f, 0.0f, false, false, 3.007f, 1.382f)
                arcToRelative(6.36f, 6.36f, 0.0f, false, true, -2.795f, -2.604f)
                arcToRelative(3.54f, 3.54f, 0.0f, false, true, 0.589f, -4.054f)
                curveToRelative(0.655f, -0.692f, 1.586f, -1.093f, 2.619f, -1.123f)
                curveToRelative(1.151f, -0.033f, 2.162f, 0.458f, 2.803f, 1.295f)
                arcToRelative(2.157f, 2.157f, 0.0f, false, false, 0.029f, 0.036f)
                arcToRelative(3.85f, 3.85f, 0.0f, false, true, 0.11f, 0.147f)
                curveToRelative(0.252f, 0.383f, 0.385f, 0.83f, 0.382f, 1.288f)
                arcToRelative(2.235f, 2.235f, 0.0f, false, true, -1.168f, 1.964f)
                arcToRelative(2.235f, 2.235f, 0.0f, false, true, -0.998f, 0.279f)
                horizontalLineToRelative(-0.02f)
                arcToRelative(1.36f, 1.36f, 0.0f, false, true, -1.158f, -0.65f)
                arcToRelative(0.135f, 0.135f, 0.0f, false, true, 0.086f, -0.202f)
                arcToRelative(0.138f, 0.138f, 0.0f, false, true, 0.084f, 0.007f)
                arcToRelative(1.236f, 1.236f, 0.0f, false, false, 1.14f, -0.13f)
                arcToRelative(1.213f, 1.213f, 0.0f, false, false, 0.526f, -1.036f)
                arcToRelative(1.202f, 1.202f, 0.0f, false, false, -0.168f, -0.574f)
                arcToRelative(1.357f, 1.357f, 0.0f, false, false, -0.638f, -0.554f)
                arcToRelative(1.156f, 1.156f, 0.0f, false, false, -0.162f, -0.05f)
                lineToRelative(-0.01f, -0.002f)
                lineToRelative(-0.023f, -0.005f)
                arcToRelative(2.026f, 2.026f, 0.0f, false, false, -2.03f, 0.666f)
                arcToRelative(1.831f, 1.831f, 0.0f, false, false, -0.444f, 1.233f)
                arcToRelative(2.6f, 2.6f, 0.0f, false, false, 0.568f, 1.567f)
                verticalLineToRelative(0.001f)
                lineToRelative(0.003f, 0.002f)
                curveToRelative(0.65f, 0.766f, 1.572f, 1.432f, 2.63f, 1.67f)
                curveToRelative(0.286f, 0.062f, 0.577f, 0.102f, 0.87f, 0.12f)
                arcToRelative(4.616f, 4.616f, 0.0f, false, false, 1.942f, -2.523f)
                arcToRelative(4.576f, 4.576f, 0.0f, false, false, -0.161f, -3.17f)
                arcTo(4.628f, 4.628f, 0.0f, false, false, 9.33f, 4.29f)
                arcToRelative(4.686f, 4.686f, 0.0f, false, false, -3.178f, -0.366f)
                arcTo(4.653f, 4.653f, 0.0f, false, false, 3.487f, 5.68f)
                arcToRelative(4.584f, 4.584f, 0.0f, false, false, 0.418f, 5.945f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.0f, 0.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, -2.0f, 2.0f)
                verticalLineToRelative(12.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.0f, 2.0f)
                horizontalLineToRelative(12.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.0f, -2.0f)
                lineTo(16.0f, 2.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, -2.0f, -2.0f)
                lineTo(2.0f, 0.0f)
                close()
                moveTo(13.413f, 11.066f)
                lineTo(13.635f, 10.868f)
                horizontalLineToRelative(0.865f)
                curveToRelative(-0.083f, 0.12f, -0.162f, 0.238f, -0.238f, 0.352f)
                curveToRelative(-0.462f, 0.69f, -0.85f, 1.27f, -1.634f, 1.68f)
                arcToRelative(6.388f, 6.388f, 0.0f, false, true, -3.53f, 0.702f)
                arcToRelative(5.945f, 5.945f, 0.0f, false, true, -4.176f, -0.293f)
                arcToRelative(5.873f, 5.873f, 0.0f, false, true, -2.929f, -2.967f)
                arcToRelative(5.805f, 5.805f, 0.0f, false, true, -0.205f, -4.147f)
                arcToRelative(5.86f, 5.86f, 0.0f, false, true, 2.621f, -3.238f)
                arcToRelative(5.942f, 5.942f, 0.0f, false, true, 4.128f, -0.697f)
                arcToRelative(5.906f, 5.906f, 0.0f, false, true, 3.554f, 2.194f)
                arcToRelative(5.816f, 5.816f, 0.0f, false, true, -0.58f, 7.742f)
                curveToRelative(0.859f, -0.199f, 1.292f, -0.586f, 1.902f, -1.13f)
                close()
            }
        }
        .build()
        return `_qweather-fill`!!
    }

private var `_qweather-fill`: ImageVector? = null
