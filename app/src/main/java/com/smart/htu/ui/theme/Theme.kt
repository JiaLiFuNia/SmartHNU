package com.smart.htu.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

val LocalColorMode = compositionLocalOf { 0 }

@Composable
fun SmartHNUTheme(
    colorMode: Int = 0,
    keyColor: Color? = null,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val darkTheme = when (colorMode) {
        2, 5 -> true
        1, 4 -> false
        else -> isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.isNavigationBarContrastEnforced = false
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val controller = remember(colorMode, keyColor, isDark) {
        when (colorMode) {
            1 -> ThemeController(ColorSchemeMode.Light)
            2 -> ThemeController(ColorSchemeMode.Dark)
            3 -> ThemeController(ColorSchemeMode.MonetSystem, keyColor = keyColor, isDark = isDark)
            4 -> ThemeController(ColorSchemeMode.MonetLight, keyColor = keyColor)
            5 -> ThemeController(ColorSchemeMode.MonetDark, keyColor = keyColor)
            else -> ThemeController(ColorSchemeMode.System)
        }
    }
    CompositionLocalProvider(
        LocalColorMode provides colorMode,
    ) {
        MiuixTheme(
            controller = controller,
            content = content
        )
    }
}

@Composable
fun isInDarkTheme(): Boolean = when (LocalColorMode.current) {
    1, 4 -> false
    2, 5, 6 -> true
    else -> isSystemInDarkTheme()
}