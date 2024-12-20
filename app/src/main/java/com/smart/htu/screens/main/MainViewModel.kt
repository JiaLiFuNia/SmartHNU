package com.smart.htu.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.screens.main.entity.SingleCourseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppUiState(
    var toDayCourseList: List<SingleCourseEntity> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val toDayCourseList = listOf(
        SingleCourseEntity(
            "习近平新时代中国特色社会主义思想",
            "",
            "宋晓可",
            "启智楼304",
            "14:30-16:10",
            "考试"
        ),
        SingleCourseEntity(
            "习近平新时代中国特色社会主义思想",
            "",
            "宋晓可",
            "启智楼304",
            "14:30-16:10",
            "考试"
        )
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    toDayCourseList = toDayCourseList
                )
            }
        }
    }

}