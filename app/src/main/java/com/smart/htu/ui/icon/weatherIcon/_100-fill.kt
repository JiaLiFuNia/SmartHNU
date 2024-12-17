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

public val WeatherIcon.`_100-fill`: ImageVector
    get() {
        if (`__100-fill` != null) {
            return `__100-fill`!!
        }
        `__100-fill` = Builder(name = "_100-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.005f, 3.5f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, false, 0.0f, 9.0f)
                arcToRelative(4.5f, 4.5f, 0.0f, false, false, 0.0f, -9.0f)
                close()
                moveTo(8.009f, 2.503f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.5f, -0.5f)
                verticalLineToRelative(-1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 1.0f, 0.0f)
                verticalLineToRelative(1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.5f, 0.5f)
                close()
                moveTo(3.766f, 4.255f)
                arcToRelative(0.498f, 0.498f, 0.0f, false, true, -0.353f, -0.147f)
                lineToRelative(-1.062f, -1.06f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.707f, -0.707f)
                lineTo(4.122f, 3.4f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.355f, 0.854f)
                verticalLineToRelative(0.001f)
                close()
                moveTo(2.004f, 8.493f)
                horizontalLineToRelative(-1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.0f, -1.0f)
                horizontalLineToRelative(1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.0f, 1.0f)
                close()
                moveTo(2.695f, 13.796f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.354f, -0.854f)
                lineToRelative(1.062f, -1.06f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.708f, 0.707f)
                lineToRelative(-1.063f, 1.06f)
                arcToRelative(0.497f, 0.497f, 0.0f, false, true, -0.353f, 0.147f)
                close()
                moveTo(7.996f, 15.997f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.5f, -0.5f)
                verticalLineToRelative(-1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 1.0f, 0.0f)
                verticalLineToRelative(1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.5f, 0.5f)
                close()
                moveTo(13.3f, 13.806f)
                arcToRelative(0.496f, 0.496f, 0.0f, false, true, -0.353f, -0.147f)
                lineToRelative(-1.06f, -1.06f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.706f, -0.707f)
                lineToRelative(1.06f, 1.06f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.353f, 0.854f)
                close()
                moveTo(15.503f, 8.507f)
                horizontalLineToRelative(-1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.0f, -1.0f)
                horizontalLineToRelative(1.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.0f, 1.0f)
                close()
                moveTo(12.25f, 4.265f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.354f, -0.854f)
                lineToRelative(1.06f, -1.06f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.708f, 0.707f)
                lineToRelative(-1.06f, 1.06f)
                arcToRelative(0.498f, 0.498f, 0.0f, false, true, -0.354f, 0.147f)
                close()
            }
        }
        .build()
        return `__100-fill`!!
    }

private var `__100-fill`: ImageVector? = null
