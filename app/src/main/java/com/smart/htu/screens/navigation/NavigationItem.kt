package com.smart.htu.screens.navigation

import androidx.annotation.DrawableRes

data class BottomNavigationItem(
    val enabled: Boolean = true,
    val title: Int,
    @DrawableRes var selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val badge: Int = 0
)