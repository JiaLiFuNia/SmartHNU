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

public val WeatherIcon._805: ImageVector
    get() {
        if (__805 != null) {
            return __805!!
        }
        __805 = Builder(name = "_805", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.0f, 8.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 16.0f, 0.0f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 0.0f, 8.0f)
                close()
                moveTo(1.0f, 8.0f)
                arcToRelative(7.008f, 7.008f, 0.0f, false, true, 7.0f, -7.0f)
                curveToRelative(0.295f, 0.001f, 0.59f, 0.022f, 0.881f, 0.063f)
                arcToRelative(0.702f, 0.702f, 0.0f, false, true, 0.36f, 0.157f)
                arcTo(8.868f, 8.868f, 0.0f, false, true, 12.036f, 8.0f)
                arcToRelative(8.838f, 8.838f, 0.0f, false, true, -2.849f, 6.822f)
                arcToRelative(0.486f, 0.486f, 0.0f, false, true, -0.24f, 0.106f)
                arcTo(6.73f, 6.73f, 0.0f, false, true, 8.0f, 15.0f)
                arcToRelative(7.008f, 7.008f, 0.0f, false, true, -7.0f, -7.0f)
                close()
            }
        }
        .build()
        return __805!!
    }

private var __805: ImageVector? = null
