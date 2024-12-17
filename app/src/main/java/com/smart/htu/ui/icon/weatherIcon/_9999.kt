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

public val WeatherIcon._9999: ImageVector
    get() {
        if (__9999 != null) {
            return __9999!!
        }
        __9999 = Builder(name = "_9999", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.455f, 1.261f)
                arcToRelative(0.526f, 0.526f, 0.0f, false, false, -0.91f, 0.0f)
                lineTo(0.07f, 14.224f)
                curveToRelative(-0.2f, 0.346f, 0.052f, 0.776f, 0.454f, 0.776f)
                horizontalLineToRelative(14.952f)
                curveToRelative(0.402f, 0.0f, 0.654f, -0.43f, 0.454f, -0.776f)
                lineTo(8.455f, 1.26f)
                close()
                moveTo(6.755f, 5.549f)
                curveTo(6.69f, 4.985f, 7.267f, 4.5f, 8.0f, 4.5f)
                curveToRelative(0.733f, 0.0f, 1.31f, 0.485f, 1.245f, 1.049f)
                lineTo(8.682f, 10.5f)
                lineTo(7.318f, 10.5f)
                lineToRelative(-0.563f, -4.951f)
                close()
                moveTo(9.008f, 12.5f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, 2.0f, 0.0f)
                close()
            }
        }
        .build()
        return __9999!!
    }

private var __9999: ImageVector? = null
