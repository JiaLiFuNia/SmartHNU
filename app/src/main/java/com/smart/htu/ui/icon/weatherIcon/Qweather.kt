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

public val WeatherIcon.Qweather: ImageVector
    get() {
        if (_qweather != null) {
            return _qweather!!
        }
        _qweather = Builder(name = "Qweather", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(14.936f, 11.53f)
                curveToRelative(-0.907f, 0.799f, -1.434f, 1.36f, -2.616f, 1.634f)
                arcToRelative(7.158f, 7.158f, 0.0f, false, false, 0.715f, -9.529f)
                arcTo(7.27f, 7.27f, 0.0f, false, false, 8.66f, 0.935f)
                arcToRelative(7.313f, 7.313f, 0.0f, false, false, -5.08f, 0.858f)
                arcTo(7.212f, 7.212f, 0.0f, false, false, 0.354f, 5.778f)
                arcToRelative(7.144f, 7.144f, 0.0f, false, false, 0.253f, 5.104f)
                arcToRelative(7.229f, 7.229f, 0.0f, false, false, 3.604f, 3.652f)
                curveToRelative(1.612f, 0.74f, 3.44f, 0.868f, 5.14f, 0.361f)
                arcToRelative(7.864f, 7.864f, 0.0f, false, false, 4.344f, -0.864f)
                curveToRelative(1.126f, -0.589f, 1.588f, -1.46f, 2.305f, -2.5f)
                horizontalLineToRelative(-1.064f)
                close()
                moveTo(6.51f, 14.152f)
                arcToRelative(5.743f, 5.743f, 0.0f, false, true, -3.551f, -1.69f)
                arcToRelative(5.642f, 5.642f, 0.0f, false, true, -0.514f, -7.317f)
                arcToRelative(5.727f, 5.727f, 0.0f, false, true, 3.28f, -2.162f)
                arcToRelative(5.767f, 5.767f, 0.0f, false, true, 3.912f, 0.45f)
                arcToRelative(5.696f, 5.696f, 0.0f, false, true, 2.692f, 2.851f)
                arcToRelative(5.628f, 5.628f, 0.0f, false, true, 0.197f, 3.9f)
                arcToRelative(5.682f, 5.682f, 0.0f, false, true, -2.39f, 3.105f)
                arcToRelative(7.005f, 7.005f, 0.0f, false, true, -1.07f, -0.146f)
                curveToRelative(-1.302f, -0.294f, -2.437f, -1.113f, -3.237f, -2.056f)
                curveToRelative(-0.002f, 0.0f, -0.003f, -0.003f, -0.004f, -0.004f)
                arcToRelative(3.2f, 3.2f, 0.0f, false, true, -0.7f, -1.929f)
                arcToRelative(2.254f, 2.254f, 0.0f, false, true, 0.548f, -1.517f)
                arcToRelative(2.473f, 2.473f, 0.0f, false, true, 1.91f, -0.89f)
                curveToRelative(0.198f, 0.0f, 0.396f, 0.023f, 0.589f, 0.07f)
                arcToRelative(1.423f, 1.423f, 0.0f, false, true, 0.24f, 0.07f)
                curveToRelative(0.327f, 0.139f, 0.603f, 0.377f, 0.784f, 0.682f)
                arcToRelative(1.48f, 1.48f, 0.0f, false, true, -0.44f, 1.98f)
                arcToRelative(1.509f, 1.509f, 0.0f, false, true, -1.403f, 0.162f)
                arcToRelative(0.17f, 0.17f, 0.0f, false, false, -0.192f, 0.045f)
                arcToRelative(0.167f, 0.167f, 0.0f, false, false, -0.017f, 0.195f)
                arcToRelative(1.675f, 1.675f, 0.0f, false, false, 1.426f, 0.8f)
                lineToRelative(0.048f, -0.001f)
                arcToRelative(2.821f, 2.821f, 0.0f, false, false, 1.203f, -0.342f)
                arcTo(2.747f, 2.747f, 0.0f, false, false, 11.26f, 7.99f)
                arcToRelative(2.862f, 2.862f, 0.0f, false, false, -0.47f, -1.585f)
                arcToRelative(3.49f, 3.49f, 0.0f, false, false, -0.072f, -0.098f)
                curveToRelative(-0.02f, -0.028f, -0.042f, -0.055f, -0.064f, -0.083f)
                lineToRelative(-0.036f, -0.045f)
                curveToRelative(-0.79f, -1.03f, -2.033f, -1.634f, -3.45f, -1.593f)
                curveToRelative(-1.27f, 0.036f, -2.417f, 0.53f, -3.223f, 1.382f)
                arcToRelative(4.357f, 4.357f, 0.0f, false, false, -0.724f, 4.99f)
                arcToRelative(7.827f, 7.827f, 0.0f, false, false, 3.44f, 3.205f)
                curveToRelative(-0.051f, -0.004f, -0.101f, -0.006f, -0.151f, -0.011f)
                close()
            }
        }
        .build()
        return _qweather!!
    }

private var _qweather: ImageVector? = null
