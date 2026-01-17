package com.smart.htu.screens.application.classroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseInfoEntity
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.TermUtil.getCurrentTerm
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
import javax.inject.Inject

data class ClassroomUiState(
    val buildingsOccupation: Map<Int, ClassroomOccupationEntity> = emptyMap(),
    val occupationDetail: List<CourseInfoEntity>? = null,
    val isLoading: Boolean = true,
    val token: String = DEFAULT_TOKEN,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val globalTermCode: String = getCurrentTerm(),
)

@HiltViewModel
class ClassroomSearchViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepo: SharedDataRepository
) : ViewModel() {

    val campusList = listOf("校本部", "平原湖校区", "科技创新港校区")
    val buildingsList = listOf(
        listOf(
            BuildingEntity("104", "启智楼", "校本部"),
            BuildingEntity("107", "新五五四楼", "校本部"),
            BuildingEntity("102", "文渊楼", "校本部"),
            BuildingEntity("310", "文昌楼（东综）", "校本部"),
            BuildingEntity("302", "求是西楼", "校本部"),
            BuildingEntity("307", "求是东楼", "校本部"),
            BuildingEntity("301", "求是中楼", "校本部"),
            BuildingEntity("119", "新联楼", "校本部"),
        ),
        listOf(
            BuildingEntity("201", "小店向真楼", "平原湖校区"),
            BuildingEntity("203", "小店向知楼", "平原湖校区"),
            BuildingEntity("202", "小店向心楼", "平原湖校区"),
        ),
        listOf(
            BuildingEntity("17162916", "数学楼", "科技创新港校区"),
            BuildingEntity("17162918", "电院楼", "科技创新港校区"),
            BuildingEntity("16035451", "生命科学学院北楼", "科技创新港校区"),
            BuildingEntity("17162917", "物理楼", "科技创新港校区"),
            BuildingEntity("16948749", "平原实验室", "科技创新港校区"),
        )
    )

    private val _uiState = MutableStateFlow(ClassroomUiState())
    val uiState: StateFlow<ClassroomUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    private val termCodeStateFlow = dataStoreRepo.observeGlobalTermCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeGlobalTermCode().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
        viewModelScope.launch {
            termCodeStateFlow.collect { value ->
                _uiState.update { it.copy(globalTermCode = value) }
                Log.i("TAG666", value)
            }
        }
        viewModelScope.launch {
            getClassroomOccupation(getCurrentDate())
        }
    }

    suspend fun getClassroomOccupation(date: String, campusIndex: Int = 0, buildingIndex: Int = 0) {
        try {
            changeLoadingState(true)
            val selectedBuildingList = buildingsList[campusIndex]
            selectedBuildingList[buildingIndex.takeIf { it < selectedBuildingList.size } ?: 0].let {
                jwcNetworkRepo.getClassroomOccupationService(
                    BuildingEntity(
                        buildingCode = it.buildingCode,
                        buildingName = it.buildingName,
                        date = date
                    )
                ).onSuccess { res ->
                    _uiState.update {
                        it.copy(buildingsOccupation = it.buildingsOccupation + (buildingIndex to res))
                    }
                    Log.i("TAG666", "getClassroomOccupation: $date")
                }.onFailure {
                    sharedDataRepo.setJWCLoginState(-2)
                }
            }
            changeLoadingState(false)
        } catch (e: Exception) {
            Log.i("TAG666", "getClassroomOccupation: $e")
        }
    }

    suspend fun getClassroomOccupationDetail(
        date: String,
        classroom: String,
        onResult: (String) -> Unit
    ) {
        val searchInfo = CourseSearchPostEntity(
            termCode = _uiState.value.globalTermCode,
            date = date,
            teachingVenueName = classroom
        )
        _uiState.update { it.copy(occupationDetail = null) }
        jwcNetworkRepo.searchCourseService(searchInfo)
            .onSuccess { res ->
                _uiState.update { it.copy(occupationDetail = res) }
            }
            .onFailure {
                onResult(it.message.toString())
            }
    }

    fun changeLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }
}