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

public val WeatherIcon._150: ImageVector
    get() {
        if (__150 != null) {
            return __150!!
        }
        __150 = Builder(name = "_150", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.403f, 1.393f)
                curveToRelative(-0.448f, 2.58f, -0.261f, 5.558f, 1.873f, 7.797f)
                curveToRelative(2.09f, 2.192f, 5.477f, 3.06f, 8.284f, 2.851f)
                curveToRelative(-0.255f, 0.333f, -0.543f, 0.65f, -0.863f, 0.946f)
                curveToRelative(-3.035f, 2.808f, -7.81f, 2.66f, -10.66f, -0.33f)
                arcToRelative(7.323f, 7.323f, 0.0f, false, true, 0.334f, -10.463f)
                arcToRelative(7.57f, 7.57f, 0.0f, false, true, 1.032f, -0.801f)
                close()
                moveTo(5.544f, 0.79f)
                curveToRelative(0.114f, -0.494f, -0.351f, -0.958f, -0.811f, -0.732f)
                arcToRelative(8.538f, 8.538f, 0.0f, false, false, -2.04f, 1.401f)
                arcToRelative(8.323f, 8.323f, 0.0f, false, false, -0.38f, 11.887f)
                curveToRelative(3.227f, 3.386f, 8.628f, 3.553f, 12.064f, 0.374f)
                arcToRelative(8.432f, 8.432f, 0.0f, false, false, 1.547f, -1.92f)
                curveToRelative(0.258f, -0.438f, -0.183f, -0.924f, -0.69f, -0.843f)
                curveToRelative(-2.705f, 0.43f, -6.217f, -0.342f, -8.234f, -2.458f)
                curveTo(4.983f, 6.384f, 4.939f, 3.424f, 5.544f, 0.79f)
                close()
            }
        }
        .build()
        return __150!!
    }

private var __150: ImageVector? = null
