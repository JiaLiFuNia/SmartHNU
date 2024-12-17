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

public val WeatherIcon._1042: ImageVector
    get() {
        if (__1042 != null) {
            return __1042!!
        }
        __1042 = Builder(name = "_1042", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.5f, 4.0f)
                horizontalLineToRelative(3.0f)
                verticalLineToRelative(2.5f)
                horizontalLineTo(12.0f)
                verticalLineToRelative(3.0f)
                horizontalLineTo(9.5f)
                verticalLineTo(12.0f)
                horizontalLineToRelative(-3.0f)
                verticalLineTo(9.5f)
                horizontalLineTo(4.0f)
                verticalLineToRelative(-3.0f)
                horizontalLineToRelative(2.5f)
                verticalLineTo(4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(0.095f, 7.65f)
                lineToRelative(3.648f, -6.298f)
                arcTo(0.705f, 0.705f, 0.0f, false, true, 4.352f, 1.0f)
                horizontalLineToRelative(7.296f)
                arcToRelative(0.703f, 0.703f, 0.0f, false, true, 0.609f, 0.352f)
                lineToRelative(3.648f, 6.298f)
                arcToRelative(0.688f, 0.688f, 0.0f, false, true, 0.0f, 0.7f)
                lineToRelative(-3.648f, 6.298f)
                arcToRelative(0.705f, 0.705f, 0.0f, false, true, -0.61f, 0.352f)
                horizontalLineTo(4.353f)
                arcToRelative(0.705f, 0.705f, 0.0f, false, true, -0.61f, -0.351f)
                lineTo(0.096f, 8.351f)
                arcToRelative(0.693f, 0.693f, 0.0f, false, true, 0.0f, -0.701f)
                close()
                moveTo(14.484f, 8.0f)
                lineToRelative(-3.242f, -5.596f)
                horizontalLineTo(4.758f)
                lineTo(1.516f, 8.0f)
                lineToRelative(3.243f, 5.597f)
                horizontalLineToRelative(6.483f)
                lineTo(14.484f, 8.0f)
                close()
            }
        }
        .build()
        return __1042!!
    }

private var __1042: ImageVector? = null
