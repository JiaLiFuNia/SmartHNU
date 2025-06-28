package com.smart.htu.component.svgVector.drawablevectors

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.smart.htu.component.svgVector.DrawableVectors
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun DrawableVectors.appIcon(): ImageVector {
    return Builder(
        name = "appIcon",
        defaultWidth = 512.0.dp,
        defaultHeight = 512.0.dp,
        viewportWidth = 512.0f,
        viewportHeight = 512.0f
    ).apply {
        path(
            fill = SolidColor(MiuixTheme.colorScheme.surface),
            stroke = null,
            strokeLineWidth = 0.0f,
            strokeLineCap = Butt,
            strokeLineJoin = Miter,
            strokeLineMiter = 4.0f,
            pathFillType = NonZero
        ) {
            moveTo(80.0f, 0.0f)
            lineTo(432.0f, 0.0f)
            arcTo(80.0f, 80.0f, 0.0f, false, true, 512.0f, 80.0f)
            lineTo(512.0f, 432.0f)
            arcTo(80.0f, 80.0f, 0.0f, false, true, 432.0f, 512.0f)
            lineTo(80.0f, 512.0f)
            arcTo(80.0f, 80.0f, 0.0f, false, true, 0.0f, 432.0f)
            lineTo(0.0f, 80.0f)
            arcTo(80.0f, 80.0f, 0.0f, false, true, 80.0f, 0.0f)
            close()
        }
        group {
            path(
                fill = linearGradient(
                    0.0f to Color(0xFF3ED1A5), 1.0f to Color(0xFF28ABC5),
                    start = Offset(256.5f, 107.0f), end = Offset(256.5f, 404.0f)
                ), stroke = null,
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(261.16f, 107.82f)
                curveTo(259.43f, 108.27f, 255.23f, 110.27f, 251.82f, 112.26f)
                curveTo(241.65f, 118.19f, 240.0f, 118.43f, 232.93f, 114.97f)
                curveTo(224.39f, 110.8f, 215.82f, 109.7f, 207.68f, 111.76f)
                curveTo(198.87f, 113.98f, 194.58f, 117.51f, 186.78f, 128.98f)
                curveTo(183.72f, 133.48f, 180.09f, 138.07f, 178.7f, 139.17f)
                curveTo(175.22f, 141.95f, 170.7f, 143.44f, 161.95f, 144.69f)
                curveTo(149.43f, 146.47f, 144.64f, 148.32f, 138.58f, 153.68f)
                curveTo(134.89f, 156.95f, 129.69f, 164.52f, 127.76f, 169.44f)
                curveTo(125.8f, 174.43f, 125.75f, 180.51f, 127.64f, 184.58f)
                curveTo(129.92f, 189.52f, 132.26f, 191.96f, 140.64f, 198.14f)
                curveTo(144.99f, 201.35f, 150.56f, 206.14f, 153.03f, 208.78f)
                curveTo(175.87f, 233.32f, 187.14f, 271.55f, 181.5f, 305.32f)
                curveTo(180.64f, 310.43f, 188.36f, 293.7f, 192.12f, 282.3f)
                curveTo(197.53f, 265.85f, 198.89f, 257.3f, 198.84f, 239.99f)
                curveTo(198.8f, 226.81f, 198.63f, 224.69f, 197.02f, 217.28f)
                curveTo(193.59f, 201.55f, 188.21f, 188.59f, 178.57f, 172.83f)
                curveTo(173.69f, 164.85f, 173.23f, 163.76f, 174.75f, 163.76f)
                curveTo(175.95f, 163.76f, 176.85f, 164.86f, 181.9f, 172.48f)
                curveTo(190.85f, 185.99f, 198.35f, 202.7f, 201.56f, 216.26f)
                curveTo(204.6f, 229.13f, 205.48f, 249.35f, 203.57f, 262.36f)
                curveTo(202.41f, 270.3f, 199.26f, 283.28f, 196.93f, 289.72f)
                curveTo(195.99f, 292.33f, 195.33f, 294.57f, 195.47f, 294.71f)
                curveTo(196.54f, 295.75f, 205.93f, 279.11f, 211.16f, 266.9f)
                curveTo(214.97f, 258.01f, 220.11f, 242.38f, 222.23f, 233.22f)
                curveTo(225.55f, 218.9f, 226.66f, 209.65f, 227.06f, 193.12f)
                curveTo(227.58f, 171.36f, 225.97f, 157.6f, 221.15f, 142.6f)
                curveTo(219.04f, 136.05f, 218.98f, 134.85f, 220.78f, 135.52f)
                curveTo(222.8f, 136.27f, 227.54f, 151.78f, 229.52f, 164.08f)
                curveTo(235.05f, 198.46f, 230.29f, 237.04f, 216.3f, 271.25f)
                curveTo(214.33f, 276.08f, 212.97f, 280.04f, 213.29f, 280.04f)
                curveTo(214.7f, 280.04f, 231.93f, 256.48f, 239.79f, 243.81f)
                curveTo(244.42f, 236.33f, 253.32f, 218.49f, 256.22f, 210.83f)
                curveTo(263.67f, 191.22f, 267.01f, 173.43f, 267.76f, 149.39f)
                curveTo(268.07f, 139.4f, 268.39f, 135.98f, 269.0f, 135.98f)
                curveTo(270.45f, 135.98f, 270.64f, 137.75f, 270.63f, 151.38f)
                curveTo(270.62f, 177.26f, 266.66f, 198.11f, 257.5f, 220.56f)
                curveTo(253.74f, 229.78f, 246.15f, 244.48f, 240.4f, 253.72f)
                lineTo(236.07f, 260.66f)
                lineTo(239.17f, 258.14f)
                curveTo(240.87f, 256.75f, 246.46f, 251.95f, 251.57f, 247.47f)
                curveTo(256.69f, 243.0f, 261.39f, 239.34f, 262.02f, 239.34f)
                curveTo(262.64f, 239.34f, 263.35f, 239.84f, 263.6f, 240.46f)
                curveTo(264.16f, 241.87f, 258.41f, 247.3f, 218.46f, 283.04f)
                curveTo(170.21f, 326.21f, 146.91f, 348.31f, 129.36f, 367.57f)
                curveTo(117.87f, 380.18f, 108.96f, 391.93f, 108.13f, 395.57f)
                curveTo(107.28f, 399.34f, 110.53f, 403.09f, 115.29f, 403.84f)
                curveTo(119.47f, 404.5f, 121.44f, 403.2f, 125.09f, 397.35f)
                curveTo(137.61f, 377.28f, 160.21f, 351.6f, 209.17f, 301.81f)
                curveTo(224.68f, 286.04f, 225.07f, 285.7f, 234.93f, 279.45f)
                curveTo(274.22f, 254.54f, 308.95f, 242.2f, 357.75f, 235.79f)
                curveTo(372.19f, 233.9f, 374.09f, 233.82f, 374.09f, 235.1f)
                curveTo(374.09f, 236.3f, 373.44f, 236.47f, 362.75f, 238.04f)
                curveTo(327.54f, 243.22f, 299.57f, 251.42f, 273.64f, 264.18f)
                curveTo(261.6f, 270.11f, 246.97f, 278.69f, 236.67f, 285.86f)
                lineTo(229.73f, 290.69f)
                lineTo(240.34f, 286.7f)
                curveTo(253.21f, 281.86f, 265.93f, 278.09f, 277.98f, 275.55f)
                curveTo(308.35f, 269.15f, 342.48f, 268.78f, 365.42f, 274.6f)
                curveTo(372.31f, 276.35f, 376.21f, 277.96f, 376.62f, 279.23f)
                curveTo(376.99f, 280.39f, 375.5f, 280.24f, 369.22f, 278.48f)
                curveTo(358.76f, 275.54f, 349.82f, 274.6f, 332.38f, 274.61f)
                curveTo(316.1f, 274.61f, 309.57f, 275.12f, 294.67f, 277.51f)
                curveTo(267.12f, 281.94f, 238.26f, 292.49f, 213.71f, 307.09f)
                lineTo(206.51f, 311.37f)
                lineTo(215.38f, 308.6f)
                curveTo(236.91f, 301.9f, 251.2f, 299.75f, 270.91f, 300.23f)
                curveTo(284.71f, 300.57f, 290.79f, 301.34f, 302.88f, 304.29f)
                curveTo(315.48f, 307.35f, 330.48f, 313.57f, 343.39f, 321.07f)
                curveTo(351.01f, 325.5f, 351.95f, 326.27f, 350.94f, 327.25f)
                curveTo(350.4f, 327.78f, 347.72f, 326.65f, 340.94f, 323.07f)
                curveTo(315.95f, 309.82f, 293.7f, 304.57f, 266.3f, 305.45f)
                curveTo(248.98f, 306.0f, 231.84f, 309.63f, 214.09f, 316.49f)
                curveTo(206.91f, 319.26f, 196.27f, 324.3f, 198.88f, 323.68f)
                curveTo(202.46f, 322.84f, 212.29f, 322.03f, 218.89f, 322.03f)
                curveTo(238.39f, 322.04f, 256.67f, 326.24f, 274.98f, 334.9f)
                curveTo(290.74f, 342.37f, 301.38f, 350.66f, 311.01f, 363.02f)
                curveTo(319.95f, 374.48f, 323.29f, 376.6f, 332.38f, 376.61f)
                curveTo(338.13f, 376.61f, 338.63f, 376.49f, 344.24f, 373.79f)
                curveTo(351.94f, 370.08f, 359.03f, 363.78f, 361.93f, 358.07f)
                curveTo(363.11f, 355.74f, 364.47f, 351.51f, 365.1f, 348.19f)
                curveTo(365.7f, 344.99f, 366.64f, 340.05f, 367.19f, 337.21f)
                curveTo(367.74f, 334.36f, 368.97f, 330.65f, 369.92f, 328.96f)
                curveTo(371.95f, 325.33f, 374.38f, 323.34f, 384.96f, 316.62f)
                curveTo(393.53f, 311.17f, 396.39f, 308.16f, 398.86f, 302.0f)
                curveTo(402.59f, 292.69f, 401.95f, 283.62f, 396.87f, 273.58f)
                curveTo(395.36f, 270.59f, 394.13f, 267.7f, 394.15f, 267.15f)
                curveTo(394.21f, 265.12f, 396.08f, 261.08f, 399.39f, 255.85f)
                curveTo(404.01f, 248.56f, 404.96f, 245.83f, 404.99f, 239.66f)
                curveTo(405.05f, 230.17f, 401.15f, 224.9f, 390.36f, 219.92f)
                curveTo(381.0f, 215.6f, 379.44f, 212.37f, 382.48f, 203.54f)
                curveTo(384.32f, 198.16f, 384.33f, 198.1f, 383.17f, 195.17f)
                curveTo(381.75f, 191.57f, 377.37f, 187.61f, 370.25f, 183.5f)
                curveTo(360.62f, 177.94f, 353.77f, 177.27f, 346.28f, 181.15f)
                curveTo(343.97f, 182.34f, 337.22f, 187.67f, 329.71f, 194.23f)
                curveTo(293.94f, 225.44f, 284.78f, 231.71f, 280.69f, 227.75f)
                curveTo(278.81f, 225.93f, 278.96f, 223.97f, 281.32f, 219.63f)
                curveTo(284.75f, 213.32f, 292.19f, 204.6f, 316.29f, 178.62f)
                curveTo(330.48f, 163.32f, 332.93f, 158.31f, 330.39f, 149.81f)
                curveTo(328.15f, 142.32f, 320.2f, 131.27f, 315.31f, 128.85f)
                curveTo(312.06f, 127.25f, 309.91f, 127.26f, 305.5f, 128.88f)
                curveTo(297.03f, 131.99f, 293.29f, 130.24f, 288.18f, 120.79f)
                curveTo(284.04f, 113.12f, 280.23f, 109.36f, 275.13f, 107.89f)
                curveTo(271.07f, 106.72f, 265.47f, 106.69f, 261.16f, 107.82f)
                close()
            }
        }
    }.build()
}