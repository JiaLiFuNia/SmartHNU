package com.smart.htu.utils

import android.content.Intent
import android.net.Uri
import com.smart.htu.App.Companion.context

fun openInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.data = Uri.parse(url)
    context.startActivity(intent)
}