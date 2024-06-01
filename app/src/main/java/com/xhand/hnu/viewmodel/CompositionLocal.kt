package com.xhand.hnu.viewmodel

import androidx.compose.runtime.compositionLocalOf

val LocalUserViewModel = compositionLocalOf<SettingsViewModel> { error("not found") }
val LocalNewsViewModel = compositionLocalOf<NewsViewModel> { error("not found") }
val LocalPersonViewModel = compositionLocalOf<PersonViewModel> { error("not found") }
val LocalGradeViewModel = compositionLocalOf<GradeViewModel> { error("not found") }