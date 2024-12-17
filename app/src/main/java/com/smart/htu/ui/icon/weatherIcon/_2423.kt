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

public val WeatherIcon._2423: ImageVector
    get() {
        if (__2423 != null) {
            return __2423!!
        }
        __2423 = Builder(name = "_2423", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.6f, 11.25f)
                horizontalLineToRelative(1.245f)
                lineTo(5.845f, 5.873f)
                horizontalLineToRelative(1.6f)
                lineTo(7.445f, 4.75f)
                lineTo(3.0f, 4.75f)
                verticalLineToRelative(1.123f)
                horizontalLineToRelative(1.6f)
                verticalLineToRelative(5.377f)
                close()
                moveTo(9.205f, 4.75f)
                lineTo(7.863f, 4.75f)
                lineToRelative(1.946f, 3.94f)
                verticalLineToRelative(2.56f)
                horizontalLineToRelative(1.245f)
                lineTo(11.054f, 8.69f)
                lineTo(13.0f, 4.75f)
                horizontalLineToRelative(-1.342f)
                lineToRelative(-1.229f, 2.612f)
                lineTo(9.205f, 4.75f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.0f, 8.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 16.0f, 0.0f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 0.0f, 8.0f)
                close()
                moveTo(14.7f, 8.0f)
                arcTo(6.7f, 6.7f, 0.0f, true, true, 1.3f, 8.0f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, true, 13.4f, 0.0f)
                close()
            }
        }
        .build()
        return __2423!!
    }

private var __2423: ImageVector? = null
