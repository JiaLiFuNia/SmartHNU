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

public val WeatherIcon.`_309-fill`: ImageVector
    get() {
        if (`__309-fill` != null) {
            return `__309-fill`!!
        }
        `__309-fill` = Builder(name = "_309-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.534f, 12.125f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.433f, 0.25f)
                lineToRelative(-1.75f, 3.031f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, -0.433f, -0.25f)
                lineToRelative(1.75f, -3.031f)
                close()
                moveTo(12.784f, 12.825f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.432f, 0.25f)
                lineToRelative(-1.0f, 1.732f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, -0.432f, -0.25f)
                lineToRelative(1.0f, -1.732f)
                close()
                moveTo(3.784f, 12.825f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.433f, 0.25f)
                lineToRelative(-1.0f, 1.732f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.433f, -0.25f)
                lineToRelative(1.0f, -1.732f)
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
        return `__309-fill`!!
    }

private var `__309-fill`: ImageVector? = null
