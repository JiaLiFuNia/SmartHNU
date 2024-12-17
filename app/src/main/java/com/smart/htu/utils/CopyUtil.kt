package com.smart.htu.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.smart.htu.App.Companion.context

@SuppressLint("ServiceCast")
fun copyContent(textContent: String) {
    val clip = ClipData.newPlainText("label", textContent)
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(clip)
}