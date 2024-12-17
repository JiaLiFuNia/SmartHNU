package com.smart.htu.utils

import android.content.Context
import android.widget.Toast

fun sendToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}