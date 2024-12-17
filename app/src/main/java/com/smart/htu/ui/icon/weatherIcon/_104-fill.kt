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

public val WeatherIcon.`_104-fill`: ImageVector
    get() {
        if (`__104-fill` != null) {
            return `__104-fill`!!
        }
        `__104-fill` = Builder(name = "_104-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.727f, 14.217f)
                arcTo(4.99f, 4.99f, 0.0f, false, true, 7.9f, 16.0f)
                arcToRelative(4.988f, 4.988f, 0.0f, false, true, -3.773f, -1.719f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.586f, -5.732f)
                arcTo(4.998f, 4.998f, 0.0f, false, true, 7.9f, 6.0f)
                arcToRelative(4.999f, 4.999f, 0.0f, false, true, 4.38f, 2.587f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.553f, 5.63f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.008f, 6.637f)
                arcToRelative(1.545f, 1.545f, 0.0f, false, true, 1.54f, -1.467f)
                arcToRelative(0.913f, 0.913f, 0.0f, false, true, 0.108f, 0.012f)
                lineToRelative(0.084f, 0.012f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 0.961f, -0.445f)
                arcToRelative(2.74f, 2.74f, 0.0f, false, true, 4.598f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 0.961f, 0.445f)
                lineToRelative(0.084f, -0.012f)
                arcToRelative(0.916f, 0.916f, 0.0f, false, true, 0.108f, -0.012f)
                arcToRelative(1.524f, 1.524f, 0.0f, false, true, 1.455f, 2.048f)
                curveToRelative(0.312f, 0.135f, 0.602f, 0.316f, 0.86f, 0.538f)
                arcTo(2.484f, 2.484f, 0.0f, false, false, 12.136f, 4.2f)
                arcToRelative(3.74f, 3.74f, 0.0f, false, false, -6.27f, 0.0f)
                arcToRelative(2.506f, 2.506f, 0.0f, false, false, -0.317f, -0.032f)
                arcTo(2.548f, 2.548f, 0.0f, false, false, 3.0f, 6.717f)
                curveToRelative(0.005f, 0.174f, 0.028f, 0.347f, 0.069f, 0.517f)
                curveToRelative(0.238f, -0.3f, 0.569f, -0.51f, 0.94f, -0.597f)
                horizontalLineToRelative(-0.001f)
                close()
            }
        }
        .build()
        return `__104-fill`!!
    }

private var `__104-fill`: ImageVector? = null
