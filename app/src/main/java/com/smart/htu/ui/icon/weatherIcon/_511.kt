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

public val WeatherIcon._511: ImageVector
    get() {
        if (__511 != null) {
            return __511!!
        }
        __511 = Builder(name = "_511", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.543f, 4.487f)
                curveToRelative(-1.581f, 0.0f, -3.876f, 1.712f, -4.57f, 2.888f)
                curveTo(7.282f, 6.2f, 4.987f, 4.487f, 3.406f, 4.487f)
                arcToRelative(3.486f, 3.486f, 0.0f, false, false, 0.0f, 6.97f)
                curveToRelative(1.58f, 0.0f, 3.876f, -1.75f, 4.569f, -2.906f)
                curveToRelative(0.693f, 1.156f, 2.988f, 2.906f, 4.569f, 2.906f)
                arcToRelative(3.486f, 3.486f, 0.0f, false, false, 0.0f, -6.97f)
                horizontalLineToRelative(-0.001f)
                close()
                moveTo(3.405f, 10.457f)
                arcToRelative(2.486f, 2.486f, 0.0f, false, true, 0.0f, -4.97f)
                curveToRelative(1.309f, 0.0f, 3.462f, 1.745f, 3.762f, 2.476f)
                curveToRelative(-0.306f, 0.675f, -2.418f, 2.494f, -3.762f, 2.494f)
                close()
                moveTo(12.543f, 10.457f)
                curveToRelative(-1.345f, 0.0f, -3.457f, -1.82f, -3.762f, -2.494f)
                curveToRelative(0.3f, -0.73f, 2.452f, -2.476f, 3.762f, -2.476f)
                arcToRelative(2.486f, 2.486f, 0.0f, false, true, 0.0f, 4.97f)
                close()
                moveTo(3.989f, 2.481f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, 2.0f, 0.0f)
                close()
                moveTo(14.0f, 2.481f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, 2.0f, 0.0f)
                close()
                moveTo(2.989f, 14.457f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 0.0f, -2.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 0.0f, 2.0f)
                close()
                moveTo(14.0f, 13.457f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, 2.0f, 0.0f)
                close()
            }
        }
        .build()
        return __511!!
    }

private var __511: ImageVector? = null
