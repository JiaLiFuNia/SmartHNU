package com.smart.htu.screens.application.physicalTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import javax.inject.Inject

// 总成绩：58.5（不及格）
//身高：174 CM  体重：73.5 KG     得分：80
//立定跳远：2.2 米   得分:6450米跑：8.2 秒   得分：68
//肺活量：4599 ML   得分：801000米跑：5.12 分   得分:30
//引体向上：1 个   得分：0
//坐位体前屈：21 CM   得分：85

@Serializable
data class PhysicalTestScore(
    val height: Int,
    val weight: Float,
    val vitalCapacity: Int,
    val fiftyMeterRun: Float,
    val sitAndReach: Float,
    val standingLongJump: Float,
    val enduranceRun: Float,
    val strengthExercise: Int
)

data class PhysicalTestUiState(
    val scoreList: Map<Int, PhysicalTestScore> = mapOf(),
    val grade: Int = 2022 // 表示为2022级
)

@HiltViewModel
class PhysicalTestViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(PhysicalTestUiState())
    val uiState: StateFlow<PhysicalTestUiState> = _uiState.asStateFlow()

    private val physicalScoreListCodeStateFlow = dataStoreRepo.observePhysicalTestScoreList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observePhysicalTestScoreList().first()
            }
        )

    init {
        viewModelScope.launch {
            physicalScoreListCodeStateFlow.collect { value ->
                _uiState.update {
                    it.copy(scoreList = value)
                }
            }
        }
    }

    fun savePhysicalTestScoreList(score: Map<Int, PhysicalTestScore>) {
        viewModelScope.launch {
            dataStoreRepo.savePhysicalTestScoreList(score)
        }
    }

    fun addPhysicalTestScore(grade: Int, score: PhysicalTestScore) {
        val scoreList = _uiState.value.scoreList.toMutableMap()
        scoreList[grade] = score
        savePhysicalTestScoreList(scoreList)
    }

    fun deletePhysicalTestScore(grade: Int) {
        val scoreList = _uiState.value.scoreList.toMutableMap()
        scoreList.remove(grade)
        savePhysicalTestScoreList(scoreList)
    }

}