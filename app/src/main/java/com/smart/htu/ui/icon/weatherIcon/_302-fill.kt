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

public val WeatherIcon.`_302-fill`: ImageVector
    get() {
        if (`__302-fill` != null) {
            return `__302-fill`!!
        }
        `__302-fill` = Builder(name = "_302-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.0f, 13.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(11.0f, 13.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(8.46f, 12.626f)
                curveToRelative(-0.054f, 0.0f, -0.089f, -0.05f, -0.065f, -0.093f)
                lineToRelative(0.792f, -1.438f)
                curveTo(9.21f, 11.05f, 9.176f, 11.0f, 9.122f, 11.0f)
                lineTo(7.544f, 11.0f)
                arcToRelative(0.147f, 0.147f, 0.0f, false, false, -0.076f, 0.02f)
                arcToRelative(0.158f, 0.158f, 0.0f, false, false, -0.058f, 0.057f)
                lineToRelative(-1.397f, 2.637f)
                curveToRelative(-0.042f, 0.079f, 0.022f, 0.17f, 0.118f, 0.17f)
                horizontalLineToRelative(1.42f)
                curveToRelative(0.05f, 0.0f, 0.084f, 0.043f, 0.069f, 0.086f)
                lineToRelative(-0.739f, 1.943f)
                curveToRelative(-0.027f, 0.07f, 0.072f, 0.118f, 0.124f, 0.063f)
                lineToRelative(2.978f, -3.243f)
                curveToRelative(0.04f, -0.042f, 0.006f, -0.107f, -0.055f, -0.107f)
                lineTo(8.46f, 12.626f)
                close()
                moveTo(11.727f, 8.217f)
                arcTo(4.99f, 4.99f, 0.0f, false, true, 7.9f, 10.0f)
                arcToRelative(4.988f, 4.988f, 0.0f, false, true, -3.773f, -1.719f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.586f, -5.732f)
                arcTo(4.998f, 4.998f, 0.0f, false, true, 7.9f, 0.0f)
                arcToRelative(4.999f, 4.999f, 0.0f, false, true, 4.38f, 2.587f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -0.553f, 5.63f)
                close()
            }
        }
        .build()
        return `__302-fill`!!
    }

private var `__302-fill`: ImageVector? = null
