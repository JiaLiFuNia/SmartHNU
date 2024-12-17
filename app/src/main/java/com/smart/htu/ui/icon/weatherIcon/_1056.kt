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

public val WeatherIcon._1056: ImageVector
    get() {
        if (__1056 != null) {
            return __1056!!
        }
        __1056 = Builder(name = "_1056", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.6f, 4.5f)
                lineTo(5.4f, 4.5f)
                arcTo(0.6f, 0.6f, 0.0f, false, true, 6.0f, 5.1f)
                lineTo(6.0f, 5.1f)
                arcTo(0.6f, 0.6f, 0.0f, false, true, 5.4f, 5.7f)
                lineTo(0.6f, 5.7f)
                arcTo(0.6f, 0.6f, 0.0f, false, true, 0.0f, 5.1f)
                lineTo(0.0f, 5.1f)
                arcTo(0.6f, 0.6f, 0.0f, false, true, 0.6f, 4.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
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
        return __1056!!
    }

private var __1056: ImageVector? = null
