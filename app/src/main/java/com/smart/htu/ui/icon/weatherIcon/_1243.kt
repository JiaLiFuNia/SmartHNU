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

public val WeatherIcon._1243: ImageVector
    get() {
        if (__1243 != null) {
            return __1243!!
        }
        __1243 = Builder(name = "_1243", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.0f, 16.0f)
                lineTo(8.0f, 0.0f)
                verticalLineToRelative(3.2f)
                lineToRelative(1.263f, 2.133f)
                lineTo(8.0f, 6.4f)
                lineToRelative(-0.842f, 3.2f)
                lineTo(8.0f, 10.667f)
                lineToRelative(-0.842f, 2.666f)
                lineToRelative(2.105f, 1.6f)
                lineToRelative(0.842f, 1.067f)
                horizontalLineTo(0.0f)
                close()
                moveTo(9.684f, 2.133f)
                lineToRelative(0.421f, 2.134f)
                horizontalLineToRelative(1.263f)
                lineTo(9.684f, 2.133f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(9.263f, 8.0f)
                lineToRelative(1.684f, -1.067f)
                lineToRelative(0.421f, 3.2f)
                lineTo(9.263f, 9.6f)
                lineTo(9.263f, 8.0f)
                close()
                moveTo(12.632f, 10.133f)
                lineTo(11.79f, 11.733f)
                lineTo(13.474f, 12.8f)
                lineTo(12.632f, 10.133f)
                close()
                moveTo(9.263f, 13.867f)
                lineTo(10.105f, 12.267f)
                lineTo(13.053f, 14.4f)
                lineTo(10.947f, 15.467f)
                lineTo(9.263f, 13.867f)
                close()
                moveTo(14.316f, 14.4f)
                lineTo(15.158f, 13.333f)
                lineTo(15.578f, 15.467f)
                lineTo(14.316f, 14.4f)
                close()
            }
        }
        .build()
        return __1243!!
    }

private var __1243: ImageVector? = null
