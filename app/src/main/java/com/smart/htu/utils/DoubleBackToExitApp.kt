package com.smart.htu.utils

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.smart.htu.App.Companion.context
import com.smart.htu.MainActivity
import kotlinx.coroutines.launch

@Composable
fun DoubleBackToExitApp(
    onExit: () -> Unit
) {
    var shouldExit by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (shouldExit) {
            onExit()
        } else {
            shouldExit = true
            coroutineScope.launch {
                MainActivity.snackBarHostState.showSnackbar(
                    "再一次操作退出应用",
                    duration = SnackbarDuration.Short
                )
                kotlinx.coroutines.delay(2000)
                shouldExit = false
            }
        }
    }
}