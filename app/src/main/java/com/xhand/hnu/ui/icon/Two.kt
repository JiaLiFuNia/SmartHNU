package com.xhand.hnu.ui.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberLooksTwo(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "looks_two",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16.583f, 28.292f)
                horizontalLineTo(23.5f)
                quadToRelative(0.5f, 0f, 0.875f, -0.396f)
                reflectiveQuadToRelative(0.375f, -0.938f)
                quadToRelative(0f, -0.541f, -0.396f, -0.937f)
                reflectiveQuadToRelative(-0.896f, -0.396f)
                horizontalLineToRelative(-5.583f)
                verticalLineToRelative(-4.333f)
                horizontalLineToRelative(4.25f)
                quadToRelative(1.042f, 0f, 1.833f, -0.792f)
                quadToRelative(0.792f, -0.792f, 0.792f, -1.875f)
                verticalLineToRelative(-4.25f)
                quadToRelative(0f, -1.083f, -0.792f, -1.875f)
                quadToRelative(-0.791f, -0.792f, -1.833f, -0.792f)
                horizontalLineToRelative(-5.583f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.396f)
                reflectiveQuadToRelative(-0.375f, 0.938f)
                quadToRelative(0f, 0.541f, 0.375f, 0.937f)
                reflectiveQuadToRelative(0.958f, 0.396f)
                horizontalLineToRelative(5.542f)
                verticalLineToRelative(4.25f)
                horizontalLineToRelative(-4.25f)
                quadToRelative(-1.042f, 0f, -1.833f, 0.792f)
                quadToRelative(-0.792f, 0.791f, -0.792f, 1.875f)
                verticalLineToRelative(5.666f)
                quadToRelative(0f, 0.542f, 0.375f, 0.938f)
                quadToRelative(0.375f, 0.396f, 0.958f, 0.396f)
                close()
                moveTo(7.875f, 34.75f)
                quadToRelative(-1.042f, 0f, -1.833f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.833f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.042f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.833f, -0.792f)
                horizontalLineToRelative(24.25f)
                quadToRelative(1.042f, 0f, 1.833f, 0.792f)
                quadToRelative(0.792f, 0.791f, 0.792f, 1.833f)
                verticalLineToRelative(24.25f)
                quadToRelative(0f, 1.042f, -0.792f, 1.833f)
                quadToRelative(-0.791f, 0.792f, -1.833f, 0.792f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(24.25f)
                verticalLineTo(7.875f)
                horizontalLineTo(7.875f)
                verticalLineToRelative(24.25f)
                close()
                moveToRelative(0f, -24.25f)
                verticalLineToRelative(24.25f)
                verticalLineToRelative(-24.25f)
                close()
            }
        }.build()
    }
}