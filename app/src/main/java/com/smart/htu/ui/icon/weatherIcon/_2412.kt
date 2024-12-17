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

public val WeatherIcon._2412: ImageVector
    get() {
        if (__2412 != null) {
            return __2412!!
        }
        __2412 = Builder(name = "_2412", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 16.0f)
                arcTo(8.0f, 8.0f, 0.0f, true, true, 8.0f, 0.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, true, 0.0f, 16.0f)
                close()
                moveTo(8.0f, 14.7f)
                arcTo(6.7f, 6.7f, 0.0f, true, false, 8.0f, 1.3f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, false, 0.0f, 13.4f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.925f, 11.75f)
                curveToRelative(1.152f, 0.0f, 2.183f, -0.52f, 2.87f, -1.337f)
                arcToRelative(2.25f, 2.25f, 0.0f, true, false, 0.415f, -4.223f)
                arcToRelative(3.749f, 3.749f, 0.0f, false, false, -6.554f, -0.029f)
                arcToRelative(2.25f, 2.25f, 0.0f, true, false, 0.44f, 4.3f)
                arcToRelative(3.74f, 3.74f, 0.0f, false, false, 2.829f, 1.289f)
                close()
                moveTo(8.296f, 7.65f)
                curveToRelative(-0.018f, 0.032f, 0.009f, 0.07f, 0.049f, 0.07f)
                horizontalLineToRelative(1.101f)
                curveToRelative(0.046f, 0.0f, 0.07f, 0.048f, 0.042f, 0.08f)
                lineToRelative(-2.234f, 2.432f)
                curveToRelative(-0.039f, 0.042f, -0.114f, 0.005f, -0.093f, -0.047f)
                lineToRelative(0.554f, -1.458f)
                curveToRelative(0.012f, -0.031f, -0.014f, -0.065f, -0.051f, -0.065f)
                lineTo(6.598f, 8.662f)
                curveToRelative(-0.072f, 0.0f, -0.12f, -0.068f, -0.088f, -0.127f)
                lineToRelative(1.048f, -1.977f)
                arcToRelative(0.119f, 0.119f, 0.0f, false, true, 0.043f, -0.043f)
                arcToRelative(0.11f, 0.11f, 0.0f, false, true, 0.057f, -0.015f)
                horizontalLineToRelative(1.184f)
                curveToRelative(0.04f, 0.0f, 0.066f, 0.038f, 0.048f, 0.07f)
                lineToRelative(-0.594f, 1.08f)
                close()
            }
        }
        .build()
        return __2412!!
    }

private var __2412: ImageVector? = null
