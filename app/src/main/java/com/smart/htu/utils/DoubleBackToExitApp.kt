package com.smart.htu.utils

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch

@Composable
fun DoubleBackToExitApp(
    onExit: () -> Unit,
    context: Context
) {
    var shouldExit by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (shouldExit) {
            onExit()
        } else {
            shouldExit = true
            coroutineScope.launch {
                showToast(context, "再一次操作退出应用")
                kotlinx.coroutines.delay(2000)
                shouldExit = false
            }
        }
    }
}