package com.smart.htu.utils

import android.content.Context
import android.widget.Toast

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
}