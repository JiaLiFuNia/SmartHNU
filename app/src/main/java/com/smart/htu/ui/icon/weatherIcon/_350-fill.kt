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

public val WeatherIcon.`_350-fill`: ImageVector
    get() {
        if (`__350-fill` != null) {
            return `__350-fill`!!
        }
        `__350-fill` = Builder(name = "_350-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.5f, 15.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(9.5f, 15.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(10.994f, 10.896f)
                arcTo(4.758f, 4.758f, 0.0f, false, true, 7.406f, 12.5f)
                arcToRelative(4.76f, 4.76f, 0.0f, false, true, -3.537f, -1.547f)
                arcToRelative(2.908f, 2.908f, 0.0f, false, true, -1.057f, 0.197f)
                curveTo(1.26f, 11.15f, 0.0f, 9.941f, 0.0f, 8.45f)
                reflectiveCurveToRelative(1.26f, -2.7f, 2.813f, -2.7f)
                curveToRelative(0.173f, 0.0f, 0.342f, 0.015f, 0.507f, 0.044f)
                curveTo(4.124f, 4.424f, 5.652f, 3.5f, 7.406f, 3.5f)
                curveToRelative(1.769f, 0.0f, 3.308f, 0.94f, 4.107f, 2.328f)
                arcToRelative(2.93f, 2.93f, 0.0f, false, true, 0.675f, -0.078f)
                curveTo(13.74f, 5.75f, 15.0f, 6.959f, 15.0f, 8.45f)
                reflectiveCurveToRelative(-1.26f, 2.7f, -2.813f, 2.7f)
                arcToRelative(2.9f, 2.9f, 0.0f, false, true, -1.193f, -0.254f)
                close()
                moveTo(15.466f, 4.215f)
                arcToRelative(0.31f, 0.31f, 0.0f, false, false, -0.08f, 0.01f)
                arcToRelative(3.066f, 3.066f, 0.0f, false, true, -1.866f, -0.076f)
                arcTo(3.183f, 3.183f, 0.0f, false, true, 11.492f, 0.364f)
                arcTo(0.29f, 0.29f, 0.0f, false, false, 11.22f, 0.0f)
                arcToRelative(0.28f, 0.28f, 0.0f, false, false, -0.104f, 0.02f)
                arcToRelative(3.546f, 3.546f, 0.0f, false, false, -2.21f, 3.096f)
                curveToRelative(0.34f, 0.063f, 0.671f, 0.16f, 0.99f, 0.293f)
                arcToRelative(2.56f, 2.56f, 0.0f, false, true, 0.54f, -1.671f)
                arcToRelative(4.166f, 4.166f, 0.0f, false, false, 2.755f, 3.356f)
                curveToRelative(0.274f, 0.096f, 0.558f, 0.164f, 0.846f, 0.203f)
                arcToRelative(2.611f, 2.611f, 0.0f, false, true, -0.239f, 0.163f)
                curveToRelative(0.304f, 0.173f, 0.582f, 0.39f, 0.823f, 0.643f)
                arcToRelative(3.553f, 3.553f, 0.0f, false, false, 1.12f, -1.504f)
                arcToRelative(0.285f, 0.285f, 0.0f, false, false, -0.275f, -0.384f)
                close()
            }
        }
        .build()
        return `__350-fill`!!
    }

private var `__350-fill`: ImageVector? = null
