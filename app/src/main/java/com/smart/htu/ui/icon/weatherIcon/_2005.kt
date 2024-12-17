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

public val WeatherIcon._2005: ImageVector
    get() {
        if (__2005 != null) {
            return __2005!!
        }
        __2005 = Builder(name = "_2005", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.406f, 7.086f)
                curveToRelative(-0.229f, -1.143f, 0.457f, -2.972f, 2.514f, -3.657f)
                curveToRelative(0.0f, 1.696f, 0.385f, 2.686f, 0.748f, 3.616f)
                curveToRelative(0.32f, 0.824f, 0.623f, 1.602f, 0.623f, 2.784f)
                arcTo(6.146f, 6.146f, 0.0f, false, true, 8.12f, 16.0f)
                curveToRelative(-3.429f, 0.0f, -6.4f, -2.514f, -6.4f, -6.171f)
                curveToRelative(0.0f, -1.6f, 0.686f, -3.429f, 2.057f, -4.115f)
                curveToRelative(0.0f, 0.0f, 0.229f, 2.286f, 1.372f, 3.429f)
                curveToRelative(0.042f, -0.337f, 0.037f, -0.799f, 0.032f, -1.34f)
                curveTo(5.158f, 5.416f, 5.12f, 1.492f, 9.034f, 0.0f)
                curveToRelative(-0.228f, 3.2f, -0.228f, 5.257f, 1.372f, 7.086f)
                close()
                moveTo(9.05f, 11.1f)
                curveToRelative(-0.7f, -0.8f, -0.7f, -1.7f, -0.6f, -3.1f)
                curveToRelative(-1.713f, 0.652f, -1.696f, 2.37f, -1.686f, 3.414f)
                curveToRelative(0.002f, 0.236f, 0.004f, 0.438f, -0.014f, 0.586f)
                curveToRelative(-0.5f, -0.5f, -0.6f, -1.5f, -0.6f, -1.5f)
                curveToRelative(-0.6f, 0.3f, -0.9f, 1.1f, -0.9f, 1.8f)
                curveToRelative(0.0f, 1.6f, 1.3f, 2.7f, 2.8f, 2.7f)
                curveToRelative(1.5f, 0.0f, 2.7f, -1.2f, 2.7f, -2.7f)
                curveToRelative(0.0f, -0.517f, -0.133f, -0.857f, -0.273f, -1.218f)
                curveToRelative(-0.158f, -0.407f, -0.327f, -0.84f, -0.327f, -1.582f)
                curveToRelative(-0.9f, 0.3f, -1.2f, 1.1f, -1.1f, 1.6f)
                close()
            }
        }
        .build()
        return __2005!!
    }

private var __2005: ImageVector? = null
