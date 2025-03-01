package com.smart.htu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.screens.NavHostScreen
import com.smart.htu.ui.theme.SmartHNUTheme
import dagger.hilt.android.AndroidEntryPoint
import top.yukonga.miuix.kmp.theme.MiuixTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeRepository: DataStoreRepo

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

            val themeMode by themeRepository.observeThemeMode()
                .collectAsState(initial = 0)

            SmartHNUTheme {
                Surface(
                    color = when (themeMode) {
                        0 -> MiuixTheme.colorScheme.surface
                        else -> MaterialTheme.colorScheme.surface
                    }
                ) {
                    NavHostScreen()
                }
            }
        }
    }
}