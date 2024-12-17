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

public val WeatherIcon.`_404-fill`: ImageVector
    get() {
        if (`__404-fill` != null) {
            return `__404-fill`!!
        }
        `__404-fill` = Builder(name = "_404-fill", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(1.0f, 13.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(13.0f, 13.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(5.151f, 13.35f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, 0.7f, 0.0f)
                verticalLineToRelative(0.544f)
                lineToRelative(0.47f, -0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, 0.35f, 0.606f)
                lineToRelative(-0.47f, 0.272f)
                lineToRelative(0.47f, 0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, -0.35f, 0.606f)
                lineToRelative(-0.47f, -0.272f)
                verticalLineToRelative(0.544f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, -0.7f, 0.0f)
                verticalLineToRelative(-0.544f)
                lineToRelative(-0.47f, 0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, -0.35f, -0.606f)
                lineToRelative(0.47f, -0.272f)
                lineToRelative(-0.47f, -0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, true, 0.35f, -0.606f)
                lineToRelative(0.47f, 0.272f)
                verticalLineToRelative(-0.544f)
                close()
                moveTo(10.501f, 13.0f)
                arcToRelative(0.35f, 0.35f, 0.0f, false, false, -0.35f, 0.35f)
                verticalLineToRelative(0.544f)
                lineToRelative(-0.47f, -0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, false, -0.35f, 0.606f)
                lineToRelative(0.47f, 0.272f)
                lineToRelative(-0.47f, 0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, false, 0.35f, 0.606f)
                lineToRelative(0.47f, -0.272f)
                verticalLineToRelative(0.544f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, false, 0.7f, 0.0f)
                verticalLineToRelative(-0.544f)
                lineToRelative(0.47f, 0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, false, 0.35f, -0.606f)
                lineToRelative(-0.47f, -0.272f)
                lineToRelative(0.47f, -0.272f)
                arcToRelative(0.35f, 0.35f, 0.0f, true, false, -0.35f, -0.606f)
                lineToRelative(-0.47f, 0.272f)
                verticalLineToRelative(-0.544f)
                arcToRelative(0.35f, 0.35f, 0.0f, false, false, -0.35f, -0.35f)
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
        return `__404-fill`!!
    }

private var `__404-fill`: ImageVector? = null
