package com.xhand.htu

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

// Material Symbols Compose – Featuring 2993 icons to use in Jetpack Compose
// https://www.composables.com/icons
// Weight 600
// Outlined
// defaultWidth = 24.dp
// defaultHeight = 24.dp

@Composable
fun rememberNoAccounts(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "no_accounts",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(24.833f, 18.917f)
                lineToRelative(-8.083f, -8.084f)
                quadToRelative(0.625f, -0.458f, 1.479f, -0.729f)
                quadToRelative(0.854f, -0.271f, 1.771f, -0.271f)
                quadToRelative(2.458f, 0f, 4.146f, 1.688f)
                quadToRelative(1.687f, 1.687f, 1.687f, 4.146f)
                quadToRelative(0f, 0.958f, -0.271f, 1.812f)
                quadToRelative(-0.27f, 0.854f, -0.729f, 1.438f)
                close()
                moveTo(9.625f, 28.792f)
                quadToRelative(2.458f, -1.709f, 4.937f, -2.625f)
                quadToRelative(2.48f, -0.917f, 5.438f, -0.917f)
                quadToRelative(1.25f, 0f, 2.229f, 0.188f)
                quadToRelative(0.979f, 0.187f, 1.688f, 0.437f)
                lineTo(19.583f, 21.5f)
                quadToRelative(-2.291f, -0.208f, -3.708f, -1.583f)
                quadToRelative(-1.417f, -1.375f, -1.667f, -3.625f)
                lineToRelative(-4.833f, -4.834f)
                quadToRelative(-1.417f, 1.875f, -2.146f, 3.98f)
                quadTo(6.5f, 17.542f, 6.5f, 20f)
                quadToRelative(0f, 2.417f, 0.729f, 4.562f)
                quadToRelative(0.729f, 2.146f, 2.396f, 4.23f)
                close()
                moveToRelative(21f, -0.292f)
                quadToRelative(1.333f, -1.625f, 2.104f, -3.75f)
                quadToRelative(0.771f, -2.125f, 0.771f, -4.75f)
                quadToRelative(0f, -5.792f, -3.854f, -9.646f)
                quadTo(25.792f, 6.5f, 20f, 6.5f)
                quadToRelative(-2.625f, 0f, -4.708f, 0.771f)
                quadToRelative(-2.084f, 0.771f, -3.834f, 2.062f)
                close()
                moveTo(20f, 37.042f)
                quadToRelative(-3.542f, 0f, -6.646f, -1.334f)
                quadToRelative(-3.104f, -1.333f, -5.416f, -3.646f)
                quadToRelative(-2.313f, -2.312f, -3.646f, -5.416f)
                quadTo(2.958f, 23.542f, 2.958f, 20f)
                reflectiveQuadToRelative(1.334f, -6.646f)
                quadToRelative(1.333f, -3.104f, 3.625f, -5.416f)
                quadToRelative(2.291f, -2.313f, 5.395f, -3.667f)
                quadToRelative(3.105f, -1.354f, 6.646f, -1.354f)
                quadToRelative(3.584f, 0f, 6.688f, 1.354f)
                quadToRelative(3.104f, 1.354f, 5.416f, 3.667f)
                quadToRelative(2.313f, 2.312f, 3.667f, 5.416f)
                quadToRelative(1.354f, 3.104f, 1.354f, 6.646f)
                reflectiveQuadToRelative(-1.354f, 6.646f)
                quadToRelative(-1.354f, 3.104f, -3.667f, 5.416f)
                quadToRelative(-2.312f, 2.313f, -5.416f, 3.646f)
                quadToRelative(-3.104f, 1.334f, -6.646f, 1.334f)
                close()
                moveToRelative(0f, -3.542f)
                quadToRelative(2.208f, 0f, 4.188f, -0.625f)
                quadToRelative(1.979f, -0.625f, 3.937f, -2f)
                quadToRelative(-2f, -1.333f, -3.958f, -2f)
                quadToRelative(-1.959f, -0.667f, -4.167f, -0.667f)
                quadToRelative(-2.208f, 0f, -4.167f, 0.667f)
                quadToRelative(-1.958f, 0.667f, -3.916f, 2f)
                quadToRelative(1.958f, 1.375f, 3.916f, 2f)
                quadToRelative(1.959f, 0.625f, 4.167f, 0.625f)
                close()
                moveToRelative(0.042f, -2.625f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberGroups(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "groups",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(-0.25f, 30.25f)
                verticalLineToRelative(-2.583f)
                quadToRelative(0f, -1.834f, 1.854f, -2.938f)
                reflectiveQuadToRelative(4.813f, -1.104f)
                quadToRelative(0.375f, 0f, 0.729f, 0.021f)
                reflectiveQuadToRelative(0.687f, 0.062f)
                quadToRelative(-0.416f, 0.834f, -0.666f, 1.709f)
                quadToRelative(-0.25f, 0.875f, -0.25f, 1.875f)
                verticalLineToRelative(2.958f)
                close()
                moveToRelative(10f, 0f)
                verticalLineToRelative(-2.958f)
                quadToRelative(0f, -2.834f, 2.833f, -4.563f)
                quadTo(15.417f, 21f, 20f, 21f)
                quadToRelative(4.625f, 0f, 7.438f, 1.729f)
                quadToRelative(2.812f, 1.729f, 2.812f, 4.563f)
                verticalLineToRelative(2.958f)
                close()
                moveToRelative(23.333f, 0f)
                verticalLineToRelative(-2.958f)
                quadToRelative(0f, -0.959f, -0.229f, -1.854f)
                quadToRelative(-0.229f, -0.896f, -0.646f, -1.73f)
                quadToRelative(0.334f, -0.041f, 0.688f, -0.062f)
                quadToRelative(0.354f, -0.021f, 0.729f, -0.021f)
                quadToRelative(3f, 0f, 4.813f, 1.104f)
                quadToRelative(1.812f, 1.104f, 1.812f, 2.938f)
                verticalLineToRelative(2.583f)
                close()
                moveTo(20f, 24.125f)
                quadToRelative(-2.583f, 0f, -4.458f, 0.771f)
                reflectiveQuadToRelative(-2.209f, 2.021f)
                verticalLineToRelative(0.166f)
                horizontalLineToRelative(13.375f)
                verticalLineToRelative(-0.166f)
                quadToRelative(-0.333f, -1.25f, -2.208f, -2.021f)
                quadToRelative(-1.875f, -0.771f, -4.5f, -0.771f)
                close()
                moveTo(6.375f, 22.208f)
                quadToRelative(-1.292f, 0f, -2.229f, -0.937f)
                quadToRelative(-0.938f, -0.938f, -0.938f, -2.271f)
                reflectiveQuadToRelative(0.938f, -2.25f)
                quadToRelative(0.937f, -0.917f, 2.229f, -0.917f)
                quadToRelative(1.333f, 0f, 2.271f, 0.917f)
                quadToRelative(0.937f, 0.917f, 0.937f, 2.25f)
                reflectiveQuadToRelative(-0.916f, 2.271f)
                quadToRelative(-0.917f, 0.937f, -2.292f, 0.937f)
                close()
                moveToRelative(27.25f, 0f)
                quadToRelative(-1.333f, 0f, -2.25f, -0.937f)
                quadToRelative(-0.917f, -0.938f, -0.917f, -2.271f)
                reflectiveQuadToRelative(0.917f, -2.25f)
                quadToRelative(0.917f, -0.917f, 2.25f, -0.917f)
                reflectiveQuadToRelative(2.25f, 0.917f)
                quadToRelative(0.917f, 0.917f, 0.917f, 2.25f)
                reflectiveQuadToRelative(-0.917f, 2.271f)
                quadToRelative(-0.917f, 0.937f, -2.25f, 0.937f)
                close()
                moveTo(20f, 19.625f)
                quadToRelative(-2.167f, 0f, -3.708f, -1.521f)
                quadToRelative(-1.542f, -1.521f, -1.542f, -3.729f)
                quadToRelative(0f, -2.167f, 1.542f, -3.708f)
                quadTo(17.833f, 9.125f, 20f, 9.125f)
                quadToRelative(2.208f, 0f, 3.729f, 1.542f)
                quadToRelative(1.521f, 1.541f, 1.521f, 3.708f)
                quadToRelative(0f, 2.208f, -1.521f, 3.729f)
                reflectiveQuadTo(20f, 19.625f)
                close()
                moveToRelative(0f, -7.333f)
                quadToRelative(-0.875f, 0f, -1.479f, 0.604f)
                quadToRelative(-0.604f, 0.604f, -0.604f, 1.479f)
                quadToRelative(0f, 0.875f, 0.604f, 1.479f)
                quadToRelative(0.604f, 0.604f, 1.521f, 0.604f)
                quadToRelative(0.833f, 0f, 1.458f, -0.604f)
                reflectiveQuadToRelative(0.625f, -1.479f)
                quadToRelative(0f, -0.875f, -0.625f, -1.479f)
                quadToRelative(-0.625f, -0.604f, -1.5f, -0.604f)
                close()
                moveToRelative(0.042f, 14.791f)
                close()
                moveTo(20f, 14.375f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberGlyphs(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "glyphs",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(9.25f, 3.375f)
                horizontalLineToRelative(3.042f)
                lineToRelative(5.5f, 14.667f)
                horizontalLineToRelative(-3.209f)
                lineToRelative(-0.958f, -2.625f)
                horizontalLineToRelative(-5.75f)
                lineToRelative(-0.917f, 2.625f)
                horizontalLineTo(3.75f)
                close()
                moveToRelative(0.083f, 16.792f)
                quadToRelative(1.834f, 0f, 3.125f, 1.271f)
                quadToRelative(1.292f, 1.27f, 1.292f, 3.104f)
                quadToRelative(0f, 0.958f, -0.479f, 1.833f)
                reflectiveQuadToRelative(-1.229f, 1.542f)
                lineToRelative(-0.667f, 0.541f)
                lineTo(13f, 30.042f)
                lineToRelative(2.792f, -2.792f)
                lineToRelative(2.166f, 2.167f)
                lineToRelative(-2.791f, 2.75f)
                lineToRelative(2.208f, 2.208f)
                lineToRelative(-2.167f, 2.125f)
                lineToRelative(-2.166f, -2.167f)
                lineToRelative(-1.459f, 1.417f)
                quadToRelative(-0.666f, 0.708f, -1.521f, 1.104f)
                quadToRelative(-0.854f, 0.396f, -1.854f, 0.396f)
                quadToRelative(-1.791f, 0f, -3.083f, -1.188f)
                quadToRelative(-1.292f, -1.187f, -1.292f, -2.979f)
                quadToRelative(0f, -0.958f, 0.375f, -1.812f)
                quadToRelative(0.375f, -0.854f, 1.084f, -1.438f)
                lineToRelative(1.791f, -1.5f)
                lineToRelative(-0.791f, -0.75f)
                quadToRelative(-0.625f, -0.625f, -0.98f, -1.395f)
                quadToRelative(-0.354f, -0.771f, -0.354f, -1.646f)
                quadToRelative(0f, -1.834f, 1.271f, -3.104f)
                quadToRelative(1.271f, -1.271f, 3.104f, -1.271f)
                close()
                moveToRelative(-0.208f, 10.208f)
                lineToRelative(-1.667f, 1.417f)
                quadToRelative(-0.25f, 0.25f, -0.396f, 0.562f)
                quadToRelative(-0.145f, 0.313f, -0.145f, 0.688f)
                quadToRelative(0f, 0.5f, 0.375f, 0.833f)
                quadToRelative(0.375f, 0.333f, 0.916f, 0.333f)
                quadToRelative(0.334f, 0f, 0.646f, -0.187f)
                quadToRelative(0.313f, -0.188f, 0.563f, -0.396f)
                lineToRelative(1.458f, -1.458f)
                close()
                moveToRelative(0.208f, -7.208f)
                quadToRelative(-0.583f, 0f, -0.979f, 0.395f)
                quadToRelative(-0.396f, 0.396f, -0.396f, 0.938f)
                quadToRelative(0f, 0.333f, 0.125f, 0.583f)
                quadToRelative(0.125f, 0.25f, 0.292f, 0.459f)
                lineToRelative(0.917f, 0.875f)
                lineToRelative(0.541f, -0.459f)
                quadToRelative(0.375f, -0.333f, 0.625f, -0.666f)
                quadToRelative(0.25f, -0.334f, 0.25f, -0.75f)
                quadToRelative(0f, -0.542f, -0.416f, -0.959f)
                quadToRelative(-0.417f, -0.416f, -0.959f, -0.416f)
                close()
                moveToRelative(1.334f, -15.542f)
                lineToRelative(-1.75f, 4.958f)
                horizontalLineToRelative(3.708f)
                lineToRelative(-1.792f, -4.958f)
                close()
                moveTo(29.5f, 1.833f)
                quadToRelative(0.333f, 0.584f, 0.542f, 1.188f)
                quadToRelative(0.208f, 0.604f, 0.375f, 1.229f)
                lineToRelative(-1.959f, 0.542f)
                horizontalLineTo(37f)
                verticalLineToRelative(3.041f)
                horizontalLineToRelative(-3.167f)
                quadToRelative(-0.5f, 1.417f, -1.291f, 2.667f)
                quadToRelative(-0.792f, 1.25f, -1.792f, 2.375f)
                quadToRelative(1.208f, 0.833f, 2.542f, 1.396f)
                quadToRelative(1.333f, 0.562f, 2.75f, 0.896f)
                lineToRelative(-0.709f, 2.875f)
                quadToRelative(-1.875f, -0.417f, -3.604f, -1.188f)
                quadTo(30f, 16.083f, 28.5f, 14.917f)
                quadToRelative(-1.5f, 1.166f, -3.188f, 1.937f)
                quadToRelative(-1.687f, 0.771f, -3.52f, 1.188f)
                lineToRelative(-0.667f, -2.875f)
                quadToRelative(1.375f, -0.334f, 2.708f, -0.917f)
                quadToRelative(1.334f, -0.583f, 2.5f, -1.375f)
                quadToRelative(-0.958f, -1.083f, -1.729f, -2.354f)
                quadToRelative(-0.771f, -1.271f, -1.271f, -2.688f)
                horizontalLineToRelative(-3.25f)
                verticalLineTo(4.792f)
                horizontalLineTo(27.5f)
                quadToRelative(-0.125f, -0.542f, -0.292f, -1.084f)
                quadToRelative(-0.166f, -0.541f, -0.416f, -1.125f)
                close()
                moveToRelative(4.375f, 19.125f)
                lineToRelative(2.167f, 2.125f)
                lineTo(22.875f, 36.25f)
                lineToRelative(-2.167f, -2.125f)
                close()
                moveToRelative(-9.5f, 1.125f)
                quadToRelative(0.958f, 0f, 1.646f, 0.688f)
                quadToRelative(0.687f, 0.687f, 0.687f, 1.687f)
                quadToRelative(0f, 0.959f, -0.687f, 1.646f)
                quadToRelative(-0.688f, 0.688f, -1.646f, 0.688f)
                quadToRelative(-1f, 0f, -1.667f, -0.688f)
                quadToRelative(-0.666f, -0.687f, -0.666f, -1.687f)
                quadToRelative(0f, -0.959f, 0.666f, -1.646f)
                quadToRelative(0.667f, -0.688f, 1.667f, -0.688f)
                close()
                moveToRelative(2.208f, -14.25f)
                quadToRelative(0.334f, 0.917f, 0.813f, 1.688f)
                quadToRelative(0.479f, 0.771f, 1.187f, 1.396f)
                quadToRelative(0.667f, -0.625f, 1.146f, -1.417f)
                quadToRelative(0.479f, -0.792f, 0.813f, -1.667f)
                close()
                moveToRelative(6.125f, 22.584f)
                quadToRelative(0.959f, 0f, 1.646f, 0.687f)
                quadToRelative(0.688f, 0.688f, 0.688f, 1.688f)
                quadToRelative(0f, 0.958f, -0.688f, 1.646f)
                quadToRelative(-0.687f, 0.687f, -1.646f, 0.687f)
                quadToRelative(-1f, 0f, -1.666f, -0.687f)
                quadToRelative(-0.667f, -0.688f, -0.667f, -1.688f)
                quadToRelative(0f, -0.958f, 0.667f, -1.646f)
                quadToRelative(0.666f, -0.687f, 1.666f, -0.687f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberBook(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "book",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(9.792f, 37.042f)
                quadToRelative(-1.459f, 0f, -2.5f, -1.042f)
                quadToRelative(-1.042f, -1.042f, -1.042f, -2.5f)
                verticalLineToRelative(-27f)
                quadToRelative(0f, -1.5f, 1.042f, -2.542f)
                quadToRelative(1.041f, -1.041f, 2.5f, -1.041f)
                horizontalLineToRelative(20.416f)
                quadToRelative(1.459f, 0f, 2.5f, 1.041f)
                quadTo(33.75f, 5f, 33.75f, 6.5f)
                verticalLineToRelative(27f)
                quadToRelative(0f, 1.458f, -1.042f, 2.5f)
                quadToRelative(-1.041f, 1.042f, -2.5f, 1.042f)
                close()
                moveToRelative(0f, -3.542f)
                horizontalLineToRelative(20.416f)
                verticalLineToRelative(-27f)
                horizontalLineToRelative(-2.333f)
                verticalLineToRelative(11f)
                lineToRelative(-4f, -2.333f)
                lineToRelative(-4f, 2.333f)
                verticalLineToRelative(-11f)
                horizontalLineTo(9.792f)
                verticalLineToRelative(27f)
                close()
                moveToRelative(0f, 0f)
                verticalLineToRelative(-27f)
                verticalLineToRelative(27f)
                close()
                moveToRelative(10.083f, -16f)
                lineToRelative(4f, -2.333f)
                lineToRelative(4f, 2.333f)
                lineToRelative(-4f, -2.333f)
                lineToRelative(-4f, 2.333f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberVisibilityOff(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "visibility_off",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(26.417f, 22.083f)
                lineToRelative(-2.834f, -2.791f)
                quadToRelative(0.75f, -2f, -0.75f, -3.271f)
                reflectiveQuadToRelative(-3.125f, -0.604f)
                lineToRelative(-2.625f, -2.667f)
                quadToRelative(0.667f, -0.292f, 1.438f, -0.458f)
                quadToRelative(0.771f, -0.167f, 1.479f, -0.167f)
                quadToRelative(2.958f, 0f, 5f, 2.042f)
                quadToRelative(2.042f, 2.041f, 2.042f, 5f)
                quadToRelative(0f, 0.708f, -0.167f, 1.541f)
                quadToRelative(-0.167f, 0.834f, -0.458f, 1.375f)
                close()
                moveToRelative(5.708f, 5.792f)
                lineToRelative(-2.167f, -2.208f)
                quadToRelative(1.709f, -1.375f, 3.063f, -3.021f)
                reflectiveQuadToRelative(2.187f, -3.479f)
                quadToRelative(-2.125f, -4.417f, -6.125f, -7.021f)
                reflectiveQuadToRelative(-8.791f, -2.604f)
                quadToRelative(-1.542f, 0f, -3.104f, 0.25f)
                quadToRelative(-1.563f, 0.25f, -2.438f, 0.625f)
                lineToRelative(-2.583f, -2.584f)
                quadToRelative(1.5f, -0.666f, 3.708f, -1.104f)
                quadToRelative(2.208f, -0.437f, 4.25f, -0.437f)
                quadToRelative(6.083f, 0f, 11.104f, 3.458f)
                quadToRelative(5.021f, 3.458f, 7.396f, 9.417f)
                quadToRelative(-1f, 2.583f, -2.687f, 4.812f)
                quadToRelative(-1.688f, 2.229f, -3.813f, 3.896f)
                close()
                moveToRelative(1.167f, 9.458f)
                lineToRelative(-6.5f, -6.458f)
                quadToRelative(-1.417f, 0.542f, -3.167f, 0.854f)
                quadToRelative(-1.75f, 0.313f, -3.625f, 0.313f)
                quadToRelative(-6.167f, 0f, -11.208f, -3.459f)
                quadToRelative(-5.042f, -3.458f, -7.417f, -9.416f)
                quadToRelative(0.792f, -2.125f, 2.25f, -4.188f)
                quadTo(5.083f, 12.917f, 7f, 11.125f)
                lineTo(2.125f, 6.208f)
                lineToRelative(2.042f, -2.083f)
                lineToRelative(31.125f, 31.083f)
                close()
                moveToRelative(-24f, -24f)
                quadTo(7.875f, 14.5f, 6.688f, 16.062f)
                quadTo(5.5f, 17.625f, 4.792f, 19.167f)
                quadToRelative(2.125f, 4.458f, 6.187f, 7.041f)
                quadToRelative(4.063f, 2.584f, 9.229f, 2.584f)
                quadToRelative(1.084f, 0f, 2.188f, -0.125f)
                reflectiveQuadToRelative(1.812f, -0.375f)
                lineToRelative(-2.333f, -2.375f)
                quadToRelative(-0.375f, 0.166f, -0.896f, 0.229f)
                quadToRelative(-0.521f, 0.062f, -0.979f, 0.062f)
                quadToRelative(-2.917f, 0f, -4.979f, -2.041f)
                quadToRelative(-2.063f, -2.042f, -2.063f, -5f)
                quadToRelative(0f, -0.459f, 0.063f, -0.959f)
                quadToRelative(0.062f, -0.5f, 0.187f, -0.875f)
                close()
                moveToRelative(13.083f, 4.959f)
                close()
                moveToRelative(-5.625f, 2.833f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberVisibility(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "visibility",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(20f, 26.208f)
                quadToRelative(2.958f, 0f, 5f, -2.041f)
                quadToRelative(2.042f, -2.042f, 2.042f, -5f)
                quadToRelative(0f, -2.959f, -2.042f, -5f)
                quadToRelative(-2.042f, -2.042f, -5f, -2.042f)
                reflectiveQuadToRelative(-5f, 2.042f)
                quadToRelative(-2.042f, 2.041f, -2.042f, 5f)
                quadToRelative(0f, 2.958f, 2.042f, 5f)
                quadToRelative(2.042f, 2.041f, 5f, 2.041f)
                close()
                moveToRelative(0f, -2.916f)
                quadToRelative(-1.708f, 0f, -2.917f, -1.209f)
                quadToRelative(-1.208f, -1.208f, -1.208f, -2.916f)
                quadToRelative(0f, -1.709f, 1.208f, -2.917f)
                quadToRelative(1.209f, -1.208f, 2.917f, -1.208f)
                quadToRelative(1.708f, 0f, 2.917f, 1.208f)
                quadToRelative(1.208f, 1.208f, 1.208f, 2.917f)
                quadToRelative(0f, 1.708f, -1.208f, 2.916f)
                quadToRelative(-1.209f, 1.209f, -2.917f, 1.209f)
                close()
                moveToRelative(0f, 8.75f)
                quadToRelative(-6.208f, 0f, -11.208f, -3.563f)
                quadToRelative(-5f, -3.562f, -7.417f, -9.312f)
                quadToRelative(2.417f, -5.75f, 7.417f, -9.313f)
                quadToRelative(5f, -3.562f, 11.208f, -3.562f)
                reflectiveQuadToRelative(11.208f, 3.562f)
                quadToRelative(5f, 3.563f, 7.417f, 9.313f)
                quadToRelative(-2.417f, 5.75f, -7.417f, 9.312f)
                quadToRelative(-5f, 3.563f, -11.208f, 3.563f)
                close()
                moveToRelative(0f, -12.875f)
                close()
                moveToRelative(0f, 9.625f)
                quadToRelative(4.875f, 0f, 8.958f, -2.625f)
                quadToRelative(4.084f, -2.625f, 6.209f, -7f)
                quadToRelative(-2.125f, -4.375f, -6.209f, -7f)
                quadTo(24.875f, 9.542f, 20f, 9.542f)
                reflectiveQuadToRelative(-8.958f, 2.625f)
                quadToRelative(-4.084f, 2.625f, -6.25f, 7f)
                quadToRelative(2.166f, 4.375f, 6.25f, 7f)
                quadToRelative(4.083f, 2.625f, 8.958f, 2.625f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSort(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "sort",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(4.792f, 30.042f)
                verticalLineTo(26.5f)
                horizontalLineToRelative(10.666f)
                verticalLineToRelative(3.542f)
                close()
                moveToRelative(0f, -8.542f)
                verticalLineToRelative(-3.542f)
                horizontalLineToRelative(20.583f)
                verticalLineTo(21.5f)
                close()
                moveToRelative(0f, -8.542f)
                verticalLineTo(9.375f)
                horizontalLineTo(35.25f)
                verticalLineToRelative(3.583f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberTimeline(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "timeline",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(5.167f, 30.125f)
                quadToRelative(-1.459f, 0f, -2.5f, -1.042f)
                quadToRelative(-1.042f, -1.041f, -1.042f, -2.5f)
                quadToRelative(0f, -1.5f, 1.042f, -2.562f)
                quadToRelative(1.041f, -1.063f, 2.5f, -1.063f)
                quadToRelative(0.291f, 0f, 0.521f, 0.063f)
                quadToRelative(0.229f, 0.062f, 0.645f, 0.146f)
                lineToRelative(6.959f, -6.959f)
                quadToRelative(-0.125f, -0.375f, -0.146f, -0.645f)
                quadToRelative(-0.021f, -0.271f, -0.021f, -0.521f)
                quadToRelative(0f, -1.5f, 1.063f, -2.542f)
                quadToRelative(1.062f, -1.042f, 2.52f, -1.042f)
                quadToRelative(1.5f, 0f, 2.542f, 1.042f)
                quadToRelative(1.042f, 1.042f, 1.042f, 2.542f)
                quadToRelative(0f, 0.291f, -0.167f, 1.041f)
                lineToRelative(3.75f, 3.792f)
                quadToRelative(0.375f, -0.083f, 0.604f, -0.125f)
                quadToRelative(0.229f, -0.042f, 0.479f, -0.042f)
                quadToRelative(0.25f, 0f, 0.5f, 0.042f)
                reflectiveQuadToRelative(0.625f, 0.125f)
                lineToRelative(5.334f, -5.333f)
                quadToRelative(-0.084f, -0.375f, -0.125f, -0.625f)
                quadToRelative(-0.042f, -0.25f, -0.042f, -0.5f)
                quadToRelative(0f, -1.5f, 1.062f, -2.542f)
                quadToRelative(1.063f, -1.042f, 2.521f, -1.042f)
                quadToRelative(1.5f, 0f, 2.521f, 1.042f)
                reflectiveQuadToRelative(1.021f, 2.542f)
                quadToRelative(0f, 1.458f, -1.042f, 2.5f)
                quadToRelative(-1.041f, 1.041f, -2.5f, 1.041f)
                quadToRelative(-0.291f, 0f, -0.541f, -0.041f)
                quadToRelative(-0.25f, -0.042f, -0.584f, -0.125f)
                lineToRelative(-5.333f, 5.375f)
                quadToRelative(0.083f, 0.333f, 0.125f, 0.604f)
                quadToRelative(0.042f, 0.271f, 0.042f, 0.521f)
                quadToRelative(0f, 1.458f, -1.063f, 2.52f)
                quadToRelative(-1.062f, 1.063f, -2.521f, 1.063f)
                quadToRelative(-1.458f, 0f, -2.52f, -1.063f)
                quadToRelative(-1.063f, -1.062f, -1.063f, -2.52f)
                quadToRelative(0f, -0.292f, 0.021f, -0.542f)
                quadToRelative(0.021f, -0.25f, 0.146f, -0.625f)
                lineToRelative(-3.709f, -3.667f)
                quadToRelative(-0.375f, 0.084f, -0.625f, 0.125f)
                quadToRelative(-0.25f, 0.042f, -0.5f, 0.042f)
                reflectiveQuadToRelative(-1.083f, -0.167f)
                lineToRelative(-7f, 7.042f)
                quadToRelative(0.083f, 0.333f, 0.104f, 0.562f)
                quadToRelative(0.021f, 0.23f, 0.021f, 0.521f)
                quadToRelative(0f, 1.459f, -1.062f, 2.5f)
                quadToRelative(-1.063f, 1.042f, -2.521f, 1.042f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberAbc(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "abc",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(28.292f, 25.083f)
                quadToRelative(-0.709f, 0f, -1.146f, -0.458f)
                quadToRelative(-0.438f, -0.458f, -0.438f, -1.167f)
                verticalLineToRelative(-6.916f)
                quadToRelative(0f, -0.709f, 0.438f, -1.167f)
                quadToRelative(0.437f, -0.458f, 1.146f, -0.458f)
                horizontalLineToRelative(5.5f)
                quadToRelative(0.708f, 0f, 1.146f, 0.458f)
                quadToRelative(0.437f, 0.458f, 0.437f, 1.125f)
                verticalLineToRelative(2f)
                horizontalLineTo(33f)
                verticalLineToRelative(-1.167f)
                horizontalLineToRelative(-3.917f)
                verticalLineToRelative(5.334f)
                horizontalLineTo(33f)
                verticalLineTo(21.5f)
                horizontalLineToRelative(2.375f)
                verticalLineToRelative(1.958f)
                quadToRelative(0f, 0.709f, -0.437f, 1.167f)
                quadToRelative(-0.438f, 0.458f, -1.146f, 0.458f)
                close()
                moveToRelative(-12.542f, 0f)
                verticalLineTo(14.917f)
                horizontalLineToRelative(7.125f)
                quadToRelative(0.708f, 0f, 1.146f, 0.458f)
                quadToRelative(0.437f, 0.458f, 0.437f, 1.167f)
                verticalLineToRelative(1.791f)
                quadToRelative(0f, 0.75f, -0.437f, 1.209f)
                quadToRelative(-0.438f, 0.458f, -1.146f, 0.458f)
                quadToRelative(0.708f, 0f, 1.146f, 0.458f)
                quadToRelative(0.437f, 0.459f, 0.437f, 1.209f)
                verticalLineToRelative(1.791f)
                quadToRelative(0f, 0.709f, -0.437f, 1.167f)
                quadToRelative(-0.438f, 0.458f, -1.146f, 0.458f)
                close()
                moveToRelative(2.417f, -6.125f)
                horizontalLineToRelative(3.875f)
                verticalLineToRelative(-1.625f)
                horizontalLineToRelative(-3.875f)
                close()
                moveToRelative(0f, 3.709f)
                horizontalLineToRelative(3.875f)
                verticalLineToRelative(-1.625f)
                horizontalLineToRelative(-3.875f)
                close()
                moveTo(4.625f, 25.083f)
                verticalLineToRelative(-8.541f)
                quadToRelative(0f, -0.709f, 0.479f, -1.167f)
                quadToRelative(0.479f, -0.458f, 1.146f, -0.458f)
                horizontalLineToRelative(5.5f)
                quadToRelative(0.667f, 0f, 1.125f, 0.458f)
                quadToRelative(0.458f, 0.458f, 0.458f, 1.167f)
                verticalLineToRelative(8.541f)
                horizontalLineToRelative(-2.375f)
                verticalLineToRelative(-2.958f)
                horizontalLineTo(7f)
                verticalLineToRelative(2.958f)
                close()
                moveTo(7f, 19.75f)
                horizontalLineToRelative(3.958f)
                verticalLineToRelative(-2.417f)
                horizontalLineTo(7f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSearch(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "search",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(32.833f, 35.458f)
                lineTo(22f, 24.625f)
                quadToRelative(-1.208f, 1f, -2.854f, 1.542f)
                quadToRelative(-1.646f, 0.541f, -3.479f, 0.541f)
                quadToRelative(-4.625f, 0f, -7.834f, -3.208f)
                quadToRelative(-3.208f, -3.208f, -3.208f, -7.792f)
                quadToRelative(0f, -4.541f, 3.208f, -7.75f)
                quadToRelative(3.209f, -3.208f, 7.792f, -3.208f)
                reflectiveQuadToRelative(7.771f, 3.208f)
                quadToRelative(3.187f, 3.209f, 3.187f, 7.792f)
                quadToRelative(0f, 1.792f, -0.541f, 3.396f)
                quadToRelative(-0.542f, 1.604f, -1.542f, 2.979f)
                lineToRelative(10.875f, 10.792f)
                close()
                moveTo(15.667f, 23.167f)
                quadToRelative(3.083f, 0f, 5.229f, -2.167f)
                reflectiveQuadToRelative(2.146f, -5.292f)
                quadToRelative(0f, -3.083f, -2.146f, -5.25f)
                quadToRelative(-2.146f, -2.166f, -5.271f, -2.166f)
                reflectiveQuadToRelative(-5.292f, 2.166f)
                quadToRelative(-2.166f, 2.167f, -2.166f, 5.25f)
                quadToRelative(0f, 3.125f, 2.166f, 5.292f)
                quadToRelative(2.167f, 2.167f, 5.334f, 2.167f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberDeleteForever(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "delete_forever",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(15.375f, 27.667f)
                lineToRelative(4.667f, -4.709f)
                lineToRelative(4.666f, 4.709f)
                lineToRelative(2.334f, -2.375f)
                lineToRelative(-4.667f, -4.667f)
                lineToRelative(4.667f, -4.708f)
                lineToRelative(-2.334f, -2.375f)
                lineToRelative(-4.666f, 4.708f)
                lineToRelative(-4.667f, -4.708f)
                lineToRelative(-2.333f, 2.375f)
                lineToRelative(4.625f, 4.708f)
                lineToRelative(-4.625f, 4.667f)
                close()
                moveTo(11f, 35.375f)
                quadToRelative(-1.458f, 0f, -2.5f, -1.042f)
                quadToRelative(-1.042f, -1.041f, -1.042f, -2.5f)
                verticalLineTo(9.458f)
                horizontalLineTo(5.333f)
                verticalLineTo(5.917f)
                horizontalLineToRelative(8.959f)
                verticalLineTo(4.125f)
                horizontalLineToRelative(11.416f)
                verticalLineToRelative(1.792f)
                horizontalLineToRelative(8.959f)
                verticalLineToRelative(3.541f)
                horizontalLineToRelative(-2.084f)
                verticalLineToRelative(22.375f)
                quadToRelative(0f, 1.459f, -1.041f, 2.5f)
                quadTo(30.5f, 35.375f, 29f, 35.375f)
                close()
                moveTo(29f, 9.458f)
                horizontalLineTo(11f)
                verticalLineToRelative(22.375f)
                horizontalLineToRelative(18f)
                close()
                moveToRelative(-18f, 0f)
                verticalLineToRelative(22.375f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberInfo(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "info",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(18.5f, 28.5f)
                horizontalLineToRelative(3.208f)
                verticalLineTo(18.375f)
                horizontalLineTo(18.5f)
                close()
                moveTo(20f, 15.667f)
                quadToRelative(0.75f, 0f, 1.271f, -0.5f)
                quadToRelative(0.521f, -0.5f, 0.521f, -1.292f)
                reflectiveQuadToRelative(-0.5f, -1.312f)
                quadToRelative(-0.5f, -0.521f, -1.292f, -0.521f)
                reflectiveQuadToRelative(-1.292f, 0.521f)
                quadToRelative(-0.5f, 0.52f, -0.5f, 1.312f)
                quadToRelative(0f, 0.75f, 0.521f, 1.271f)
                quadToRelative(0.521f, 0.521f, 1.271f, 0.521f)
                close()
                moveToRelative(0f, 21.375f)
                quadToRelative(-3.542f, 0f, -6.646f, -1.334f)
                quadToRelative(-3.104f, -1.333f, -5.416f, -3.646f)
                quadToRelative(-2.313f, -2.312f, -3.646f, -5.416f)
                quadTo(2.958f, 23.542f, 2.958f, 20f)
                quadToRelative(0f, -3.583f, 1.334f, -6.687f)
                quadToRelative(1.333f, -3.105f, 3.646f, -5.396f)
                quadToRelative(2.312f, -2.292f, 5.416f, -3.646f)
                quadTo(16.458f, 2.917f, 20f, 2.917f)
                quadToRelative(3.583f, 0f, 6.688f, 1.354f)
                quadToRelative(3.104f, 1.354f, 5.395f, 3.646f)
                quadToRelative(2.292f, 2.291f, 3.646f, 5.416f)
                quadToRelative(1.354f, 3.125f, 1.354f, 6.667f)
                reflectiveQuadToRelative(-1.354f, 6.646f)
                quadToRelative(-1.354f, 3.104f, -3.667f, 5.416f)
                quadToRelative(-2.312f, 2.313f, -5.416f, 3.646f)
                quadToRelative(-3.104f, 1.334f, -6.646f, 1.334f)
                close()
                moveToRelative(0f, -3.542f)
                quadToRelative(5.625f, 0f, 9.562f, -3.938f)
                quadTo(33.5f, 25.625f, 33.5f, 20f)
                reflectiveQuadToRelative(-3.917f, -9.562f)
                quadTo(25.667f, 6.5f, 20f, 6.5f)
                quadToRelative(-5.625f, 0f, -9.562f, 3.917f)
                quadTo(6.5f, 14.333f, 6.5f, 20f)
                quadToRelative(0f, 5.625f, 3.938f, 9.562f)
                quadTo(14.375f, 33.5f, 20f, 33.5f)
                close()
                moveTo(20f, 20f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberChat(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "chat",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(10.167f, 23.208f)
                horizontalLineToRelative(12.875f)
                verticalLineToRelative(-2.625f)
                horizontalLineTo(10.167f)
                close()
                moveToRelative(0f, -5.208f)
                horizontalLineToRelative(19.666f)
                verticalLineToRelative(-2.625f)
                horizontalLineTo(10.167f)
                close()
                moveToRelative(0f, -5.208f)
                horizontalLineToRelative(19.666f)
                verticalLineToRelative(-2.667f)
                horizontalLineTo(10.167f)
                close()
                moveTo(2.958f, 36.917f)
                verticalLineTo(6.458f)
                quadTo(2.958f, 5f, 4f, 3.958f)
                quadToRelative(1.042f, -1.041f, 2.5f, -1.041f)
                horizontalLineToRelative(27f)
                quadToRelative(1.458f, 0f, 2.521f, 1.041f)
                quadToRelative(1.062f, 1.042f, 1.062f, 2.5f)
                verticalLineToRelative(20.417f)
                quadToRelative(0f, 1.417f, -1.062f, 2.479f)
                quadToRelative(-1.063f, 1.063f, -2.521f, 1.063f)
                horizontalLineTo(9.458f)
                close()
                moveToRelative(3.542f, -8f)
                lineToRelative(2.042f, -2.042f)
                horizontalLineTo(33.5f)
                verticalLineTo(6.458f)
                horizontalLineToRelative(-27f)
                close()
                moveToRelative(0f, -22.459f)
                verticalLineTo(28.917f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberCode(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "code",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(13.125f, 30.375f)
                lineToRelative(-10.5f, -10.458f)
                lineToRelative(10.542f, -10.5f)
                lineToRelative(2.541f, 2.5f)
                lineToRelative(-8f, 8f)
                lineToRelative(7.917f, 7.958f)
                close()
                moveToRelative(13.708f, 0.125f)
                lineToRelative(-2.541f, -2.542f)
                lineToRelative(8f, -8f)
                lineToRelative(-7.917f, -7.916f)
                lineToRelative(2.5f, -2.542f)
                lineToRelative(10.5f, 10.458f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberAccountCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "account_circle",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(9.667f, 28.792f)
                quadToRelative(2.458f, -1.709f, 4.958f, -2.625f)
                quadToRelative(2.5f, -0.917f, 5.375f, -0.917f)
                quadToRelative(2.917f, 0f, 5.417f, 0.938f)
                quadToRelative(2.5f, 0.937f, 4.958f, 2.604f)
                quadToRelative(1.667f, -2.125f, 2.396f, -4.25f)
                quadToRelative(0.729f, -2.125f, 0.729f, -4.542f)
                quadToRelative(0f, -5.708f, -3.896f, -9.604f)
                quadTo(25.708f, 6.5f, 20f, 6.5f)
                quadToRelative(-5.708f, 0f, -9.604f, 3.896f)
                quadTo(6.5f, 14.292f, 6.5f, 20f)
                quadToRelative(0f, 2.417f, 0.729f, 4.521f)
                reflectiveQuadToRelative(2.438f, 4.271f)
                close()
                moveTo(20f, 21.5f)
                quadToRelative(-2.5f, 0f, -4.167f, -1.667f)
                quadToRelative(-1.666f, -1.666f, -1.666f, -4.166f)
                quadToRelative(0f, -2.459f, 1.666f, -4.146f)
                quadTo(17.5f, 9.833f, 20f, 9.833f)
                reflectiveQuadToRelative(4.167f, 1.688f)
                quadToRelative(1.666f, 1.687f, 1.666f, 4.146f)
                quadToRelative(0f, 2.5f, -1.666f, 4.166f)
                quadTo(22.5f, 21.5f, 20f, 21.5f)
                close()
                moveToRelative(0f, 15.542f)
                quadToRelative(-3.5f, 0f, -6.604f, -1.334f)
                quadToRelative(-3.104f, -1.333f, -5.438f, -3.666f)
                quadToRelative(-2.333f, -2.334f, -3.666f, -5.438f)
                quadTo(2.958f, 23.5f, 2.958f, 20f)
                reflectiveQuadToRelative(1.334f, -6.604f)
                quadToRelative(1.333f, -3.104f, 3.666f, -5.438f)
                quadToRelative(2.334f, -2.333f, 5.438f, -3.687f)
                quadTo(16.5f, 2.917f, 20f, 2.917f)
                reflectiveQuadToRelative(6.604f, 1.354f)
                quadToRelative(3.104f, 1.354f, 5.438f, 3.687f)
                quadToRelative(2.333f, 2.334f, 3.687f, 5.438f)
                quadTo(37.083f, 16.5f, 37.083f, 20f)
                reflectiveQuadToRelative(-1.354f, 6.604f)
                quadToRelative(-1.354f, 3.104f, -3.687f, 5.438f)
                quadToRelative(-2.334f, 2.333f, -5.438f, 3.666f)
                quadTo(23.5f, 37.042f, 20f, 37.042f)
                close()
                moveToRelative(0f, -3.542f)
                quadToRelative(2.167f, 0f, 4.104f, -0.583f)
                quadToRelative(1.938f, -0.584f, 3.938f, -2.042f)
                quadToRelative(-2.042f, -1.333f, -3.959f, -2f)
                quadToRelative(-1.916f, -0.667f, -4.083f, -0.667f)
                quadToRelative(-2.167f, 0f, -4.083f, 0.646f)
                quadToRelative(-1.917f, 0.646f, -3.959f, 2.021f)
                quadToRelative(2.042f, 1.458f, 3.959f, 2.042f)
                quadToRelative(1.916f, 0.583f, 4.083f, 0.583f)
                close()
                moveToRelative(0f, -14.917f)
                quadToRelative(1.25f, 0f, 2.083f, -0.833f)
                quadToRelative(0.834f, -0.833f, 0.834f, -2.083f)
                quadToRelative(0f, -1.25f, -0.834f, -2.084f)
                quadToRelative(-0.833f, -0.833f, -2.083f, -0.833f)
                quadToRelative(-1.25f, 0f, -2.083f, 0.833f)
                quadToRelative(-0.834f, 0.834f, -0.834f, 2.084f)
                reflectiveQuadToRelative(0.834f, 2.083f)
                quadToRelative(0.833f, 0.833f, 2.083f, 0.833f)
                close()
                moveToRelative(0f, -2.916f)
                close()
                moveToRelative(0.042f, 15.208f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSettingsFilled(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "settings",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(15.5f, 37.042f)
                lineToRelative(-0.792f, -5.417f)
                quadToRelative(-0.25f, -0.042f, -0.916f, -0.417f)
                quadToRelative(-0.667f, -0.375f, -1.459f, -0.875f)
                lineToRelative(-5.041f, 2.25f)
                lineToRelative(-4.542f, -8.041f)
                lineToRelative(4.542f, -3.334f)
                quadToRelative(-0.084f, -0.25f, -0.084f, -0.562f)
                verticalLineTo(20f)
                quadToRelative(0f, -0.25f, 0.021f, -0.583f)
                quadToRelative(0.021f, -0.334f, 0.063f, -0.667f)
                lineTo(2.75f, 15.417f)
                lineToRelative(4.542f, -7.959f)
                lineToRelative(5.125f, 2.209f)
                quadToRelative(0.416f, -0.334f, 1.041f, -0.688f)
                quadToRelative(0.625f, -0.354f, 1.25f, -0.562f)
                lineToRelative(0.792f, -5.5f)
                horizontalLineToRelative(9f)
                lineToRelative(0.833f, 5.458f)
                quadToRelative(0.542f, 0.208f, 1.188f, 0.563f)
                quadToRelative(0.646f, 0.354f, 1.104f, 0.729f)
                lineToRelative(5.083f, -2.209f)
                lineToRelative(4.584f, 7.959f)
                lineToRelative(-4.667f, 3.333f)
                quadToRelative(0.042f, 0.292f, 0.063f, 0.604f)
                quadToRelative(0.02f, 0.313f, 0.02f, 0.646f)
                reflectiveQuadToRelative(-0.02f, 0.646f)
                quadToRelative(-0.021f, 0.312f, -0.063f, 0.604f)
                lineToRelative(4.625f, 3.292f)
                lineToRelative(-4.583f, 8.041f)
                lineToRelative(-5.084f, -2.291f)
                quadToRelative(-0.416f, 0.375f, -1f, 0.729f)
                quadToRelative(-0.583f, 0.354f, -1.25f, 0.604f)
                lineToRelative(-0.833f, 5.417f)
                close()
                moveToRelative(4.417f, -11.625f)
                quadToRelative(2.25f, 0f, 3.833f, -1.584f)
                quadTo(25.333f, 22.25f, 25.333f, 20f)
                reflectiveQuadToRelative(-1.583f, -3.833f)
                quadToRelative(-1.583f, -1.584f, -3.833f, -1.584f)
                reflectiveQuadToRelative(-3.834f, 1.584f)
                quadTo(14.5f, 17.75f, 14.5f, 20f)
                reflectiveQuadToRelative(1.583f, 3.833f)
                quadToRelative(1.584f, 1.584f, 3.834f, 1.584f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSettings(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "settings",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(15.5f, 37.042f)
                lineToRelative(-0.792f, -5.417f)
                quadToRelative(-0.25f, -0.042f, -0.916f, -0.417f)
                quadToRelative(-0.667f, -0.375f, -1.459f, -0.875f)
                lineToRelative(-5.041f, 2.25f)
                lineToRelative(-4.542f, -8.041f)
                lineToRelative(4.542f, -3.334f)
                quadToRelative(-0.084f, -0.25f, -0.084f, -0.562f)
                verticalLineTo(20f)
                quadToRelative(0f, -0.25f, 0.021f, -0.583f)
                quadToRelative(0.021f, -0.334f, 0.063f, -0.667f)
                lineTo(2.75f, 15.417f)
                lineToRelative(4.542f, -7.959f)
                lineToRelative(5.125f, 2.209f)
                quadToRelative(0.416f, -0.334f, 1.041f, -0.688f)
                quadToRelative(0.625f, -0.354f, 1.25f, -0.562f)
                lineToRelative(0.792f, -5.5f)
                horizontalLineToRelative(9f)
                lineToRelative(0.833f, 5.458f)
                quadToRelative(0.542f, 0.208f, 1.188f, 0.563f)
                quadToRelative(0.646f, 0.354f, 1.104f, 0.729f)
                lineToRelative(5.083f, -2.209f)
                lineToRelative(4.584f, 7.959f)
                lineToRelative(-4.667f, 3.333f)
                quadToRelative(0.042f, 0.292f, 0.063f, 0.604f)
                quadToRelative(0.02f, 0.313f, 0.02f, 0.646f)
                reflectiveQuadToRelative(-0.02f, 0.646f)
                quadToRelative(-0.021f, 0.312f, -0.063f, 0.604f)
                lineToRelative(4.625f, 3.292f)
                lineToRelative(-4.583f, 8.041f)
                lineToRelative(-5.084f, -2.291f)
                quadToRelative(-0.416f, 0.375f, -1f, 0.729f)
                quadToRelative(-0.583f, 0.354f, -1.25f, 0.604f)
                lineToRelative(-0.833f, 5.417f)
                close()
                moveToRelative(4.417f, -11.625f)
                quadToRelative(2.25f, 0f, 3.833f, -1.584f)
                quadTo(25.333f, 22.25f, 25.333f, 20f)
                reflectiveQuadToRelative(-1.583f, -3.833f)
                quadToRelative(-1.583f, -1.584f, -3.833f, -1.584f)
                reflectiveQuadToRelative(-3.834f, 1.584f)
                quadTo(14.5f, 17.75f, 14.5f, 20f)
                reflectiveQuadToRelative(1.583f, 3.833f)
                quadToRelative(1.584f, 1.584f, 3.834f, 1.584f)
                close()
                moveToRelative(0f, -2.625f)
                quadToRelative(-1.209f, 0f, -2f, -0.813f)
                quadToRelative(-0.792f, -0.812f, -0.792f, -1.979f)
                reflectiveQuadToRelative(0.792f, -1.979f)
                quadToRelative(0.791f, -0.813f, 2f, -0.813f)
                quadToRelative(1.166f, 0f, 1.979f, 0.813f)
                quadToRelative(0.812f, 0.812f, 0.812f, 1.979f)
                reflectiveQuadToRelative(-0.812f, 1.979f)
                quadToRelative(-0.813f, 0.813f, -1.979f, 0.813f)
                close()
                moveTo(20f, 19.958f)
                close()
                moveTo(18.458f, 33.5f)
                horizontalLineTo(21.5f)
                lineToRelative(0.625f, -4.542f)
                quadToRelative(1.333f, -0.333f, 2.521f, -1f)
                quadToRelative(1.187f, -0.666f, 2.146f, -1.75f)
                lineToRelative(4.333f, 1.875f)
                lineToRelative(1.417f, -2.541f)
                lineToRelative(-3.792f, -2.792f)
                quadToRelative(0.167f, -0.708f, 0.292f, -1.375f)
                quadToRelative(0.125f, -0.667f, 0.125f, -1.375f)
                reflectiveQuadToRelative(-0.105f, -1.375f)
                quadToRelative(-0.104f, -0.667f, -0.312f, -1.375f)
                lineToRelative(3.792f, -2.792f)
                lineToRelative(-1.375f, -2.583f)
                lineToRelative(-4.334f, 1.875f)
                quadToRelative(-0.916f, -1.042f, -2.125f, -1.771f)
                quadToRelative(-1.208f, -0.729f, -2.583f, -0.937f)
                lineToRelative(-0.583f, -4.584f)
                horizontalLineToRelative(-3.084f)
                lineTo(17.917f, 11f)
                quadToRelative(-1.459f, 0.333f, -2.625f, 1f)
                quadToRelative(-1.167f, 0.667f, -2.209f, 1.75f)
                lineToRelative(-4.25f, -1.875f)
                lineToRelative(-1.416f, 2.583f)
                lineToRelative(3.708f, 2.709f)
                quadToRelative(-0.167f, 0.75f, -0.292f, 1.437f)
                quadToRelative(-0.125f, 0.688f, -0.125f, 1.354f)
                quadToRelative(0f, 0.709f, 0.104f, 1.417f)
                quadToRelative(0.105f, 0.708f, 0.313f, 1.458f)
                lineToRelative(-3.708f, 2.709f)
                lineToRelative(1.416f, 2.541f)
                lineToRelative(4.25f, -1.833f)
                quadToRelative(1.042f, 1.042f, 2.25f, 1.729f)
                quadToRelative(1.209f, 0.688f, 2.584f, 1.021f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberDriveFileRenameOutlineFilled(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "drive_file_rename_outline",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(16.417f, 35f)
                lineToRelative(6.875f, -6.875f)
                horizontalLineToRelative(13.5f)
                verticalLineTo(35f)
                close()
                moveTo(30.75f, 13.208f)
                lineTo(25f, 7.5f)
                lineToRelative(1.75f, -1.75f)
                quadToRelative(0.833f, -0.875f, 2.042f, -0.875f)
                quadToRelative(1.208f, 0f, 2.125f, 0.875f)
                lineToRelative(1.625f, 1.625f)
                quadToRelative(0.916f, 0.875f, 0.854f, 2.063f)
                quadToRelative(-0.063f, 1.187f, -0.896f, 2.02f)
                close()
                moveToRelative(-1.833f, 1.834f)
                lineTo(8.958f, 35f)
                horizontalLineTo(3.25f)
                verticalLineToRelative(-5.708f)
                lineTo(23.167f, 9.333f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberDriveFileRenameOutline(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "drive_file_rename_outline",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(15.583f, 35f)
                lineToRelative(6.875f, -6.875f)
                horizontalLineToRelative(14.584f)
                verticalLineTo(35f)
                close()
                moveToRelative(-9.708f, -2.958f)
                horizontalLineToRelative(1.583f)
                lineToRelative(17f, -16.959f)
                lineToRelative(-1.583f, -1.583f)
                lineToRelative(-17f, 17f)
                close()
                moveToRelative(24.583f, -18.834f)
                lineTo(24.75f, 7.5f)
                lineToRelative(1.75f, -1.75f)
                quadToRelative(0.833f, -0.875f, 2.021f, -0.875f)
                quadToRelative(1.187f, 0f, 2.104f, 0.875f)
                lineToRelative(1.625f, 1.583f)
                quadToRelative(0.917f, 0.917f, 0.854f, 2.105f)
                quadToRelative(-0.062f, 1.187f, -0.896f, 2.02f)
                close()
                moveToRelative(-1.833f, 1.834f)
                lineTo(8.667f, 35f)
                horizontalLineTo(2.958f)
                verticalLineToRelative(-5.708f)
                lineTo(22.875f, 9.333f)
                close()
                moveToRelative(-4.958f, -0.75f)
                lineToRelative(-0.792f, -0.792f)
                lineToRelative(1.583f, 1.583f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberViewTimelineFilled(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "view_timeline",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(10.375f, 28.083f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.666f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(5f, -6.791f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(5f, -6.75f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.667f)
                horizontalLineToRelative(-9.292f)
                close()
                moveTo(8.125f, 35.417f)
                quadToRelative(-1.458f, 0f, -2.5f, -1.042f)
                reflectiveQuadToRelative(-1.042f, -2.5f)
                verticalLineTo(8.125f)
                quadToRelative(0f, -1.458f, 1.042f, -2.5f)
                reflectiveQuadToRelative(2.5f, -1.042f)
                horizontalLineToRelative(23.75f)
                quadToRelative(1.458f, 0f, 2.5f, 1.042f)
                reflectiveQuadToRelative(1.042f, 2.5f)
                verticalLineToRelative(23.75f)
                quadToRelative(0f, 1.458f, -1.042f, 2.5f)
                reflectiveQuadToRelative(-2.5f, 1.042f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberViewTimeline(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "view_timeline",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(10.375f, 28.083f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.666f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(5f, -6.791f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(5f, -6.75f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.667f)
                horizontalLineToRelative(-9.292f)
                close()
                moveTo(8.125f, 35.417f)
                quadToRelative(-1.458f, 0f, -2.5f, -1.042f)
                reflectiveQuadToRelative(-1.042f, -2.5f)
                verticalLineTo(8.125f)
                quadToRelative(0f, -1.458f, 1.042f, -2.5f)
                reflectiveQuadToRelative(2.5f, -1.042f)
                horizontalLineToRelative(23.75f)
                quadToRelative(1.458f, 0f, 2.5f, 1.042f)
                reflectiveQuadToRelative(1.042f, 2.5f)
                verticalLineToRelative(23.75f)
                quadToRelative(0f, 1.458f, -1.042f, 2.5f)
                reflectiveQuadToRelative(-2.5f, 1.042f)
                close()
                moveToRelative(0f, -3.542f)
                horizontalLineToRelative(23.75f)
                verticalLineTo(8.125f)
                horizontalLineTo(8.125f)
                verticalLineToRelative(23.75f)
                close()
                moveToRelative(0f, -23.75f)
                verticalLineToRelative(23.75f)
                verticalLineToRelative(-23.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberFilterAlt(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "filter_alt",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(23.875f, 21.833f)
                verticalLineToRelative(9.834f)
                quadToRelative(0f, 0.916f, -0.604f, 1.5f)
                quadToRelative(-0.604f, 0.583f, -1.521f, 0.583f)
                horizontalLineToRelative(-3.292f)
                quadToRelative(-0.958f, 0f, -1.625f, -0.667f)
                quadToRelative(-0.666f, -0.666f, -0.666f, -1.625f)
                verticalLineToRelative(-9.625f)
                lineTo(6.625f, 9.625f)
                quadToRelative(-0.875f, -1.042f, -0.271f, -2.208f)
                quadToRelative(0.604f, -1.167f, 1.938f, -1.167f)
                horizontalLineToRelative(23.416f)
                quadToRelative(1.334f, 0f, 1.938f, 1.167f)
                quadToRelative(0.604f, 1.166f, -0.229f, 2.208f)
                close()
                moveTo(20f, 21f)
                lineToRelative(8.833f, -11.208f)
                horizontalLineTo(11.167f)
                close()
                moveToRelative(0f, 0f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberLock(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "lock",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(9.792f, 37f)
                quadToRelative(-1.459f, 0f, -2.5f, -1.042f)
                quadToRelative(-1.042f, -1.041f, -1.042f, -2.5f)
                verticalLineTo(16.5f)
                quadToRelative(0f, -1.458f, 1.042f, -2.521f)
                quadToRelative(1.041f, -1.062f, 2.5f, -1.062f)
                horizontalLineToRelative(2.041f)
                verticalLineTo(9.792f)
                quadToRelative(0f, -3.459f, 2.375f, -5.834f)
                quadTo(16.583f, 1.583f, 20f, 1.583f)
                reflectiveQuadToRelative(5.792f, 2.375f)
                quadToRelative(2.375f, 2.375f, 2.375f, 5.834f)
                verticalLineToRelative(3.125f)
                horizontalLineToRelative(2.041f)
                quadToRelative(1.459f, 0f, 2.5f, 1.062f)
                quadToRelative(1.042f, 1.063f, 1.042f, 2.521f)
                verticalLineToRelative(16.958f)
                quadToRelative(0f, 1.459f, -1.042f, 2.5f)
                quadTo(31.667f, 37f, 30.208f, 37f)
                close()
                moveToRelative(5.583f, -24.083f)
                horizontalLineToRelative(9.25f)
                verticalLineTo(9.792f)
                quadToRelative(0f, -1.959f, -1.333f, -3.313f)
                quadTo(21.958f, 5.125f, 20f, 5.125f)
                quadToRelative(-1.958f, 0f, -3.292f, 1.333f)
                quadToRelative(-1.333f, 1.334f, -1.333f, 3.334f)
                close()
                moveTo(9.792f, 33.458f)
                horizontalLineToRelative(20.416f)
                verticalLineTo(16.5f)
                horizontalLineTo(9.792f)
                verticalLineToRelative(16.958f)
                close()
                moveTo(20f, 28.125f)
                quadToRelative(1.292f, 0f, 2.229f, -0.896f)
                quadToRelative(0.938f, -0.896f, 0.938f, -2.187f)
                quadToRelative(0f, -1.25f, -0.938f, -2.25f)
                quadToRelative(-0.937f, -1f, -2.229f, -1f)
                reflectiveQuadToRelative(-2.229f, 0.979f)
                quadToRelative(-0.938f, 0.979f, -0.938f, 2.271f)
                quadToRelative(0f, 1.25f, 0.938f, 2.166f)
                quadToRelative(0.937f, 0.917f, 2.229f, 0.917f)
                close()
                moveToRelative(0f, -3.167f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPushPin(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "push_pin",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(27f, 20.25f)
                lineToRelative(3.458f, 3.167f)
                verticalLineToRelative(3.541f)
                horizontalLineToRelative(-8.666f)
                verticalLineToRelative(9.75f)
                lineTo(20f, 38.5f)
                lineToRelative(-1.75f, -1.792f)
                verticalLineToRelative(-9.75f)
                horizontalLineTo(9.583f)
                verticalLineToRelative(-3.541f)
                lineToRelative(3.25f, -3.167f)
                verticalLineTo(7.667f)
                horizontalLineToRelative(-1.916f)
                verticalLineToRelative(-3.5f)
                horizontalLineToRelative(18.041f)
                verticalLineToRelative(3.5f)
                horizontalLineTo(27f)
                close()
                moveToRelative(-12.583f, 3.167f)
                horizontalLineToRelative(11f)
                lineToRelative(-1.959f, -1.792f)
                verticalLineTo(7.667f)
                horizontalLineToRelative(-7.083f)
                verticalLineToRelative(13.958f)
                close()
                moveToRelative(5.5f, 0f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberClearNight(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "clear_night",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(19.958f, 33.417f)
                quadToRelative(2.084f, 0f, 4.25f, -0.771f)
                quadToRelative(2.167f, -0.771f, 4.292f, -2.396f)
                quadToRelative(-4.25f, -1.5f, -7.125f, -3.917f)
                quadToRelative(-2.875f, -2.416f, -4.458f, -5.458f)
                quadToRelative(-1.584f, -3.042f, -1.959f, -6.542f)
                reflectiveQuadToRelative(0.292f, -7.125f)
                quadToRelative(-4.208f, 1.875f, -6.562f, 5.23f)
                quadTo(6.333f, 15.792f, 6.333f, 20f)
                quadToRelative(0f, 5.917f, 3.855f, 9.667f)
                quadToRelative(3.854f, 3.75f, 9.77f, 3.75f)
                close()
                moveToRelative(0f, 3.583f)
                quadToRelative(-3.708f, 0f, -6.854f, -1.292f)
                quadToRelative(-3.146f, -1.291f, -5.417f, -3.541f)
                quadToRelative(-2.27f, -2.25f, -3.562f, -5.375f)
                quadTo(2.833f, 23.667f, 2.833f, 20f)
                quadToRelative(0f, -6.708f, 4.25f, -11.25f)
                reflectiveQuadToRelative(10.084f, -5.583f)
                quadToRelative(1.291f, -0.209f, 1.916f, 0.625f)
                quadToRelative(0.625f, 0.833f, 0.167f, 2.208f)
                quadToRelative(-1.208f, 3.625f, -0.896f, 7.167f)
                quadToRelative(0.313f, 3.541f, 1.938f, 6.479f)
                quadToRelative(1.625f, 2.937f, 4.52f, 5f)
                quadToRelative(2.896f, 2.062f, 6.938f, 2.771f)
                quadToRelative(1.417f, 0.25f, 1.854f, 1.166f)
                quadToRelative(0.438f, 0.917f, -0.396f, 1.917f)
                quadToRelative(-2.541f, 3f, -6f, 4.75f)
                quadTo(23.75f, 37f, 19.958f, 37f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPalette(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "palette",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
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
                moveTo(20f, 37.042f)
                quadToRelative(-3.5f, 0f, -6.604f, -1.334f)
                quadToRelative(-3.104f, -1.333f, -5.438f, -3.666f)
                quadToRelative(-2.333f, -2.334f, -3.666f, -5.438f)
                quadTo(2.958f, 23.5f, 2.958f, 20f)
                quadToRelative(0f, -3.583f, 1.354f, -6.687f)
                quadToRelative(1.355f, -3.105f, 3.709f, -5.417f)
                quadToRelative(2.354f, -2.313f, 5.521f, -3.646f)
                quadToRelative(3.166f, -1.333f, 6.75f, -1.333f)
                quadToRelative(3.416f, 0f, 6.437f, 1.166f)
                quadToRelative(3.021f, 1.167f, 5.333f, 3.188f)
                quadToRelative(2.313f, 2.021f, 3.667f, 4.812f)
                quadToRelative(1.354f, 2.792f, 1.354f, 6.042f)
                quadToRelative(0f, 4.667f, -2.687f, 7.479f)
                quadToRelative(-2.688f, 2.813f, -7.188f, 2.813f)
                horizontalLineToRelative(-2.583f)
                quadToRelative(-0.625f, 0f, -1.042f, 0.437f)
                quadToRelative(-0.416f, 0.438f, -0.416f, 1.021f)
                quadToRelative(0f, 0.917f, 0.5f, 1.563f)
                quadToRelative(0.5f, 0.645f, 0.5f, 1.645f)
                quadToRelative(0f, 1.709f, -1.125f, 2.834f)
                reflectiveQuadTo(20f, 37.042f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(-9.208f, 1.208f)
                quadToRelative(0.875f, 0f, 1.5f, -0.625f)
                reflectiveQuadToRelative(0.625f, -1.541f)
                quadToRelative(0f, -0.875f, -0.625f, -1.5f)
                reflectiveQuadToRelative(-1.5f, -0.625f)
                quadToRelative(-0.875f, 0f, -1.5f, 0.625f)
                reflectiveQuadToRelative(-0.625f, 1.5f)
                quadToRelative(0f, 0.916f, 0.625f, 1.541f)
                quadToRelative(0.625f, 0.625f, 1.5f, 0.625f)
                close()
                moveToRelative(5f, -6.708f)
                quadToRelative(0.916f, 0f, 1.541f, -0.625f)
                quadToRelative(0.625f, -0.625f, 0.625f, -1.542f)
                quadToRelative(0f, -0.875f, -0.625f, -1.5f)
                reflectiveQuadToRelative(-1.5f, -0.625f)
                quadToRelative(-0.916f, 0f, -1.541f, 0.625f)
                quadToRelative(-0.625f, 0.625f, -0.625f, 1.5f)
                quadToRelative(0f, 0.917f, 0.625f, 1.542f)
                reflectiveQuadToRelative(1.5f, 0.625f)
                close()
                moveToRelative(8.416f, 0f)
                quadToRelative(0.917f, 0f, 1.542f, -0.625f)
                reflectiveQuadToRelative(0.625f, -1.542f)
                quadToRelative(0f, -0.875f, -0.625f, -1.5f)
                reflectiveQuadToRelative(-1.542f, -0.625f)
                quadToRelative(-0.875f, 0f, -1.5f, 0.625f)
                reflectiveQuadToRelative(-0.625f, 1.5f)
                quadToRelative(0f, 0.917f, 0.625f, 1.542f)
                reflectiveQuadToRelative(1.5f, 0.625f)
                close()
                moveToRelative(5.084f, 6.708f)
                quadToRelative(0.875f, 0f, 1.5f, -0.625f)
                reflectiveQuadToRelative(0.625f, -1.541f)
                quadToRelative(0f, -0.875f, -0.625f, -1.5f)
                reflectiveQuadToRelative(-1.5f, -0.625f)
                quadToRelative(-0.917f, 0f, -1.521f, 0.625f)
                quadToRelative(-0.604f, 0.625f, -0.604f, 1.5f)
                quadToRelative(0f, 0.916f, 0.604f, 1.541f)
                quadToRelative(0.604f, 0.625f, 1.521f, 0.625f)
                close()
                moveTo(19.917f, 33.5f)
                quadToRelative(0.333f, 0f, 0.521f, -0.167f)
                quadToRelative(0.187f, -0.166f, 0.187f, -0.5f)
                quadToRelative(0f, -0.583f, -0.604f, -1.125f)
                quadToRelative(-0.604f, -0.541f, -0.604f, -2.125f)
                quadToRelative(0f, -1.916f, 1.291f, -3.312f)
                quadToRelative(1.292f, -1.396f, 3.209f, -1.396f)
                horizontalLineToRelative(3.291f)
                quadToRelative(3f, 0f, 4.646f, -1.729f)
                reflectiveQuadTo(33.5f, 18.25f)
                quadToRelative(0f, -5.25f, -3.979f, -8.5f)
                reflectiveQuadTo(20.333f, 6.5f)
                quadToRelative(-5.791f, 0f, -9.812f, 3.938f)
                quadTo(6.5f, 14.375f, 6.5f, 20f)
                reflectiveQuadToRelative(3.917f, 9.562f)
                quadToRelative(3.916f, 3.938f, 9.5f, 3.938f)
                close()
            }
        }.build()
    }
}