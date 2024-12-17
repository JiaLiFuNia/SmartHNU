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

public val WeatherIcon._1045: ImageVector
    get() {
        if (__1045 != null) {
            return __1045!!
        }
        __1045 = Builder(name = "_1045", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.25f, 5.121f)
                curveToRelative(0.055f, -0.203f, 0.306f, -0.25f, 0.455f, -0.102f)
                lineToRelative(1.411f, 1.412f)
                arcToRelative(0.8f, 0.8f, 0.0f, false, false, 1.132f, 0.0f)
                lineToRelative(0.282f, -0.283f)
                arcToRelative(0.8f, 0.8f, 0.0f, false, false, 0.0f, -1.132f)
                lineTo(5.12f, 3.605f)
                curveToRelative(-0.149f, -0.149f, -0.101f, -0.4f, 0.102f, -0.454f)
                arcToRelative(2.799f, 2.799f, 0.0f, false, true, 3.531f, 2.79f)
                arcToRelative(0.284f, 0.284f, 0.0f, false, true, -0.085f, 0.191f)
                lineToRelative(-0.51f, 0.51f)
                lineToRelative(3.974f, 3.975f)
                arcToRelative(0.8f, 0.8f, 0.0f, false, true, 0.0f, 1.131f)
                lineToRelative(-0.283f, 0.283f)
                arcToRelative(0.8f, 0.8f, 0.0f, false, true, -1.132f, 0.0f)
                lineTo(6.744f, 8.057f)
                lineToRelative(-0.51f, 0.51f)
                arcToRelative(0.284f, 0.284f, 0.0f, false, true, -0.192f, 0.085f)
                arcToRelative(2.799f, 2.799f, 0.0f, false, true, -2.79f, -3.531f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(16.0f, 8.0f)
                arcTo(8.0f, 8.0f, 0.0f, true, true, 0.0f, 8.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, true, 16.0f, 0.0f)
                close()
                moveTo(14.7f, 8.0f)
                arcToRelative(6.67f, 6.67f, 0.0f, false, false, -1.352f, -4.037f)
                lineToRelative(-9.385f, 9.385f)
                arcTo(6.7f, 6.7f, 0.0f, false, false, 14.7f, 8.0f)
                close()
                moveTo(12.315f, 2.874f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, false, -9.44f, 9.44f)
                lineToRelative(9.44f, -9.44f)
                close()
            }
        }
        .build()
        return __1045!!
    }

private var __1045: ImageVector? = null
