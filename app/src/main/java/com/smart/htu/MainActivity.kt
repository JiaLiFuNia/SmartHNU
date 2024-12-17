package com.smart.htu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.smart.htu.screens.NavHostScreen
import com.smart.htu.ui.theme.SmartHNUTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHNUTheme {
                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavHostScreen()
                }
            }
        }
    }
}