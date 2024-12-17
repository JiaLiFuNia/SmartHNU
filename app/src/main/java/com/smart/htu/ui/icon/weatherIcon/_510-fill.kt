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

public val WeatherIcon.`_510-fill`: ImageVector
    get() {
        if (`__510-fill` != null) {
            return `__510-fill`!!
        }
        `__510-fill` = Builder(name = "_510-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.25f, 11.0f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(9.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                lineTo(0.25f, 11.0f)
                close()
                moveTo(3.25f, 12.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-3.5f)
                close()
                moveTo(10.0f, 14.25f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.25f, -0.25f)
                horizontalLineToRelative(3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, 0.5f)
                horizontalLineToRelative(-3.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.25f, -0.25f)
                close()
                moveTo(8.25f, 12.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-7.5f)
                close()
                moveTo(1.0f, 14.25f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.25f, -0.25f)
                horizontalLineToRelative(7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, 0.5f)
                horizontalLineToRelative(-7.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.25f, -0.25f)
                close()
                moveTo(3.25f, 15.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, 0.5f)
                horizontalLineToRelative(9.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, false, 0.0f, -0.5f)
                horizontalLineToRelative(-9.5f)
                close()
                moveTo(7.9f, 10.0f)
                arcToRelative(4.99f, 4.99f, 0.0f, false, false, 3.827f, -1.783f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, false, 4.215f, -3.307f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, -0.192f, 0.09f)
                horizontalLineToRelative(-5.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, -0.5f)
                horizontalLineToRelative(5.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.084f, 0.015f)
                arcTo(3.008f, 3.008f, 0.0f, false, false, 14.66f, 3.0f)
                horizontalLineToRelative(-4.41f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, -0.5f)
                horizontalLineToRelative(1.981f)
                arcTo(4.998f, 4.998f, 0.0f, false, false, 7.9f, 0.0f)
                arcToRelative(4.998f, 4.998f, 0.0f, false, false, -4.359f, 2.549f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.586f, 5.732f)
                arcTo(4.988f, 4.988f, 0.0f, false, false, 7.9f, 10.0f)
                close()
                moveTo(8.0f, 3.75f)
                arcToRelative(0.25f, 0.25f, 0.0f, false, true, 0.25f, -0.25f)
                horizontalLineToRelative(5.5f)
                arcToRelative(0.25f, 0.25f, 0.0f, true, true, 0.0f, 0.5f)
                horizontalLineToRelative(-5.5f)
                arcTo(0.25f, 0.25f, 0.0f, false, true, 8.0f, 3.75f)
                close()
            }
        }
        .build()
        return `__510-fill`!!
    }

private var `__510-fill`: ImageVector? = null
