package com.xhand.hnu2.viewmodel

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu2.model.entity.GradeEntity
import com.xhand.hnu2.model.entity.GradePost
import com.xhand.hnu2.model.entity.KccjList
import com.xhand.hnu2.network.GradeService
import kotlinx.coroutines.launch

data class ToggleableInfo(
    var isChecked: Boolean,
    val text: String,
    val imageVector: ImageVector,
    val route: String?
)


class PersonViewModel() : ViewModel() {
    var hasMessage by mutableStateOf(true)
    var checkboxes by mutableStateOf(
        listOf(
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
            ),
        )
    )
}