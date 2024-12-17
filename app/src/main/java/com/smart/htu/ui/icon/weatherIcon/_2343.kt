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

public val WeatherIcon._2343: ImageVector
    get() {
        if (__2343 != null) {
            return __2343!!
        }
        __2343 = Builder(name = "_2343", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 2.875f)
                lineTo(14.3f, 13.8f)
                lineTo(1.7f, 13.8f)
                lineTo(8.0f, 2.875f)
                close()
                moveTo(8.455f, 1.261f)
                arcToRelative(0.526f, 0.526f, 0.0f, false, false, -0.91f, 0.0f)
                lineTo(0.07f, 14.224f)
                curveToRelative(-0.2f, 0.346f, 0.052f, 0.776f, 0.454f, 0.776f)
                horizontalLineToRelative(14.952f)
                curveToRelative(0.402f, 0.0f, 0.654f, -0.43f, 0.454f, -0.776f)
                lineTo(8.455f, 1.26f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.382f, 8.102f)
                curveToRelative(-0.074f, 0.0f, -0.122f, -0.08f, -0.089f, -0.15f)
                lineToRelative(1.089f, -2.3f)
                curveToRelative(0.033f, -0.07f, -0.015f, -0.152f, -0.089f, -0.152f)
                horizontalLineToRelative(-2.17f)
                arcToRelative(0.18f, 0.18f, 0.0f, false, false, -0.105f, 0.031f)
                arcToRelative(0.239f, 0.239f, 0.0f, false, false, -0.079f, 0.092f)
                lineToRelative(-1.921f, 4.22f)
                curveToRelative(-0.057f, 0.125f, 0.03f, 0.27f, 0.162f, 0.27f)
                horizontalLineToRelative(1.953f)
                curveToRelative(0.069f, 0.0f, 0.116f, 0.07f, 0.094f, 0.139f)
                lineToRelative(-1.015f, 3.11f)
                curveToRelative(-0.038f, 0.11f, 0.099f, 0.188f, 0.17f, 0.1f)
                lineToRelative(4.095f, -5.189f)
                curveToRelative(0.053f, -0.068f, 0.008f, -0.171f, -0.076f, -0.171f)
                horizontalLineTo(8.382f)
                close()
            }
        }
        .build()
        return __2343!!
    }

private var __2343: ImageVector? = null
