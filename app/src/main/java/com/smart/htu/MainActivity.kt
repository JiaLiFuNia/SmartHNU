package com.smart.htu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smart.htu.App.Companion.context
import com.smart.htu.screens.AppNavHost
import com.smart.htu.screens.application.courseTable.CourseTableViewModel
import com.smart.htu.utils.Calendar.createCalendar
import com.smart.htu.utils.Permission.Companion.checkRequestCalendarPermissions
import dagger.hilt.android.AndroidEntryPoint
import top.yukonga.miuix.kmp.basic.SnackbarHostState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        lateinit var snackBarHostState: SnackbarHostState
    }

    private val courseTableViewModel: CourseTableViewModel by viewModels()
    private lateinit var calendarPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            snackBarHostState = remember { SnackbarHostState() }
            AppNavHost()
        }

        calendarPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                createCalendar(context)
                courseTableViewModel.setIsWriteCalendarPermissionGranted(true)
            }
        }
    }

    fun requestCalendarPermissions() {
        checkRequestCalendarPermissions(this, calendarPermissionLauncher)
    }
}


