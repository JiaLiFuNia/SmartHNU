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

public val WeatherIcon.`_509-fill`: ImageVector
    get() {
        if (`__509-fill` != null) {
            return `__509-fill`!!
        }
        `__509-fill` = Builder(name = "_509-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.25f, 11.0f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(9.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                lineTo(0.25f, 11.0f)
                close()
                moveTo(3.25f, 12.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-3.5f)
                close()
                moveTo(10.0f, 14.25f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.25f, -0.25f)
                horizontalLineToRelative(3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, 0.5f)
                horizontalLineToRelative(-3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.25f, -0.25f)
                close()
                moveTo(8.25f, 12.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-7.5f)
                close()
                moveTo(1.0f, 14.25f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.25f, -0.25f)
                horizontalLineToRelative(7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, 0.5f)
                horizontalLineToRelative(-7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.25f, -0.25f)
                close()
                moveTo(3.25f, 15.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(9.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-9.5f)
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
        return `__509-fill`!!
    }

private var `__509-fill`: ImageVector? = null
