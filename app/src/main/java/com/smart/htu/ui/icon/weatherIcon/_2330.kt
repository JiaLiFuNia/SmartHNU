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

public val WeatherIcon._2330: ImageVector
    get() {
        if (__2330 != null) {
            return __2330!!
        }
        __2330 = Builder(name = "_2330", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.408f, 3.5f)
                curveTo(5.28f, 1.667f, 8.174f, 0.5f, 12.086f, 0.0f)
                lineToRelative(-2.009f, 1.967f)
                arcToRelative(6.495f, 6.495f, 0.0f, false, true, 2.52f, 1.533f)
                arcToRelative(6.274f, 6.274f, 0.0f, false, true, 0.0f, 9.0f)
                curveToRelative(-1.871f, 1.833f, -4.765f, 3.0f, -8.678f, 3.5f)
                lineToRelative(2.01f, -1.967f)
                arcTo(6.493f, 6.493f, 0.0f, false, true, 3.408f, 12.5f)
                arcToRelative(6.261f, 6.261f, 0.0f, false, true, 0.0f, -9.0f)
                close()
                moveTo(8.17f, 5.098f)
                arcToRelative(0.197f, 0.197f, 0.0f, false, false, -0.34f, 0.0f)
                lineToRelative(-2.804f, 4.86f)
                curveToRelative(-0.075f, 0.13f, 0.02f, 0.292f, 0.17f, 0.292f)
                horizontalLineToRelative(5.607f)
                arcToRelative(0.194f, 0.194f, 0.0f, false, false, 0.17f, -0.291f)
                lineTo(8.17f, 5.098f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 6.2f)
                curveToRelative(-0.293f, 0.0f, -0.524f, 0.194f, -0.498f, 0.42f)
                lineToRelative(0.225f, 1.98f)
                horizontalLineToRelative(0.546f)
                lineToRelative(0.225f, -1.98f)
                curveToRelative(0.026f, -0.226f, -0.205f, -0.42f, -0.498f, -0.42f)
                close()
                moveTo(8.003f, 9.8f)
                arcToRelative(0.4f, 0.4f, 0.0f, true, false, 0.0f, -0.8f)
                arcToRelative(0.4f, 0.4f, 0.0f, false, false, 0.0f, 0.8f)
                close()
            }
        }
        .build()
        return __2330!!
    }

private var __2330: ImageVector? = null
