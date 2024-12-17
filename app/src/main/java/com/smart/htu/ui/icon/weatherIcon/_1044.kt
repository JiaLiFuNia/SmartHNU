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

public val WeatherIcon._1044: ImageVector
    get() {
        if (__1044 != null) {
            return __1044!!
        }
        __1044 = Builder(name = "_1044", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.981f, 3.721f)
                arcToRelative(0.194f, 0.194f, 0.0f, false, false, -0.26f, 0.26f)
                lineToRelative(1.45f, 3.312f)
                lineToRelative(2.122f, -2.121f)
                lineToRelative(-3.312f, -1.45f)
                close()
                moveTo(5.525f, 7.647f)
                lineTo(7.646f, 5.525f)
                lineTo(12.243f, 10.121f)
                lineTo(10.121f, 12.243f)
                lineTo(5.525f, 7.647f)
                close()
                moveTo(10.475f, 12.596f)
                lineTo(12.596f, 10.475f)
                lineTo(12.95f, 10.829f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.0f, 0.707f)
                lineToRelative(-1.414f, 1.414f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.708f, 0.0f)
                lineToRelative(-0.353f, -0.354f)
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
        return __1044!!
    }

private var __1044: ImageVector? = null
