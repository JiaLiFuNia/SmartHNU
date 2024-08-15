package com.xhand.hnu.components

import android.annotation.SuppressLint
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

// 复制内容到剪切板
@SuppressLint("StaticFieldLeak")
fun copyText(cbManager: ClipboardManager, text: String) {
    cbManager.setText(AnnotatedString(text))
}