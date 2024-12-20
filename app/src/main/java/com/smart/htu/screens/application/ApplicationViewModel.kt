package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.R
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.navigation.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ApplicationUiState(
    val appList: List<SmallCardContent>,
    val appListIsCommonList: List<SmallCardContent>
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val initAllAppList = listOf(
        SmallCardContent(
            icon = R.drawable.today_24px,
            description = "没有课程",
            label = R.string.today_course,
            route = ""
        ),
        SmallCardContent(
            label = R.string.dorm_air_conditioner,
            icon = R.drawable.bolt_24px,
            description = "电费剩余 00 度",
            url = "https://houqin.htu.edu.cn/one/plan/"
        ),
        SmallCardContent(
            label = R.string.classroom_search,
            icon = R.drawable.apartment_24px,
            route = Destinations.ClassroomSearch.route
        ),
        SmallCardContent(
            label = R.string.book_search,
            icon = R.drawable.book_4_24px,
            route = ""
        ),
        SmallCardContent(
            icon = R.drawable.finance_24px,
            label = R.string.course_grade,
            route = ""
        ),
        SmallCardContent(
            icon = R.drawable.near_me_24px,
            label = R.string.live_service,
            route = ""
        ),
        SmallCardContent(
            icon = R.drawable.app_registration_24px,
            label = R.string.common_applications,
            route = ""
        )
    )

    private val _uiState = MutableStateFlow(
        ApplicationUiState(
            appList = initAllAppList,
            appListIsCommonList = INIT_COMMON_APP_LIST
        )
    )
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    private val appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        INIT_COMMON_APP_LIST
    )

    init {
        viewModelScope.launch {
            appListIsCommonListStateFlow.collect { value ->
                _uiState.update { it.copy(appListIsCommonList = value) }
            }
        }
    }

    fun changeCommonAppListState(index: Int, add: Boolean = true) {
        viewModelScope.launch {
            val currentListState = _uiState.value.appListIsCommonList.toMutableList()
            if (add)
                currentListState.apply {
                    add(_uiState.value.appList[index])
                }
            else
                currentListState.apply {
                    removeAt(index)
                }
            dataStoreRepo.saveSmallCard(currentListState)
            _uiState.update { it.copy(appListIsCommonList = currentListState) }
        }
    }
}