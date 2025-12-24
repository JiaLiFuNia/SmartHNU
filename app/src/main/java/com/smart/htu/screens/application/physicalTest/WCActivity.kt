package com.smart.htu.screens.application.physicalTest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.HttpUrl.Companion.toHttpUrl

@AndroidEntryPoint
class WCActivity : ComponentActivity() {

    private val viewModel: PhysicalTestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intent = intent
        Log.d("TAG666 MainActivity", "${intent.dataString}")
        intent.data?.let { uri ->
            val code = uri.toString().toHttpUrl().queryParameter("code")
            code?.let {
                viewModel.savePhysicalTestCode(it)
            }
        }

        setContent {
        }
    }

}