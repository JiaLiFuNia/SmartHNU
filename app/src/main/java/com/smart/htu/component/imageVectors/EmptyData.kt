package com.smart.htu.component.imageVectors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.smart.htu.R
import com.smart.htu.ui.theme.isInDarkTheme

/*@Composable
fun emptyData(): ImageVector {
    return Builder(
        name = "EmptyData",
        defaultWidth = 748.97.dp,
        defaultHeight = 457.27.dp,
        viewportWidth = 748.97f,
        viewportHeight = 457.27f
    ).apply {
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(384.22f, 369.78f)
            lineTo(122.39f, 437.21f)
            arcToRelative(
                34.62f,
                34.62f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -42.11f,
                -24.87f
            )
            lineTo(1.1f, 104.84f)
            arcToRelative(
                34.62f,
                34.62f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                24.87f,
                -42.11f
            )
            lineToRelative(243.59f, -62.73f)
            lineTo(331.9f, 27.91f)
            lineToRelative(77.19f, 299.76f)
            arcTo(
                34.62f,
                34.62f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                384.22f,
                369.78f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.background)) {
            moveTo(28.44f, 72.37f)
            arcToRelative(
                24.66f,
                24.66f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                -17.71f,
                29.99f
            )
            lineToRelative(79.18f, 307.5f)
            arcToRelative(
                24.66f,
                24.66f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                29.99f,
                17.71f
            )
            lineTo(381.74f, 360.14f)
            arcToRelative(
                24.66f,
                24.66f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                17.71f,
                -29.99f
            )
            lineTo(323.45f, 35.04f)
            lineToRelative(-54.79f, -24.53f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(331.23f, 29.5f)
            lineToRelative(-40.52f, 10.44f)
            arcToRelative(
                11.52f,
                11.52f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -14.03f,
                -8.28f
            )
            lineToRelative(-7.71f, -29.93f)
            arcToRelative(
                0.72f,
                0.72f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                0.99f,
                -0.84f
            )
            lineToRelative(61.38f, 27.26f)
            arcToRelative(
                0.72f,
                0.72f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -0.11f,
                1.36f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(309.91f, 292.1f)
            lineToRelative(-119.21f, 30.7f)
            arcToRelative(
                5.76f,
                5.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -2.87f,
                -11.15f
            )
            lineToRelative(119.21f, -30.7f)
            arcToRelative(
                5.76f,
                5.76f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                2.87f,
                11.15f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(337.76f, 304.99f)
            lineTo(195.55f, 341.61f)
            arcToRelative(
                5.76f,
                5.76f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -2.87f,
                -11.15f
            )
            lineToRelative(142.21f, -36.62f)
            arcToRelative(
                5.76f,
                5.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                2.87f,
                11.15f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(142.05f, 339.41f)
            moveToRelative(-20.35f, 0f)
            arcToRelative(
                20.35f,
                20.35f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                40.71f,
                0f
            )
            arcToRelative(
                20.35f,
                20.35f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -40.71f,
                0f
            )
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.background)) {
            moveTo(297.24f, 238.83f)
            lineTo(138.92f, 279.58f)
            arcToRelative(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -21.69f,
                -12.81f
            )
            lineTo(84.05f, 137.88f)
            arcTo(17.83f, 17.83f, 0f, isMoreThanHalf = false, isPositiveArc = true, 96.86f, 116.19f)
            lineToRelative(158.32f, -40.75f)
            arcToRelative(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                21.69f,
                12.81f
            )
            lineToRelative(33.18f, 128.89f)
            arcTo(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                297.24f,
                238.83f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(297.24f, 238.83f)
            lineTo(138.92f, 279.58f)
            arcToRelative(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -21.69f,
                -12.81f
            )
            lineTo(84.05f, 137.88f)
            arcTo(17.83f, 17.83f, 0f, isMoreThanHalf = false, isPositiveArc = true, 96.86f, 116.19f)
            lineToRelative(158.32f, -40.75f)
            arcToRelative(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                21.69f,
                12.81f
            )
            lineToRelative(33.18f, 128.89f)
            arcTo(
                17.83f,
                17.83f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                297.24f,
                238.83f
            )
            close()
            moveTo(97.38f, 118.22f)
            arcToRelative(
                15.74f,
                15.74f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                -11.3f,
                19.14f
            )
            lineToRelative(33.18f, 128.89f)
            arcToRelative(
                15.74f,
                15.74f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                19.14f,
                11.3f
            )
            lineTo(296.72f, 236.8f)
            arcToRelative(
                15.74f,
                15.74f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                11.3f,
                -19.14f
            )
            lineTo(274.84f, 88.77f)
            arcToRelative(
                15.74f,
                15.74f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                -19.14f,
                -11.3f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(247.15f, 118.77f)
            lineToRelative(-79.9f, 20.57f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(81.33f, -20.94f)
            curveToRelative(3.29f, 1.66f, 2.42f, 5.07f, 0.09f, 5.67f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(250.75f, 132.78f)
            lineToRelative(-79.9f, 20.57f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(81.33f, -20.94f)
            curveToRelative(3.29f, 1.66f, 2.42f, 5.07f, 0.09f, 5.67f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(155.63f, 165.78f)
            lineTo(131.25f, 172.05f)
            arcToRelative(
                3.1f,
                3.1f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.78f,
                -2.23f
            )
            lineTo(120.06f, 141.05f)
            arcToRelative(
                3.1f,
                3.1f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                2.23f,
                -3.78f
            )
            lineToRelative(24.39f, -6.28f)
            arcToRelative(
                3.11f,
                3.11f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                3.78f,
                2.23f
            )
            lineToRelative(7.41f, 28.78f)
            arcToRelative(
                3.1f,
                3.1f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -2.23f,
                3.78f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(257.91f, 161.54f)
            lineTo(135.96f, 192.93f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(123.38f, -31.76f)
            curveToRelative(3.29f, 1.66f, 2.42f, 5.07f, 0.09f, 5.67f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(261.52f, 175.56f)
            lineTo(139.57f, 206.95f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(123.38f, -31.76f)
            curveToRelative(3.29f, 1.66f, 2.42f, 5.07f, 0.09f, 5.67f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(265.12f, 189.57f)
            lineTo(143.18f, 220.96f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(123.38f, -31.76f)
            curveToRelative(3.29f, 1.66f, 2.42f, 5.07f, 0.09f, 5.67f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(268.73f, 203.59f)
            lineTo(146.79f, 234.98f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(123.38f, -31.76f)
            curveTo(271.93f, 199.58f, 271.06f, 202.99f, 268.73f, 203.59f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(272.34f, 217.6f)
            lineTo(150.39f, 248.99f)
            arcToRelative(
                2.86f,
                2.86f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -3.47f,
                -1.8f
            )
            arcToRelative(
                2.76f,
                2.76f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.94f,
                -3.5f
            )
            lineToRelative(123.38f, -31.76f)
            curveTo(275.53f, 213.59f, 274.67f, 217f, 272.34f, 217.6f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(143.55f, 346.54f)
            arcToRelative(
                2.11f,
                2.11f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -1.34f,
                -0.09f
            )
            lineToRelative(-0.03f, -0.01f)
            lineToRelative(-5.55f, -2.35f)
            arcToRelative(
                2.13f,
                2.13f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                1.66f,
                -3.91f
            )
            lineToRelative(3.59f, 1.53f)
            lineToRelative(4.71f, -11.08f)
            arcToRelative(
                2.13f,
                2.13f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                2.79f,
                -1.12f
            )
            horizontalLineToRelative(0f)
            lineToRelative(-0.03f, 0.07f)
            lineToRelative(0.03f, -0.07f)
            arcToRelative(
                2.13f,
                2.13f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                1.12f,
                2.79f
            )
            lineToRelative(-5.54f, 13.02f)
            arcToRelative(
                2.13f,
                2.13f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -1.43f,
                1.22f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(641.24f, 450.68f)
            lineTo(375.84f, 399.09f)
            arcTo(
                34.62f,
                34.62f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                348.49f,
                358.54f
            )
            lineTo(409.08f, 46.84f)
            arcTo(
                34.62f,
                34.62f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                449.62f,
                19.49f
            )
            lineTo(696.54f, 67.49f)
            lineToRelative(44.31f, 51.99f)
            lineTo(681.79f, 423.33f)
            arcTo(
                34.62f,
                34.62f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                641.24f,
                450.68f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.background)) {
            moveTo(447.72f, 29.27f)
            arcTo(
                24.66f,
                24.66f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                418.85f,
                48.74f
            )
            lineTo(358.26f, 360.44f)
            arcToRelative(
                24.66f,
                24.66f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                19.48f,
                28.87f
            )
            lineTo(643.14f, 440.9f)
            arcToRelative(
                24.66f,
                24.66f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                28.87f,
                -19.48f
            )
            lineTo(730.16f, 122.29f)
            lineTo(691.22f, 76.6f)
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(643.89f, 161.46f)
            lineTo(523.05f, 137.97f)
            arcTo(5.76f, 5.76f, 56f, isMoreThanHalf = false, isPositiveArc = true, 525.25f, 126.66f)
            lineToRelative(120.84f, 23.49f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -2.2f,
                11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(663.5f, 185.07f)
            lineTo(519.35f, 157.05f)
            arcTo(5.76f, 5.76f, 56f, isMoreThanHalf = false, isPositiveArc = true, 521.54f, 145.74f)
            lineTo(665.7f, 173.76f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -2.2f,
                11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(629.47f, 235.65f)
            lineTo(508.64f, 212.16f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = false,
                -2.2f,
                11.31f
            )
            lineTo(627.27f, 246.96f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                2.2f,
                -11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(649.08f, 259.26f)
            lineTo(504.93f, 231.24f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = false,
                -2.2f,
                11.31f
            )
            lineToRelative(144.15f, 28.02f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = false,
                2.2f,
                -11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(610.65f, 332.46f)
            lineTo(489.81f, 308.97f)
            arcTo(5.76f, 5.76f, 56f, isMoreThanHalf = false, isPositiveArc = true, 492.01f, 297.67f)
            lineToRelative(120.84f, 23.49f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -2.2f,
                11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(630.26f, 356.07f)
            lineTo(486.1f, 328.05f)
            arcTo(5.76f, 5.76f, 56f, isMoreThanHalf = false, isPositiveArc = true, 488.3f, 316.75f)
            lineTo(632.46f, 344.77f)
            arcToRelative(
                5.76f,
                5.76f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -2.2f,
                11.31f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(471.98f, 132.07f)
            moveToRelative(-19.98f, -3.88f)
            arcToRelative(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                39.96f,
                7.77f
            )
            arcToRelative(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -39.96f,
                -7.77f
            )
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.onSurface)) {
            moveTo(468.52f, 139.31f)
            arcToRelative(
                2.11f,
                2.11f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -1.17f,
                -0.66f
            )
            lineToRelative(-0.02f, -0.02f)
            lineTo(463.34f, 134.13f)
            arcTo(2.13f, 2.13f, 56f, isMoreThanHalf = false, isPositiveArc = true, 466.52f, 131.31f)
            lineToRelative(2.59f, 2.92f)
            lineTo(478.12f, 126.25f)
            arcTo(
                2.13f,
                2.13f,
                132.55f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                481.12f,
                126.44f
            )
            lineToRelative(0f, 0f)
            lineTo(481.06f, 126.49f)
            lineToRelative(0.06f, -0.05f)
            arcToRelative(
                2.13f,
                2.13f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -0.18f,
                3f
            )
            lineTo(470.34f, 138.82f)
            arcToRelative(
                2.13f,
                2.13f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -1.82f,
                0.49f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(477.39f, 221.86f)
            arcTo(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                453.53f,
                237.96f
            )
            arcToRelative(
                4.37f,
                4.37f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -0.51f,
                -0.12f
            )
            arcTo(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                477.39f,
                221.86f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(438.74f, 303.08f)
            moveToRelative(-19.98f, -3.88f)
            arcToRelative(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                39.96f,
                7.77f
            )
            arcToRelative(
                20.35f,
                20.35f,
                56f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -39.96f,
                -7.77f
            )
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(739.57f, 120.62f)
            lineTo(698.49f, 112.64f)
            arcTo(
                11.52f,
                11.52f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                689.39f,
                99.14f
            )
            lineTo(695.28f, 68.8f)
            arcTo(0.72f, 0.72f, 56f, isMoreThanHalf = false, isPositiveArc = true, 696.54f, 68.47f)
            lineToRelative(43.72f, 50.98f)
            arcToRelative(
                0.72f,
                0.72f,
                56f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -0.68f,
                1.18f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(496.1f, 423.95f)
            arcToRelative(
                9.16f,
                9.16f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                -12.59f,
                3.05f
            )
            lineTo(376.66f, 361.83f)
            arcToRelative(
                9.16f,
                9.16f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                9.54f,
                -15.64f
            )
            lineToRelative(106.85f, 65.18f)
            arcToRelative(
                9.16f,
                9.16f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                3.05f,
                12.59f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.primary)) {
            moveTo(389.24f, 358.78f)
            arcToRelative(
                73.26f,
                73.26f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -24.4f,
                -100.7f
            )
            arcTo(
                73.26f,
                73.26f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                389.24f,
                358.78f
            )
            close()
            moveTo(279.78f, 292.01f)
            arcToRelative(
                54.95f,
                54.95f,
                0f,
                isMoreThanHalf = true,
                isPositiveArc = false,
                75.52f,
                -18.3f
            )
            arcToRelative(
                54.95f,
                54.95f,
                0f,
                isMoreThanHalf = false,
                isPositiveArc = false,
                -75.52f,
                18.3f
            )
            close()
        }
        path(fill = SolidColor(MiuixTheme.colorScheme.secondaryContainer)) {
            moveTo(325.88f, 319.86f)
            moveToRelative(-53.9f, -18.56f)
            arcToRelative(
                57.01f,
                57.01f,
                64f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                107.8f,
                37.12f
            )
            arcToRelative(
                57.01f,
                57.01f,
                64f,
                isMoreThanHalf = true,
                isPositiveArc = true,
                -107.8f,
                -37.12f
            )
        }
    }.build()
}*/

@Composable
fun emptyData(): ImageVector {
    return ImageVector.vectorResource(id = if (isInDarkTheme()) R.drawable.empty_dark else R.drawable.empty)
}