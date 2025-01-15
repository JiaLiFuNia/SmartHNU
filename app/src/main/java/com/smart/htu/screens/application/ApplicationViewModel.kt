package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.R
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.SHOWER_ALIPAY_URL
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
    val appListIsCommonList: List<SmallCardContent>,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val initAllAppList = listOf(
        SmallCardContent(
            guestEnable = false,
            icon = R.drawable.today_24px,
            description = "没有课程",
            label = R.string.today_course,
        ),
        SmallCardContent(
            guestEnable = false,
            label = R.string.dorm_air_conditioner,
            icon = R.drawable.bolt_24px,
            description = "电费剩余 00 度",
            route = Destinations.AirCondition.route
        ),
        SmallCardContent(
            guestEnable = false,
            label = R.string.classroom_search,
            icon = R.drawable.apartment_24px,
            route = Destinations.ClassroomSearch.route
        ),
        SmallCardContent(
            guestEnable = true,
            label = R.string.book_search,
            icon = R.drawable.book_4_24px,
            route = Destinations.LibrarySearch.route
        ),
        SmallCardContent(
            guestEnable = false,
            icon = R.drawable.finance_24px,
            label = R.string.course_grade,
            route = null
        ),
        SmallCardContent(
            guestEnable = true,
            icon = R.drawable.near_me_24px,
            label = R.string.live_service,
            route = null
        ),
        SmallCardContent(
            guestEnable = true,
            icon = R.drawable.bathtub_24px,
            description = "支付宝-卡博士",
            label = R.string.shower_water,
            appUrl = SHOWER_ALIPAY_URL
        ),
        SmallCardContent(
            guestEnable = true,
            icon = R.drawable.app_registration_24px,
            label = R.string.common_applications,
            route = null
        )
    )

    private val _uiState = MutableStateFlow(
        ApplicationUiState(
            appList = initAllAppList,
            appListIsCommonList = INIT_COMMON_APP_LIST
        )
    )
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    private val _appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        INIT_COMMON_APP_LIST
    )

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_BLUR_EFFECT
        )

    init {
        viewModelScope.launch {
            _appListIsCommonListStateFlow.collect { value ->
                _uiState.update { it.copy(appListIsCommonList = value) }
            }
        }
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
    }

    fun changeCommonAppListState(index: Int, add: Boolean = true) {
        viewModelScope.launch {
            val currentListState = _uiState.value.appListIsCommonList.toMutableList()
            if (add)
                currentListState.apply { add(_uiState.value.appList[index]) }
            else
                currentListState.apply { removeAt(index) }
            dataStoreRepo.saveSmallCard(currentListState)
            _uiState.update { it.copy(appListIsCommonList = currentListState) }
        }
    }
}