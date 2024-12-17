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

public val WeatherIcon.`_900-fill`: ImageVector
    get() {
        if (`__900-fill` != null) {
            return `__900-fill`!!
        }
        `__900-fill` = Builder(name = "_900-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.5f, 0.0f)
                arcTo(2.5f, 2.5f, 0.0f, false, false, 9.0f, 2.5f)
                verticalLineToRelative(4.99f)
                arcToRelative(0.534f, 0.534f, 0.0f, false, true, -0.217f, 0.423f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, false, 5.435f, 0.0f)
                arcTo(0.534f, 0.534f, 0.0f, false, true, 14.0f, 7.49f)
                lineTo(14.0f, 2.5f)
                arcTo(2.5f, 2.5f, 0.0f, false, false, 11.5f, 0.0f)
                close()
                moveTo(13.5f, 11.5f)
                arcTo(2.0f, 2.0f, 0.0f, true, true, 11.0f, 9.563f)
                lineTo(11.0f, 3.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 1.0f, 0.0f)
                verticalLineToRelative(6.063f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 1.5f, 1.937f)
                close()
                moveTo(4.9f, 2.5f)
                curveToRelative(-0.9f, 0.3f, -1.2f, 1.1f, -1.1f, 1.6f)
                curveToRelative(-0.7f, -0.8f, -0.7f, -1.7f, -0.6f, -3.1f)
                curveToRelative(-2.1f, 0.8f, -1.6f, 3.2f, -1.7f, 4.0f)
                curveTo(1.0f, 4.5f, 0.9f, 3.5f, 0.9f, 3.5f)
                curveTo(0.3f, 3.8f, 0.0f, 4.6f, 0.0f, 5.3f)
                curveTo(0.0f, 6.9f, 1.3f, 8.0f, 2.8f, 8.0f)
                curveToRelative(1.5f, 0.0f, 2.7f, -1.2f, 2.7f, -2.7f)
                curveToRelative(0.0f, -1.1f, -0.6f, -1.4f, -0.6f, -2.8f)
                close()
            }
        }
        .build()
        return `__900-fill`!!
    }

private var `__900-fill`: ImageVector? = null
