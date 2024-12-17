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

public val WeatherIcon._1603: ImageVector
    get() {
        if (__1603 != null) {
            return __1603!!
        }
        __1603 = Builder(name = "_1603", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.8f, 2.4f)
                arcToRelative(1.2f, 1.2f, 0.0f, true, true, 0.0f, -2.4f)
                arcToRelative(1.2f, 1.2f, 0.0f, false, true, 0.0f, 2.4f)
                close()
                moveTo(2.69f, 3.905f)
                lineTo(1.317f, 3.0f)
                lineTo(0.0f, 16.0f)
                horizontalLineToRelative(14.0f)
                lineToRelative(-3.498f, -5.768f)
                lineToRelative(-3.349f, -0.98f)
                lineToRelative(-1.032f, -1.928f)
                lineToRelative(-2.277f, -1.102f)
                lineTo(2.69f, 3.905f)
                close()
                moveTo(9.5f, 8.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 0.0f, -3.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 0.0f, 3.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.6f, 5.2f)
                arcToRelative(1.2f, 1.2f, 0.0f, true, false, 2.4f, 0.0f)
                arcToRelative(1.2f, 1.2f, 0.0f, false, false, -2.4f, 0.0f)
                close()
                moveTo(12.0f, 4.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, -2.0f, 0.0f)
                close()
                moveTo(13.0f, 10.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, 0.0f, -2.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, 0.0f, 2.0f)
                close()
                moveTo(7.0f, 3.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, -2.0f, 0.0f)
                close()
                moveTo(14.5f, 14.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 0.0f, -3.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 0.0f, 3.0f)
                close()
            }
        }
        .build()
        return __1603!!
    }

private var __1603: ImageVector? = null
