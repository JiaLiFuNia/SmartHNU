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

public val WeatherIcon.`_502-fill`: ImageVector
    get() {
        if (`__502-fill` != null) {
            return `__502-fill`!!
        }
        `__502-fill` = Builder(name = "_502-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
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
            }
        }
        .build()
        return `__502-fill`!!
    }

private var `__502-fill`: ImageVector? = null
