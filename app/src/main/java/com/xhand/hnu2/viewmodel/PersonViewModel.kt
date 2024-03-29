package com.xhand.hnu2.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val route: String?
)
class PersonViewModel : ViewModel() {
    var hasMessage by mutableStateOf(true)
    var checkboxes = mutableStateListOf(
        ToggleableInfo(
            isChecked = true,
            text = "今日课程",
            imageVector = Icons.Default.DateRange,
            route = "schedule_screen"
        ),
        ToggleableInfo(
            isChecked = true,
            text = "课程成绩",
            imageVector = Icons.Default.Edit,
            route = "grade_screen"
        )
    )

}