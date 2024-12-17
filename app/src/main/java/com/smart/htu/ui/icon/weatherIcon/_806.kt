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

public val WeatherIcon._806: ImageVector
    get() {
        if (__806 != null) {
            return __806!!
        }
        __806 = Builder(name = "_806", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 0.0f)
                arcToRelative(8.03f, 8.03f, 0.0f, false, false, -1.065f, 0.079f)
                arcToRelative(7.992f, 7.992f, 0.0f, false, false, -0.354f, 15.788f)
                horizontalLineToRelative(0.001f)
                curveToRelative(0.468f, 0.087f, 0.942f, 0.131f, 1.418f, 0.133f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 8.0f, 0.0f)
                close()
                moveTo(1.0f, 8.0f)
                arcToRelative(7.008f, 7.008f, 0.0f, false, true, 6.204f, -6.951f)
                arcTo(25.25f, 25.25f, 0.0f, false, true, 8.0f, 7.5f)
                curveToRelative(0.032f, 2.51f, -0.328f, 5.01f, -1.067f, 7.41f)
                arcTo(7.005f, 7.005f, 0.0f, false, true, 1.0f, 8.0f)
                close()
            }
        }
        .build()
        return __806!!
    }

private var __806: ImageVector? = null
