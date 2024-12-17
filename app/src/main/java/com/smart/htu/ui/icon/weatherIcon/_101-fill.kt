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

public val WeatherIcon.`_101-fill`: ImageVector
    get() {
        if (`__101-fill` != null) {
            return `__101-fill`!!
        }
        `__101-fill` = Builder(name = "_101-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.745f, 1.777f)
                arcToRelative(0.516f, 0.516f, 0.0f, true, false, 1.007f, -0.224f)
                lineTo(5.496f, 0.403f)
                arcTo(0.516f, 0.516f, 0.0f, false, false, 4.49f, 0.627f)
                lineToRelative(0.255f, 1.15f)
                close()
                moveTo(1.023f, 3.535f)
                lineToRelative(0.994f, 0.633f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, 0.554f, -0.87f)
                lineToRelative(-0.994f, -0.633f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, -0.554f, 0.87f)
                close()
                moveTo(0.628f, 8.043f)
                lineToRelative(1.15f, -0.256f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, -0.223f, -1.008f)
                lineToRelative(-1.15f, 0.256f)
                arcToRelative(0.516f, 0.516f, 0.0f, true, false, 0.223f, 1.008f)
                close()
                moveTo(10.866f, 5.763f)
                arcToRelative(0.535f, 0.535f, 0.0f, false, false, 0.112f, -0.012f)
                lineToRelative(1.15f, -0.256f)
                arcToRelative(0.516f, 0.516f, 0.0f, true, false, -0.224f, -1.008f)
                lineToRelative(-1.15f, 0.256f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, 0.112f, 1.02f)
                close()
                moveTo(8.522f, 2.728f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, 0.712f, -0.158f)
                lineToRelative(0.633f, -0.994f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, -0.87f, -0.554f)
                lineToRelative(-0.633f, 0.994f)
                arcToRelative(0.516f, 0.516f, 0.0f, false, false, 0.158f, 0.712f)
                close()
                moveTo(2.819f, 7.032f)
                curveToRelative(0.071f, 0.303f, 0.182f, 0.596f, 0.331f, 0.87f)
                arcToRelative(3.13f, 3.13f, 0.0f, false, false, 0.908f, -0.486f)
                arcToRelative(2.453f, 2.453f, 0.0f, false, true, -0.232f, -0.608f)
                arcTo(2.504f, 2.504f, 0.0f, false, true, 8.714f, 5.72f)
                lineToRelative(0.004f, 0.038f)
                arcToRelative(5.42f, 5.42f, 0.0f, false, true, 1.064f, 0.25f)
                arcToRelative(3.51f, 3.51f, 0.0f, false, false, -0.061f, -0.512f)
                arcToRelative(3.535f, 3.535f, 0.0f, false, false, -6.902f, 1.536f)
                close()
                moveTo(11.994f, 14.396f)
                arcTo(4.758f, 4.758f, 0.0f, false, true, 8.406f, 16.0f)
                arcToRelative(4.76f, 4.76f, 0.0f, false, true, -3.537f, -1.547f)
                arcToRelative(2.908f, 2.908f, 0.0f, false, true, -1.056f, 0.197f)
                curveTo(2.258f, 14.65f, 1.0f, 13.441f, 1.0f, 11.95f)
                reflectiveCurveToRelative(1.26f, -2.7f, 2.813f, -2.7f)
                curveToRelative(0.173f, 0.0f, 0.342f, 0.015f, 0.507f, 0.044f)
                curveTo(5.124f, 7.924f, 6.652f, 7.0f, 8.406f, 7.0f)
                curveToRelative(1.769f, 0.0f, 3.308f, 0.94f, 4.107f, 2.328f)
                arcToRelative(2.93f, 2.93f, 0.0f, false, true, 0.675f, -0.078f)
                curveToRelative(1.553f, 0.0f, 2.812f, 1.209f, 2.812f, 2.7f)
                reflectiveCurveToRelative(-1.26f, 2.7f, -2.813f, 2.7f)
                arcToRelative(2.9f, 2.9f, 0.0f, false, true, -1.193f, -0.254f)
                close()
            }
        }
        .build()
        return `__101-fill`!!
    }

private var `__101-fill`: ImageVector? = null
