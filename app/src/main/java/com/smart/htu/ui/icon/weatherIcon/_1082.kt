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

public val WeatherIcon._1082: ImageVector
    get() {
        if (__1082 != null) {
            return __1082!!
        }
        __1082 = Builder(name = "_1082", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(5.109f, 4.132f)
                curveToRelative(-0.057f, -0.362f, 0.17f, -0.8f, 0.496f, -0.997f)
                curveToRelative(0.256f, -0.153f, 0.551f, -0.133f, 0.806f, 0.023f)
                lineToRelative(0.07f, 0.042f)
                arcToRelative(0.846f, 0.846f, 0.0f, false, true, 0.409f, 0.853f)
                lineTo(6.532f, 6.33f)
                horizontalLineTo(5.454f)
                lineTo(5.11f, 4.132f)
                close()
                moveTo(6.6f, 7.43f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, true, -1.2f, 0.0f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, true, 1.2f, 0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.178f, 0.0f)
                curveToRelative(1.097f, 0.0f, 2.136f, 0.21f, 3.063f, 0.584f)
                curveToRelative(2.452f, 0.97f, 4.15f, 3.083f, 4.15f, 5.536f)
                verticalLineToRelative(1.014f)
                lineToRelative(1.462f, 2.378f)
                curveToRelative(0.37f, 0.6f, 0.019f, 1.092f, -0.774f, 1.092f)
                horizontalLineToRelative(-0.689f)
                verticalLineToRelative(2.26f)
                curveToRelative(0.0f, 0.705f, -0.677f, 1.286f, -1.508f, 1.286f)
                lineToRelative(-2.071f, -0.263f)
                lineToRelative(-0.004f, 1.765f)
                verticalLineToRelative(0.013f)
                curveToRelative(-0.011f, 0.226f, -0.521f, 0.342f, -0.512f, 0.335f)
                horizontalLineToRelative(-7.97f)
                verticalLineToRelative(-5.38f)
                curveTo(0.897f, 9.502f, 0.0f, 7.899f, 0.0f, 6.114f)
                curveTo(0.004f, 2.735f, 3.217f, 0.0f, 7.178f, 0.0f)
                close()
                moveTo(10.0f, 5.53f)
                arcToRelative(4.0f, 4.0f, 0.0f, true, false, -8.0f, 0.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, false, false, 8.0f, 0.0f)
                close()
            }
        }
        .build()
        return __1082!!
    }

private var __1082: ImageVector? = null
