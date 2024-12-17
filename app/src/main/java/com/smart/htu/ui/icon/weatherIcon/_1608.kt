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

public val WeatherIcon._1608: ImageVector
    get() {
        if (__1608 != null) {
            return __1608!!
        }
        __1608 = Builder(name = "_1608", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.487f, 0.349f)
                lineTo(9.948f, 6.18f)
                lineToRelative(2.398f, 0.49f)
                lineToRelative(-6.463f, 6.183f)
                lineToRelative(2.537f, -5.83f)
                lineToRelative(-2.397f, -0.493f)
                lineTo(12.487f, 0.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(4.0f, 2.0f)
                lineToRelative(-0.423f, 6.346f)
                lineTo(6.0f, 8.0f)
                lineToRelative(-4.0f, 8.0f)
                lineToRelative(0.423f, -6.346f)
                lineTo(0.0f, 10.0f)
                lineToRelative(4.0f, -8.0f)
                close()
                moveTo(12.699f, 9.552f)
                arcToRelative(0.23f, 0.23f, 0.0f, false, false, -0.398f, 0.0f)
                lineToRelative(-3.27f, 5.67f)
                arcToRelative(0.227f, 0.227f, 0.0f, false, false, 0.198f, 0.34f)
                horizontalLineToRelative(6.542f)
                arcToRelative(0.227f, 0.227f, 0.0f, false, false, 0.198f, -0.34f)
                lineToRelative(-3.27f, -5.67f)
                close()
                moveTo(11.955f, 11.428f)
                curveToRelative(-0.028f, -0.247f, 0.224f, -0.46f, 0.545f, -0.46f)
                curveToRelative(0.32f, 0.0f, 0.573f, 0.213f, 0.545f, 0.46f)
                lineToRelative(-0.246f, 2.166f)
                lineTo(12.2f, 13.594f)
                lineToRelative(-0.246f, -2.166f)
                close()
                moveTo(12.941f, 14.468f)
                arcToRelative(0.438f, 0.438f, 0.0f, true, true, -0.875f, 0.0f)
                arcToRelative(0.438f, 0.438f, 0.0f, false, true, 0.875f, 0.0f)
                close()
            }
        }
        .build()
        return __1608!!
    }

private var __1608: ImageVector? = null
