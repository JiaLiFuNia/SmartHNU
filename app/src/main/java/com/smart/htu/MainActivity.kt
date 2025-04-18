package com.smart.htu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smart.htu.screens.NavHostScreen
import com.smart.htu.ui.theme.SmartHNUTheme
import dagger.hilt.android.AndroidEntryPoint
import top.yukonga.miuix.kmp.theme.MiuixTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() 

    companion object {
        lateinit var snackBarHostState: SnackbarHostState
    }

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            snackBarHostState = remember { SnackbarHostState() }

            SmartHNUTheme {
                Surface(
                    color = MiuixTheme.colorScheme.surface
                ) {
                    NavHostScreen()
                }
            }
        }
    }
}