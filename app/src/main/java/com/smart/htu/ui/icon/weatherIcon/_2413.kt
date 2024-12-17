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

public val WeatherIcon._2413: ImageVector
    get() {
        if (__2413 != null) {
            return __2413!!
        }
        __2413 = Builder(name = "_2413", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.053f, 14.7f)
                curveToRelative(3.153f, -1.698f, 5.733f, -4.387f, 8.965f, -9.025f)
                curveToRelative(0.149f, -0.213f, -0.39f, 0.786f, -0.504f, 1.018f)
                curveToRelative(-1.57f, 3.174f, -3.59f, 5.201f, -4.882f, 6.422f)
                arcToRelative(0.396f, 0.396f, 0.0f, false, false, 0.094f, 0.643f)
                curveToRelative(1.17f, 0.62f, 3.94f, 1.6f, 6.033f, -2.04f)
                curveTo(14.612f, 8.497f, 14.906f, 3.573f, 15.0f, 1.21f)
                curveToRelative(0.017f, -0.416f, -0.248f, -0.138f, -0.53f, 0.188f)
                curveToRelative(-1.024f, 1.187f, -3.946f, 1.442f, -6.009f, 2.568f)
                curveToRelative(-4.27f, 2.33f, -4.632f, 5.305f, -3.61f, 8.04f)
                arcToRelative(0.43f, 0.43f, 0.0f, false, true, 0.003f, 0.296f)
                curveToRelative(-0.034f, 0.096f, -0.734f, 0.878f, -0.821f, 0.93f)
                curveToRelative(-0.581f, 0.355f, -1.47f, 0.922f, -2.447f, 0.978f)
                curveToRelative(-1.591f, 0.092f, 0.458f, 1.031f, 1.468f, 0.49f)
                close()
            }
        }
        .build()
        return __2413!!
    }

private var __2413: ImageVector? = null
