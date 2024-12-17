package com.smart.htu.screens.application.classroom

import androidx.lifecycle.ViewModel
import com.smart.htu.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ClassroomUiState(
    val buildingsList: List<ClassroomNameEntity>
)

@HiltViewModel
class ClassroomSearchViewModel @Inject constructor() : ViewModel() {

    private val buildingsList = listOf(
        ClassroomNameEntity("104", "启智楼"),
        ClassroomNameEntity("107", "新五五四楼"),
        ClassroomNameEntity("102", "文渊楼"),
        ClassroomNameEntity("310", "文昌楼（东综）")
    )
    val haveCourseTime = listOf(
        R.string.period_1_2,
        R.string.period_3_4,
        R.string.period_5_6,
        R.string.period_7_8,
        R.string.period_9_10,
    )
    val roomList = List(8) {
        FreeRoomEntity(1, "", "启智楼10${it + 1}")
    } + List(8) {
        FreeRoomEntity(2, "", "启智楼20${it + 1}")
    } + List(8) {
        FreeRoomEntity(3, "", "启智楼30${it + 1}")
    } + List(8) {
        FreeRoomEntity(4, "", "启智楼40${it + 1}")
    } + List(8) {
        FreeRoomEntity(5, "", "启智楼50${it + 1}")
    }
    private val _uiState = MutableStateFlow(
        ClassroomUiState(
            buildingsList = buildingsList
        )
    )
    val uiState: StateFlow<ClassroomUiState> = _uiState.asStateFlow()

}