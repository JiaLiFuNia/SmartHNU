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

public val WeatherIcon._1605: ImageVector
    get() {
        if (__1605 != null) {
            return __1605!!
        }
        __1605 = Builder(name = "_1605", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.455f, 1.261f)
                arcToRelative(0.526f, 0.526f, 0.0f, false, false, -0.91f, 0.0f)
                lineTo(0.07f, 14.224f)
                curveToRelative(-0.2f, 0.346f, 0.052f, 0.776f, 0.454f, 0.776f)
                horizontalLineToRelative(14.952f)
                curveToRelative(0.402f, 0.0f, 0.654f, -0.43f, 0.454f, -0.776f)
                lineTo(8.455f, 1.26f)
                close()
                moveTo(9.153f, 8.592f)
                curveToRelative(-0.065f, -0.536f, 0.22f, -1.394f, 1.231f, -1.692f)
                curveToRelative(-0.048f, 0.867f, 0.155f, 1.351f, 0.341f, 1.793f)
                curveToRelative(0.143f, 0.338f, 0.274f, 0.652f, 0.275f, 1.094f)
                curveToRelative(0.0f, 1.713f, -1.308f, 2.963f, -2.965f, 2.963f)
                curveTo(6.378f, 12.75f, 5.0f, 11.501f, 5.0f, 9.787f)
                curveToRelative(0.0f, -0.71f, 0.308f, -1.583f, 0.923f, -1.91f)
                curveToRelative(0.0f, 0.0f, 0.11f, 1.178f, 0.692f, 1.67f)
                curveToRelative(0.012f, -0.13f, 0.007f, -0.31f, 0.0f, -0.524f)
                curveToRelative(-0.03f, -1.08f, -0.086f, -3.02f, 1.845f, -3.773f)
                curveToRelative(-0.08f, 1.453f, -0.03f, 2.465f, 0.693f, 3.342f)
                close()
            }
        }
        .build()
        return __1605!!
    }

private var __1605: ImageVector? = null
