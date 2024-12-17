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

public val WeatherIcon._2386: ImageVector
    get() {
        if (__2386 != null) {
            return __2386!!
        }
        __2386 = Builder(name = "_2386", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.25f, 10.875f)
                horizontalLineTo(11.0f)
                arcToRelative(0.625f, 0.625f, 0.0f, true, false, -0.59f, -0.833f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, -0.589f, -0.209f)
                arcTo(1.25f, 1.25f, 0.0f, true, true, 11.0f, 11.5f)
                horizontalLineTo(7.25f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, 0.0f, -0.625f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.924f, 10.485f)
                arcToRelative(1.563f, 1.563f, 0.0f, false, true, 3.076f, 0.39f)
                curveToRelative(0.0f, 0.873f, -0.775f, 1.563f, -1.563f, 1.563f)
                lineTo(6.313f, 12.438f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, 0.0f, -0.626f)
                horizontalLineToRelative(8.125f)
                curveToRelative(0.462f, 0.0f, 0.937f, -0.43f, 0.937f, -0.937f)
                arcToRelative(0.937f, 0.937f, 0.0f, false, false, -1.845f, -0.235f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, -0.606f, -0.155f)
                close()
                moveTo(7.563f, 13.063f)
                curveToRelative(0.0f, -0.173f, 0.14f, -0.313f, 0.312f, -0.313f)
                horizontalLineToRelative(5.0f)
                arcToRelative(1.25f, 1.25f, 0.0f, true, true, -1.179f, 1.667f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, 0.59f, -0.209f)
                arcToRelative(0.625f, 0.625f, 0.0f, true, false, 0.589f, -0.833f)
                horizontalLineToRelative(-5.0f)
                arcToRelative(0.312f, 0.312f, 0.0f, false, true, -0.313f, -0.313f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.5f, 0.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, 0.5f)
                verticalLineToRelative(15.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, 1.0f, 0.0f)
                verticalLineTo(9.0f)
                lineToRelative(11.0f, -4.0f)
                lineTo(3.0f, 1.0f)
                verticalLineTo(0.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, -0.5f)
                close()
            }
        }
        .build()
        return __2386!!
    }

private var __2386: ImageVector? = null
