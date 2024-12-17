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

public val WeatherIcon._1250: ImageVector
    get() {
        if (__1250 != null) {
            return __1250!!
        }
        __1250 = Builder(name = "_1250", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(5.973f, 10.328f)
                lineTo(9.0f, 4.866f)
                lineTo(7.073f, 0.142f)
                arcToRelative(0.243f, 0.243f, 0.0f, false, false, -0.08f, -0.105f)
                arcTo(0.186f, 0.186f, 0.0f, false, false, 6.88f, 0.0f)
                arcToRelative(0.18f, 0.18f, 0.0f, false, false, -0.113f, 0.042f)
                arcToRelative(0.247f, 0.247f, 0.0f, false, false, -0.077f, 0.107f)
                lineTo(0.022f, 15.635f)
                arcToRelative(0.296f, 0.296f, 0.0f, false, false, 0.01f, 0.246f)
                arcToRelative(0.232f, 0.232f, 0.0f, false, false, 0.077f, 0.088f)
                curveToRelative(0.031f, 0.02f, 0.067f, 0.031f, 0.103f, 0.031f)
                lineToRelative(4.696f, -0.016f)
                lineToRelative(1.76f, -3.927f)
                lineToRelative(-0.695f, -1.729f)
                close()
                moveTo(15.982f, 14.676f)
                lineTo(13.5f, 10.0f)
                lineTo(10.0f, 14.969f)
                lineToRelative(5.82f, 0.031f)
                curveToRelative(0.132f, 0.0f, 0.22f, -0.174f, 0.162f, -0.324f)
                close()
                moveTo(12.065f, 3.0f)
                lineToRelative(-0.46f, 4.56f)
                lineToRelative(1.748f, 0.146f)
                lineTo(7.7f, 15.068f)
                lineToRelative(1.81f, -5.23f)
                lineToRelative(-1.606f, 0.212f)
                lineTo(12.065f, 3.0f)
                close()
            }
        }
        .build()
        return __1250!!
    }

private var __1250: ImageVector? = null
