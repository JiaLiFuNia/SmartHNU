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

public val WeatherIcon.`_399-fill`: ImageVector
    get() {
        if (`__399-fill` != null) {
            return `__399-fill`!!
        }
        `__399-fill` = Builder(name = "_399-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(1.464f, 14.536f)
                arcTo(5.0f, 5.0f, 0.0f, false, true, 0.0f, 11.0f)
                curveTo(0.0f, 8.5f, 2.777f, 4.025f, 5.0f, 1.0f)
                curveToRelative(2.223f, 3.025f, 5.0f, 7.5f, 5.0f, 10.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, false, true, -8.536f, 3.536f)
                close()
                moveTo(10.879f, 7.219f)
                curveToRelative(-0.563f, -0.5f, -0.879f, -1.178f, -0.879f, -1.886f)
                curveTo(10.0f, 4.0f, 11.666f, 1.613f, 13.0f, 0.0f)
                curveToRelative(1.334f, 1.613f, 3.0f, 4.0f, 3.0f, 5.333f)
                curveToRelative(0.0f, 0.708f, -0.316f, 1.386f, -0.879f, 1.886f)
                curveTo(14.56f, 7.719f, 13.796f, 8.0f, 13.0f, 8.0f)
                reflectiveCurveToRelative(-1.559f, -0.28f, -2.121f, -0.781f)
                close()
            }
        }
        .build()
        return `__399-fill`!!
    }

private var `__399-fill`: ImageVector? = null
