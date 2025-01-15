package com.smart.htu.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.DataStoreRepo.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.entity.SingleCourseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppUiState(
    val toDayCourseList: List<SingleCourseEntity> = emptyList(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val username: String = DEFAULT_USERNAME,
    val isLogSuccess: Boolean = false,
    val appListIsCommonList: List<SmallCardContent> = INIT_COMMON_APP_LIST
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

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_BLUR_EFFECT
        )

    private val _username = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_USERNAME
        )

    private val _loginState = dataStoreRepo.observeLoginState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private val _appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        INIT_COMMON_APP_LIST
    )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(toDayCourseList = toDayCourseList)
            }
        }
        viewModelScope.launch {
            _username.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            _loginState.collect { value ->
                _uiState.update {
                    it.copy(isLogSuccess = value == 1)
                }
            }
        }
        viewModelScope.launch {
            _appListIsCommonListStateFlow.collect { value ->
                _uiState.update { it.copy(appListIsCommonList = value) }
            }
        }
    }

}