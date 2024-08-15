package com.xhand.hnu.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel


data class ToggleableInfo(
    var isChecked: Boolean,
    val text: String,
    val imageVector: ImageVector,
    val route: String?
)

data class FunctionCard(
    var title: String,
    val painterResource: Int,
    val route: String
)

class PersonViewModel : ViewModel()