package com.smart.htu.utils

import android.content.Context
import android.widget.Toast
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult

object ToastUtil {
    private var toast: Toast? = null

    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, text, duration).also {
            it.show()
        }
    }

    fun showLongToast(context: Context, text: String) {
        showToast(context, text, Toast.LENGTH_LONG)
    }

    // snackBar
    suspend fun showSnackbar(
        snackBarHostState: SnackbarHostState,
        message: String,
        actionLabel: String? = null,
        withDismissButton: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
        onConfirm: () -> Unit = {},
        onDismiss: () -> Unit = {}
    ) {
        val res = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            withDismissAction = withDismissButton
        )
        when (res) {
            SnackbarResult.ActionPerformed -> {
                onConfirm()
            }

            SnackbarResult.Dismissed -> {
                onDismiss()
            }
        }
    }
}