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

public val WeatherIcon._305: ImageVector
    get() {
        if (__305 != null) {
            return __305!!
        }
        __305 = Builder(name = "_305", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.0f, 14.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(10.0f, 14.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 2.0f, 0.0f)
                curveToRelative(0.0f, -0.5f, -0.555f, -1.395f, -1.0f, -2.0f)
                curveToRelative(-0.445f, 0.605f, -1.0f, 1.5f, -1.0f, 2.0f)
                close()
                moveTo(7.9f, 10.0f)
                curveToRelative(1.453f, 0.0f, 2.761f, -0.62f, 3.675f, -1.61f)
                arcToRelative(0.335f, 0.335f, 0.0f, false, true, 0.365f, -0.083f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.566f, -5.767f)
                arcToRelative(0.335f, 0.335f, 0.0f, false, true, -0.341f, -0.152f)
                arcTo(4.997f, 4.997f, 0.0f, false, false, 7.9f, 0.0f)
                arcToRelative(4.997f, 4.997f, 0.0f, false, false, -4.25f, 2.365f)
                arcToRelative(0.334f, 0.334f, 0.0f, false, true, -0.32f, 0.153f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.596f, 5.836f)
                arcToRelative(0.334f, 0.334f, 0.0f, false, true, 0.345f, 0.086f)
                arcTo(4.99f, 4.99f, 0.0f, false, false, 7.9f, 10.0f)
                close()
                moveTo(11.805f, 7.104f)
                curveToRelative(-0.172f, -0.129f, -0.438f, -0.097f, -0.555f, 0.083f)
                arcTo(3.997f, 3.997f, 0.0f, false, true, 7.9f, 9.0f)
                arcToRelative(3.996f, 3.996f, 0.0f, false, true, -3.297f, -1.734f)
                curveToRelative(-0.112f, -0.163f, -0.347f, -0.197f, -0.513f, -0.089f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -0.362f, -3.54f)
                curveToRelative(0.184f, 0.072f, 0.408f, -0.01f, 0.485f, -0.192f)
                arcToRelative(4.001f, 4.001f, 0.0f, false, true, 7.398f, 0.059f)
                curveToRelative(0.08f, 0.2f, 0.335f, 0.282f, 0.53f, 0.19f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -0.335f, 3.41f)
                close()
            }
        }
        .build()
        return __305!!
    }

private var __305: ImageVector? = null
