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

public val WeatherIcon._1037: ImageVector
    get() {
        if (__1037 != null) {
            return __1037!!
        }
        __1037 = Builder(name = "_1037", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(9.0f, 4.866f)
                lineToRelative(-3.027f, 5.462f)
                lineToRelative(0.696f, 1.729f)
                lineToRelative(-1.761f, 3.927f)
                lineTo(0.212f, 16.0f)
                arcToRelative(0.184f, 0.184f, 0.0f, false, true, -0.103f, -0.031f)
                arcToRelative(0.232f, 0.232f, 0.0f, false, true, -0.077f, -0.088f)
                arcToRelative(0.29f, 0.29f, 0.0f, false, true, -0.01f, -0.246f)
                lineTo(6.69f, 0.149f)
                arcToRelative(0.247f, 0.247f, 0.0f, false, true, 0.077f, -0.107f)
                arcTo(0.188f, 0.188f, 0.0f, false, true, 6.88f, 0.0f)
                curveToRelative(0.04f, 0.0f, 0.08f, 0.012f, 0.113f, 0.037f)
                arcToRelative(0.243f, 0.243f, 0.0f, false, true, 0.08f, 0.105f)
                lineTo(9.0f, 4.866f)
                close()
                moveTo(13.5f, 11.0f)
                lineToRelative(2.482f, 4.676f)
                curveToRelative(0.057f, 0.15f, -0.03f, 0.324f, -0.163f, 0.324f)
                lineTo(10.0f, 15.969f)
                lineTo(13.5f, 11.0f)
                close()
                moveTo(11.905f, 7.56f)
                lineTo(12.365f, 3.0f)
                lineTo(8.205f, 10.05f)
                lineTo(9.81f, 9.838f)
                lineTo(8.0f, 15.068f)
                lineToRelative(5.653f, -7.362f)
                lineToRelative(-1.748f, -0.145f)
                close()
            }
        }
        .build()
        return __1037!!
    }

private var __1037: ImageVector? = null
