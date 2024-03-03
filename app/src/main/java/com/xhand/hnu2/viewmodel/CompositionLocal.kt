package com.xhand.hnu2.viewmodel

import androidx.compose.runtime.compositionLocalOf

val LocalUserViewModel = compositionLocalOf<SettingsViewModel> { error("not found") }
val LocalNewsViewModel = compositionLocalOf<NewsViewModel> { error("not found") }