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

public val WeatherIcon._2421: ImageVector
    get() {
        if (__2421 != null) {
            return __2421!!
        }
        __2421 = Builder(name = "_2421", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.659f, 11.25f)
                horizontalLineToRelative(1.29f)
                lineTo(5.949f, 5.873f)
                horizontalLineToRelative(1.66f)
                lineTo(7.609f, 4.75f)
                lineTo(3.0f, 4.75f)
                verticalLineToRelative(1.123f)
                horizontalLineToRelative(1.659f)
                verticalLineToRelative(5.377f)
                close()
                moveTo(9.99f, 4.75f)
                lineTo(8.605f, 4.75f)
                verticalLineToRelative(6.5f)
                lineTo(9.99f, 11.25f)
                curveToRelative(1.068f, 0.0f, 1.837f, -0.254f, 2.306f, -0.762f)
                curveToRelative(0.47f, -0.51f, 0.705f, -1.342f, 0.705f, -2.495f)
                curveToRelative(0.0f, -1.149f, -0.235f, -1.976f, -0.705f, -2.481f)
                curveToRelative(-0.47f, -0.508f, -1.238f, -0.762f, -2.306f, -0.762f)
                close()
                moveTo(10.247f, 10.092f)
                horizontalLineToRelative(-0.35f)
                lineTo(9.897f, 5.908f)
                horizontalLineToRelative(0.35f)
                curveToRelative(0.508f, 0.0f, 0.87f, 0.158f, 1.086f, 0.475f)
                curveToRelative(0.216f, 0.316f, 0.323f, 0.853f, 0.323f, 1.61f)
                curveToRelative(0.0f, 0.764f, -0.107f, 1.305f, -0.323f, 1.624f)
                curveToRelative(-0.216f, 0.317f, -0.578f, 0.475f, -1.086f, 0.475f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.0f, 8.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 16.0f, 0.0f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 0.0f, 8.0f)
                close()
                moveTo(14.7f, 8.0f)
                arcTo(6.7f, 6.7f, 0.0f, true, true, 1.3f, 8.0f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, true, 13.4f, 0.0f)
                close()
            }
        }
        .build()
        return __2421!!
    }

private var __2421: ImageVector? = null
