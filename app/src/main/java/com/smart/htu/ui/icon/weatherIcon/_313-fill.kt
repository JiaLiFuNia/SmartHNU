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

public val WeatherIcon.`_313-fill`: ImageVector
    get() {
        if (`__313-fill` != null) {
            return `__313-fill`!!
        }
        `__313-fill` = Builder(name = "_313-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(1.0f, 9.0f)
                lineToRelative(1.0f, 1.5f)
                lineTo(1.0f, 12.0f)
                lineToRelative(-1.0f, -1.5f)
                lineTo(1.0f, 9.0f)
                close()
                moveTo(16.0f, 10.5f)
                lineTo(15.0f, 9.0f)
                lineToRelative(-1.0f, 1.5f)
                lineToRelative(1.0f, 1.5f)
                lineToRelative(1.0f, -1.5f)
                close()
                moveTo(9.0f, 14.5f)
                lineTo(8.0f, 13.0f)
                lineToRelative(-1.0f, 1.5f)
                lineTo(8.0f, 16.0f)
                lineToRelative(1.0f, -1.5f)
                close()
                moveTo(4.5f, 11.0f)
                lineToRelative(1.0f, 1.5f)
                lineToRelative(-1.0f, 1.5f)
                lineToRelative(-1.0f, -1.5f)
                lineToRelative(1.0f, -1.5f)
                close()
                moveTo(12.5f, 12.5f)
                lineTo(11.5f, 11.0f)
                lineTo(10.5f, 12.5f)
                lineTo(11.5f, 14.0f)
                lineTo(12.5f, 12.5f)
                close()
                moveTo(11.727f, 8.217f)
                arcTo(4.99f, 4.99f, 0.0f, false, true, 7.9f, 10.0f)
                arcToRelative(4.988f, 4.988f, 0.0f, false, true, -3.773f, -1.719f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.586f, -5.732f)
                arcTo(4.998f, 4.998f, 0.0f, false, true, 7.9f, 0.0f)
                arcToRelative(4.999f, 4.999f, 0.0f, false, true, 4.38f, 2.587f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.553f, 5.63f)
                close()
            }
        }
        .build()
        return `__313-fill`!!
    }

private var `__313-fill`: ImageVector? = null
