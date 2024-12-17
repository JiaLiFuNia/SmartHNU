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

public val WeatherIcon._1034: ImageVector
    get() {
        if (__1034 != null) {
            return __1034!!
        }
        __1034 = Builder(name = "_1034", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.302f, 1.7f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, 1.4f, 0.0f)
                verticalLineToRelative(1.088f)
                lineToRelative(0.942f, -0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, 0.7f, 1.212f)
                lineTo(4.402f, 4.0f)
                lineToRelative(0.942f, 0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, -0.7f, 1.212f)
                lineToRelative(-0.942f, -0.544f)
                verticalLineTo(6.3f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, -1.4f, 0.0f)
                verticalLineTo(5.212f)
                lineToRelative(-0.942f, 0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, -0.7f, -1.212f)
                lineTo(1.602f, 4.0f)
                lineTo(0.66f, 3.456f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, 0.7f, -1.212f)
                lineToRelative(0.942f, 0.544f)
                verticalLineTo(1.7f)
                close()
                moveTo(11.5f, 7.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, 0.5f)
                verticalLineToRelative(2.063f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, false, 1.0f, 0.0f)
                verticalLineTo(7.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, -0.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(10.2f, 8.399f)
                lineToRelative(-0.532f, 0.356f)
                arcToRelative(3.3f, 3.3f, 0.0f, true, false, 3.665f, 0.0f)
                lineToRelative(-0.533f, -0.356f)
                verticalLineTo(2.5f)
                arcToRelative(1.3f, 1.3f, 0.0f, true, false, -2.6f, 0.0f)
                verticalLineToRelative(5.899f)
                close()
                moveTo(9.0f, 2.5f)
                arcToRelative(2.5f, 2.5f, 0.0f, false, true, 5.0f, 0.0f)
                verticalLineToRelative(5.258f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, true, -5.0f, 0.0f)
                verticalLineTo(2.5f)
                close()
            }
        }
        .build()
        return __1034!!
    }

private var __1034: ImageVector? = null
