package com.smart.htu.screens.navigation

import androidx.annotation.DrawableRes

data class BottomNavigationItem(
    var title: Int,
    @DrawableRes var selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val badge: Boolean? = false
)