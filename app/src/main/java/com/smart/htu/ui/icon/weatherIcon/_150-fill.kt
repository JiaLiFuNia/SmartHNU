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

public val WeatherIcon.`_150-fill`: ImageVector
    get() {
        if (`__150-fill` != null) {
            return `__150-fill`!!
        }
        `__150-fill` = Builder(name = "_150-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.733f, 0.059f)
                curveToRelative(0.46f, -0.226f, 0.925f, 0.238f, 0.811f, 0.732f)
                curveTo(4.94f, 3.424f, 4.984f, 6.384f, 7.0f, 8.5f)
                curveToRelative(2.017f, 2.116f, 5.529f, 2.888f, 8.234f, 2.458f)
                curveToRelative(0.507f, -0.08f, 0.948f, 0.405f, 0.69f, 0.844f)
                arcToRelative(8.432f, 8.432f, 0.0f, false, true, -1.547f, 1.919f)
                curveTo(10.94f, 16.9f, 5.54f, 16.733f, 2.313f, 13.347f)
                arcToRelative(8.323f, 8.323f, 0.0f, false, true, 0.38f, -11.887f)
                arcTo(8.538f, 8.538f, 0.0f, false, true, 4.732f, 0.06f)
                close()
            }
        }
        .build()
        return `__150-fill`!!
    }

private var `__150-fill`: ImageVector? = null
