package com.smart.htu.component.imageVectors

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
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun appLogo(): ImageVector {
    return Builder(
        name = "appLogo",
        defaultWidth = 512.0.dp,
        defaultHeight = 512.0.dp,
        viewportWidth = 512.0f,
        viewportHeight = 512.0f
    ).apply {
        path(
            fill = SolidColor(MiuixTheme.colorScheme.surfaceContainer),
            stroke = null,
            strokeLineWidth = 0.0f,
            strokeLineCap = Butt,
            strokeLineJoin = Miter,
            strokeLineMiter = 4.0f,
            pathFillType = NonZero
        ) {
            moveTo(458.63f, 53.5f)
            curveTo(410.33f, 5.38f, 340.88f, 0.0f, 256.0f, 0.0f)
            curveTo(171.02f, 0.0f, 101.46f, 5.44f, 53.17f, 53.69f)
            curveTo(4.9f, 101.92f, 0.0f, 171.36f, 0.0f, 256.25f)
            curveTo(0.0f, 341.15f, 4.9f, 410.62f, 53.19f, 458.86f)
            curveTo(101.47f, 507.12f, 171.03f, 512.0f, 256.0f, 512.0f)
            curveTo(340.97f, 512.0f, 410.51f, 507.12f, 458.8f, 458.86f)
            curveTo(507.09f, 410.61f, 512.0f, 341.15f, 512.0f, 256.25f)
            curveTo(512.0f, 171.26f, 507.04f, 101.73f, 458.63f, 53.5f)
            close()
        }
        path(
            fill = linearGradient(
                0.0f to Color(0xFF3ED1A5), 1.0f to Color(0xFF28ABC5), start =
                    Offset(256.5f, 107.0f), end = Offset(256.5f, 404.0f)
            ), stroke = null,
            strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
            strokeLineMiter = 4.0f, pathFillType = NonZero
        ) {
            moveTo(261.16f, 107.82f)
            curveTo(259.44f, 108.27f, 255.23f, 110.27f, 251.82f, 112.26f)
            curveTo(241.66f, 118.2f, 240.01f, 118.43f, 232.93f, 114.97f)
            curveTo(224.4f, 110.8f, 215.82f, 109.71f, 207.69f, 111.76f)
            curveTo(198.88f, 113.98f, 194.59f, 117.52f, 186.78f, 128.99f)
            curveTo(183.72f, 133.49f, 180.09f, 138.07f, 178.71f, 139.18f)
            curveTo(175.22f, 141.96f, 170.71f, 143.45f, 161.95f, 144.69f)
            curveTo(149.43f, 146.48f, 144.64f, 148.32f, 138.58f, 153.69f)
            curveTo(134.9f, 156.96f, 129.7f, 164.52f, 127.76f, 169.44f)
            curveTo(125.8f, 174.43f, 125.75f, 180.51f, 127.64f, 184.59f)
            curveTo(129.93f, 189.52f, 132.26f, 191.96f, 140.64f, 198.14f)
            curveTo(144.99f, 201.35f, 150.57f, 206.14f, 153.03f, 208.79f)
            curveTo(175.87f, 233.32f, 187.14f, 271.55f, 181.5f, 305.33f)
            curveTo(180.65f, 310.44f, 188.37f, 293.7f, 192.12f, 282.3f)
            curveTo(197.54f, 265.86f, 198.9f, 257.3f, 198.85f, 239.99f)
            curveTo(198.8f, 226.81f, 198.63f, 224.69f, 197.02f, 217.29f)
            curveTo(193.59f, 201.56f, 188.21f, 188.6f, 178.57f, 172.83f)
            curveTo(173.7f, 164.86f, 173.24f, 163.76f, 174.76f, 163.76f)
            curveTo(175.95f, 163.76f, 176.86f, 164.87f, 181.9f, 172.49f)
            curveTo(190.85f, 186.0f, 198.35f, 202.7f, 201.56f, 216.27f)
            curveTo(204.6f, 229.13f, 205.49f, 249.35f, 203.58f, 262.36f)
            curveTo(202.41f, 270.31f, 199.26f, 283.28f, 196.93f, 289.73f)
            curveTo(195.99f, 292.33f, 195.33f, 294.58f, 195.47f, 294.71f)
            curveTo(196.54f, 295.75f, 205.94f, 279.11f, 211.17f, 266.91f)
            curveTo(214.98f, 258.02f, 220.11f, 242.38f, 222.23f, 233.23f)
            curveTo(225.55f, 218.9f, 226.66f, 209.66f, 227.06f, 193.13f)
            curveTo(227.59f, 171.36f, 225.98f, 157.6f, 221.15f, 142.61f)
            curveTo(219.04f, 136.05f, 218.98f, 134.86f, 220.79f, 135.53f)
            curveTo(222.8f, 136.28f, 227.55f, 151.78f, 229.53f, 164.09f)
            curveTo(235.05f, 198.47f, 230.29f, 237.04f, 216.31f, 271.25f)
            curveTo(214.33f, 276.09f, 212.97f, 280.04f, 213.29f, 280.04f)
            curveTo(214.7f, 280.04f, 231.94f, 256.48f, 239.79f, 243.81f)
            curveTo(244.43f, 236.34f, 253.32f, 218.49f, 256.23f, 210.84f)
            curveTo(263.67f, 191.23f, 267.01f, 173.43f, 267.77f, 149.39f)
            curveTo(268.08f, 139.41f, 268.39f, 135.99f, 269.01f, 135.99f)
            curveTo(270.45f, 135.99f, 270.64f, 137.76f, 270.64f, 151.38f)
            curveTo(270.62f, 177.26f, 266.67f, 198.11f, 257.5f, 220.57f)
            curveTo(253.74f, 229.79f, 246.16f, 244.49f, 240.4f, 253.72f)
            lineTo(236.07f, 260.67f)
            lineTo(239.17f, 258.14f)
            curveTo(240.88f, 256.75f, 246.46f, 251.95f, 251.58f, 247.48f)
            curveTo(256.7f, 243.01f, 261.4f, 239.34f, 262.02f, 239.34f)
            curveTo(262.64f, 239.34f, 263.36f, 239.85f, 263.6f, 240.46f)
            curveTo(264.16f, 241.88f, 258.41f, 247.3f, 218.46f, 283.04f)
            curveTo(170.21f, 326.22f, 146.91f, 348.32f, 129.37f, 367.57f)
            curveTo(117.87f, 380.19f, 108.96f, 391.94f, 108.14f, 395.57f)
            curveTo(107.28f, 399.34f, 110.53f, 403.1f, 115.29f, 403.85f)
            curveTo(119.48f, 404.5f, 121.44f, 403.2f, 125.09f, 397.36f)
            curveTo(137.62f, 377.29f, 160.22f, 351.61f, 209.18f, 301.82f)
            curveTo(224.68f, 286.05f, 225.07f, 285.71f, 234.93f, 279.46f)
            curveTo(274.22f, 254.55f, 308.95f, 242.2f, 357.75f, 235.8f)
            curveTo(372.19f, 233.9f, 374.1f, 233.82f, 374.1f, 235.1f)
            curveTo(374.1f, 236.3f, 373.45f, 236.47f, 362.75f, 238.04f)
            curveTo(327.54f, 243.22f, 299.58f, 251.43f, 273.64f, 264.19f)
            curveTo(261.6f, 270.11f, 246.97f, 278.69f, 236.67f, 285.87f)
            lineTo(229.74f, 290.7f)
            lineTo(240.34f, 286.71f)
            curveTo(253.21f, 281.86f, 265.94f, 278.09f, 277.98f, 275.55f)
            curveTo(308.35f, 269.15f, 342.48f, 268.78f, 365.42f, 274.61f)
            curveTo(372.32f, 276.36f, 376.21f, 277.97f, 376.62f, 279.23f)
            curveTo(376.99f, 280.39f, 375.51f, 280.24f, 369.23f, 278.48f)
            curveTo(358.77f, 275.55f, 349.83f, 274.61f, 332.38f, 274.61f)
            curveTo(316.1f, 274.62f, 309.58f, 275.12f, 294.67f, 277.52f)
            curveTo(267.13f, 281.95f, 238.26f, 292.49f, 213.72f, 307.09f)
            lineTo(206.52f, 311.37f)
            lineTo(215.39f, 308.61f)
            curveTo(236.92f, 301.91f, 251.2f, 299.75f, 270.91f, 300.24f)
            curveTo(284.71f, 300.58f, 290.8f, 301.35f, 302.88f, 304.29f)
            curveTo(315.49f, 307.36f, 330.48f, 313.57f, 343.39f, 321.08f)
            curveTo(351.01f, 325.5f, 351.96f, 326.27f, 350.94f, 327.26f)
            curveTo(350.4f, 327.78f, 347.72f, 326.66f, 340.95f, 323.07f)
            curveTo(315.95f, 309.83f, 293.7f, 304.58f, 266.3f, 305.45f)
            curveTo(248.98f, 306.01f, 231.85f, 309.63f, 214.09f, 316.5f)
            curveTo(206.92f, 319.27f, 196.27f, 324.3f, 198.89f, 323.69f)
            curveTo(202.47f, 322.84f, 212.3f, 322.03f, 218.9f, 322.03f)
            curveTo(238.39f, 322.05f, 256.68f, 326.25f, 274.98f, 334.91f)
            curveTo(290.75f, 342.37f, 301.38f, 350.67f, 311.02f, 363.03f)
            curveTo(319.96f, 374.48f, 323.3f, 376.61f, 332.38f, 376.61f)
            curveTo(338.14f, 376.61f, 338.63f, 376.5f, 344.24f, 373.79f)
            curveTo(351.94f, 370.09f, 359.03f, 363.79f, 361.93f, 358.08f)
            curveTo(363.12f, 355.75f, 364.47f, 351.52f, 365.1f, 348.19f)
            curveTo(365.7f, 344.99f, 366.65f, 340.05f, 367.2f, 337.21f)
            curveTo(367.75f, 334.37f, 368.97f, 330.66f, 369.92f, 328.96f)
            curveTo(371.96f, 325.34f, 374.38f, 323.35f, 384.97f, 316.62f)
            curveTo(393.53f, 311.17f, 396.4f, 308.16f, 398.86f, 302.0f)
            curveTo(402.6f, 292.7f, 401.96f, 283.63f, 396.87f, 273.59f)
            curveTo(395.36f, 270.6f, 394.14f, 267.7f, 394.15f, 267.15f)
            curveTo(394.21f, 265.12f, 396.09f, 261.08f, 399.4f, 255.86f)
            curveTo(404.02f, 248.56f, 404.96f, 245.83f, 405.0f, 239.67f)
            curveTo(405.05f, 230.17f, 401.15f, 224.91f, 390.36f, 219.93f)
            curveTo(381.0f, 215.6f, 379.45f, 212.37f, 382.48f, 203.55f)
            curveTo(384.33f, 198.17f, 384.33f, 198.11f, 383.18f, 195.18f)
            curveTo(381.75f, 191.57f, 377.38f, 187.62f, 370.25f, 183.5f)
            curveTo(360.63f, 177.94f, 353.78f, 177.27f, 346.29f, 181.15f)
            curveTo(343.98f, 182.35f, 337.23f, 187.68f, 329.71f, 194.23f)
            curveTo(293.94f, 225.44f, 284.78f, 231.71f, 280.7f, 227.76f)
            curveTo(278.81f, 225.93f, 278.96f, 223.98f, 281.33f, 219.63f)
            curveTo(284.75f, 213.33f, 292.19f, 204.6f, 316.29f, 178.62f)
            curveTo(330.48f, 163.33f, 332.94f, 158.32f, 330.39f, 149.81f)
            curveTo(328.16f, 142.32f, 320.21f, 131.27f, 315.32f, 128.86f)
            curveTo(312.07f, 127.25f, 309.92f, 127.26f, 305.51f, 128.88f)
            curveTo(297.04f, 132.0f, 293.29f, 130.25f, 288.18f, 120.79f)
            curveTo(284.04f, 113.13f, 280.23f, 109.36f, 275.14f, 107.9f)
            curveTo(271.08f, 106.73f, 265.48f, 106.7f, 261.16f, 107.82f)
            close()
        }
    }
        .build()
}