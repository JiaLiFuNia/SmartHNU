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

public val WeatherIcon.`_901-fill`: ImageVector
    get() {
        if (`__901-fill` != null) {
            return `__901-fill`!!
        }
        `__901-fill` = Builder(name = "_901-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.5f, 0.0f)
                arcTo(2.5f, 2.5f, 0.0f, false, false, 9.0f, 2.5f)
                verticalLineToRelative(4.99f)
                arcToRelative(0.534f, 0.534f, 0.0f, false, true, -0.217f, 0.423f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, false, 5.435f, 0.0f)
                arcTo(0.534f, 0.534f, 0.0f, false, true, 14.0f, 7.49f)
                lineTo(14.0f, 2.5f)
                arcTo(2.5f, 2.5f, 0.0f, false, false, 11.5f, 0.0f)
                close()
                moveTo(13.5f, 11.5f)
                arcTo(2.0f, 2.0f, 0.0f, true, true, 11.0f, 9.563f)
                lineTo(11.0f, 7.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 1.0f, 0.0f)
                verticalLineToRelative(2.063f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 1.5f, 1.937f)
                close()
                moveTo(2.302f, 1.7f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, 1.4f, 0.0f)
                verticalLineToRelative(1.088f)
                lineToRelative(0.942f, -0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, 0.7f, 1.212f)
                lineTo(4.402f, 4.0f)
                lineToRelative(0.942f, 0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, -0.7f, 1.212f)
                lineToRelative(-0.942f, -0.544f)
                lineTo(3.702f, 6.3f)
                arcToRelative(0.7f, 0.7f, 0.0f, true, true, -1.4f, 0.0f)
                lineTo(2.302f, 5.212f)
                lineToRelative(-0.942f, 0.544f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, -0.7f, -1.212f)
                lineTo(1.602f, 4.0f)
                lineTo(0.66f, 3.456f)
                arcToRelative(0.7f, 0.7f, 0.0f, false, true, 0.7f, -1.212f)
                lineToRelative(0.942f, 0.544f)
                lineTo(2.302f, 1.7f)
                close()
            }
        }
        .build()
        return `__901-fill`!!
    }

private var `__901-fill`: ImageVector? = null
