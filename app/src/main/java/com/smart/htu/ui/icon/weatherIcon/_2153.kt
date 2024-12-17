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

public val WeatherIcon._2153: ImageVector
    get() {
        if (__2153 != null) {
            return __2153!!
        }
        __2153 = Builder(name = "_2153", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.88f, 0.721f)
                lineTo(8.651f, 0.0f)
                lineToRelative(3.662f, 6.104f)
                lineToRelative(-2.039f, 1.02f)
                lineToRelative(2.658f, 3.188f)
                lineToRelative(-1.269f, 0.634f)
                lineTo(13.107f, 16.0f)
                lineTo(6.37f, 10.012f)
                lineToRelative(2.059f, -1.03f)
                lineToRelative(-4.376f, -3.28f)
                lineToRelative(3.271f, -1.09f)
                lineTo(2.88f, 0.721f)
                close()
            }
        }
        .build()
        return __2153!!
    }

private var __2153: ImageVector? = null
