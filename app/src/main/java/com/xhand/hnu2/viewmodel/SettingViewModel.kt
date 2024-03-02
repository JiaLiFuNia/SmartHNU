package com.xhand.hnu2.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import com.xhand.hnu2.R
import com.xhand.hnu2.model.entity.Update
import com.xhand.hnu2.network.UpdateService
import retrofit2.Response


class SettingsViewModel : ViewModel() {

    val ifUpdate: Boolean = true

    @SuppressLint("StaticFieldLeak")
    fun copyText(cbManager: ClipboardManager, context: Context) {
        cbManager.setText(AnnotatedString(context.getString(R.string.qq_group_number)))
    }

    private val updateService = UpdateService.instance()
    suspend fun updateRes(): Update {
        return updateService.update()
    }
}