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

public val WeatherIcon._2163: ImageVector
    get() {
        if (__2163 != null) {
            return __2163!!
        }
        __2163 = Builder(name = "_2163", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(16.0f, 5.857f)
                lineToRelative(-2.986f, -4.779f)
                lineTo(3.066f, 16.0f)
                horizontalLineTo(16.0f)
                verticalLineTo(5.857f)
                close()
                moveTo(8.0f, 4.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, false, 0.0f, -4.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 0.0f, 4.0f)
                close()
                moveTo(4.5f, 4.0f)
                lineToRelative(-0.5f, 0.866f)
                lineToRelative(0.866f, 0.5f)
                lineToRelative(0.5f, -0.866f)
                lineTo(4.5f, 4.0f)
                close()
                moveTo(2.414f, 6.0f)
                lineTo(1.0f, 7.414f)
                lineToRelative(1.414f, 1.414f)
                lineToRelative(1.414f, -1.414f)
                lineTo(2.414f, 6.0f)
                close()
                moveTo(4.0f, 11.259f)
                lineTo(4.966f, 11.0f)
                lineToRelative(0.259f, 0.966f)
                lineToRelative(-0.966f, 0.259f)
                lineTo(4.0f, 11.259f)
                close()
                moveTo(3.0f, 13.5f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, -3.0f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 3.0f, 0.0f)
                close()
                moveTo(6.5f, 9.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, false, 0.0f, -3.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, false, 0.0f, 3.0f)
                close()
            }
        }
        .build()
        return __2163!!
    }

private var __2163: ImageVector? = null
