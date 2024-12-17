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

public val WeatherIcon._2081: ImageVector
    get() {
        if (__2081 != null) {
            return __2081!!
        }
        __2081 = Builder(name = "_2081", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.0f, 2.0f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, true, 0.6f, 0.6f)
                verticalLineToRelative(1.8f)
                horizontalLineToRelative(1.8f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, true, 0.0f, 1.2f)
                horizontalLineToRelative(-1.8f)
                verticalLineToRelative(1.8f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, true, -1.2f, 0.0f)
                verticalLineTo(5.6f)
                horizontalLineToRelative(-1.8f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, true, 0.0f, -1.2f)
                horizontalLineToRelative(1.8f)
                verticalLineTo(2.6f)
                arcTo(0.6f, 0.6f, 0.0f, false, true, 13.0f, 2.0f)
                close()
                moveTo(4.5f, 3.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.5f, 0.5f)
                verticalLineToRelative(6.063f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -1.0f, 0.0f)
                verticalLineTo(3.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.5f, -0.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(5.8f, 8.399f)
                lineToRelative(0.532f, 0.356f)
                arcToRelative(3.3f, 3.3f, 0.0f, true, true, -3.665f, 0.0f)
                lineToRelative(0.533f, -0.356f)
                verticalLineTo(2.5f)
                arcToRelative(1.3f, 1.3f, 0.0f, false, true, 2.6f, 0.0f)
                verticalLineToRelative(5.899f)
                close()
                moveTo(7.0f, 2.5f)
                arcToRelative(2.5f, 2.5f, 0.0f, false, false, -5.0f, 0.0f)
                verticalLineToRelative(5.258f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, false, 5.0f, 0.0f)
                verticalLineTo(2.5f)
                close()
            }
        }
        .build()
        return __2081!!
    }

private var __2081: ImageVector? = null
