package com.xhand.hnu.model

import android.content.Context
import android.content.Intent
import android.net.Uri

fun viewWebsite(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.data = Uri.parse(url)
    context.startActivity(intent)
}