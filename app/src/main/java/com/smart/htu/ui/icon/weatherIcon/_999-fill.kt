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

public val WeatherIcon.`_999-fill`: ImageVector
    get() {
        if (`__999-fill` != null) {
            return `__999-fill`!!
        }
        `__999-fill` = Builder(name = "_999-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.9f, 13.0f)
                arcToRelative(4.99f, 4.99f, 0.0f, false, false, 3.827f, -1.783f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.553f, -5.63f)
                arcTo(4.999f, 4.999f, 0.0f, false, false, 7.9f, 3.0f)
                arcToRelative(4.998f, 4.998f, 0.0f, false, false, -4.359f, 2.549f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.586f, 5.732f)
                arcTo(4.988f, 4.988f, 0.0f, false, false, 7.9f, 13.0f)
                close()
                moveTo(8.038f, 9.497f)
                horizontalLineToRelative(-0.064f)
                curveToRelative(-0.378f, 0.0f, -0.69f, -0.296f, -0.641f, -0.648f)
                arcToRelative(1.8f, 1.8f, 0.0f, false, true, 0.109f, -0.447f)
                curveToRelative(0.087f, -0.204f, 0.31f, -0.437f, 0.671f, -0.7f)
                lineToRelative(0.366f, -0.268f)
                arcToRelative(1.24f, 1.24f, 0.0f, false, false, 0.291f, -0.278f)
                arcToRelative(0.907f, 0.907f, 0.0f, false, false, 0.2f, -0.569f)
                curveToRelative(0.0f, -0.238f, -0.074f, -0.454f, -0.225f, -0.648f)
                curveToRelative(-0.147f, -0.198f, -0.418f, -0.297f, -0.812f, -0.297f)
                curveToRelative(-0.388f, 0.0f, -0.663f, 0.121f, -0.827f, 0.362f)
                arcToRelative(1.65f, 1.65f, 0.0f, false, false, -0.07f, 0.114f)
                curveToRelative(-0.18f, 0.326f, -0.488f, 0.639f, -0.88f, 0.639f)
                curveToRelative(-0.404f, 0.0f, -0.738f, -0.32f, -0.638f, -0.686f)
                curveToRelative(0.146f, -0.54f, 0.445f, -0.945f, 0.896f, -1.214f)
                curveToRelative(0.394f, -0.238f, 0.879f, -0.357f, 1.454f, -0.357f)
                curveToRelative(0.755f, 0.0f, 1.382f, 0.17f, 1.88f, 0.508f)
                curveToRelative(0.501f, 0.338f, 0.752f, 0.84f, 0.752f, 1.504f)
                curveToRelative(0.0f, 0.408f, -0.109f, 0.75f, -0.326f, 1.03f)
                curveToRelative(-0.127f, 0.169f, -0.37f, 0.385f, -0.732f, 0.648f)
                lineToRelative(-0.356f, 0.259f)
                arcToRelative(1.016f, 1.016f, 0.0f, false, false, -0.386f, 0.494f)
                curveToRelative(-0.1f, 0.295f, -0.331f, 0.554f, -0.662f, 0.554f)
                close()
                moveTo(8.016f, 11.5f)
                lineTo(7.97f, 11.5f)
                curveToRelative(-0.4f, 0.0f, -0.725f, -0.304f, -0.725f, -0.68f)
                curveToRelative(0.0f, -0.375f, 0.324f, -0.679f, 0.725f, -0.679f)
                horizontalLineToRelative(0.045f)
                curveToRelative(0.4f, 0.0f, 0.724f, 0.304f, 0.724f, 0.68f)
                curveToRelative(0.0f, 0.375f, -0.324f, 0.679f, -0.724f, 0.679f)
                close()
            }
        }
        .build()
        return `__999-fill`!!
    }

private var `__999-fill`: ImageVector? = null
