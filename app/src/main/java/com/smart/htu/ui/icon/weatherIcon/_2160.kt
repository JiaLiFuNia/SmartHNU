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

public val WeatherIcon._2160: ImageVector
    get() {
        if (__2160 != null) {
            return __2160!!
        }
        __2160 = Builder(name = "_2160", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.068f, 5.982f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 0.2f, -1.518f)
                curveTo(2.768f, 3.598f, 4.625f, 2.604f, 6.0f, 2.0f)
                curveToRelative(0.165f, 1.492f, 0.232f, 3.598f, -0.268f, 4.464f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, -3.664f, -0.482f)
                close()
                moveTo(2.102f, 12.973f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, true, 0.3f, -2.277f)
                curveTo(3.152f, 9.397f, 5.937f, 7.905f, 8.0f, 7.0f)
                curveToRelative(0.248f, 2.239f, 0.348f, 5.397f, -0.402f, 6.696f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, true, -5.496f, -0.723f)
                close()
                moveTo(11.268f, 6.464f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, false, 3.464f, 2.0f)
                curveToRelative(0.5f, -0.866f, 0.433f, -2.972f, 0.268f, -4.464f)
                curveToRelative(-1.375f, 0.603f, -3.232f, 1.598f, -3.732f, 2.464f)
                close()
            }
        }
        .build()
        return __2160!!
    }

private var __2160: ImageVector? = null
