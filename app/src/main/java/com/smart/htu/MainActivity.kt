package com.smart.htu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smart.htu.screens.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val FOCUS_CHANNEL_ID = "focus_channel"
        const val FOCUS_CHANNEL_NAME = "任务通知"
        const val FOCUS_CHANNEL_DESC = "用于通知临近的任务"
        lateinit var notificationManager: NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(FOCUS_CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    FOCUS_CHANNEL_ID,
                    FOCUS_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = FOCUS_CHANNEL_DESC
                    setAllowBubbles(true)
                    setShowBadge(true)
                }
            )
        }

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            AppNavHost()
        }
    }

}
