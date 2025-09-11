package com.smart.htu.screens.application.airCondition

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.ACCookie
import com.smart.htu.api.module.AreaData
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuyRecords
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.NetworkRepo
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

data class AirConditionUiState(
    val blurEffect: Boolean = true,
    val remoteLoginCookie: ACCookie? = ACCookie("", ""),
    val userLoginCookie: ACCookie? = ACCookie("", ""),
    val customConfig: AreaData? = null,
    val buildingCode: String = "",
    val roomCode: String = "",
    val cookieType: Int = 0,
    val billData: BillDetail? = null,
    val billRecords: BillRecords? = null,
    val buyRecords: BuyRecords? = null,
    val isCheckingConfig: Boolean = false,
    val isCookieValid: Boolean = true
)

// 西
@HiltViewModel
class AirConditionViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val appNetworkRepo: AppNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(AirConditionUiState())
    val uiState: StateFlow<AirConditionUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val buildingIdStateFlow = dataStoreRepo.observeBuildingId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBuildingId().first()
            }
        )

    private val roomIdStateFlow = dataStoreRepo.observeRoomId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeRoomId().first()
            }
        )

    private val cookieTypeStateFlow = dataStoreRepo.observeAirConditionCookieType()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAirConditionCookieType().first()
            }
        )

    private val userCookieStateFlow = dataStoreRepo.observeAirConditionUserCookie()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                ACCookie("", "")
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            buildingIdStateFlow.collect { value ->
                _uiState.update { it.copy(buildingCode = value) }
            }
        }
        viewModelScope.launch {
            roomIdStateFlow.collect { value ->
                _uiState.update { it.copy(roomCode = value) }
            }
        }
        viewModelScope.launch {
            cookieTypeStateFlow.collect { value ->
                _uiState.update { it.copy(cookieType = value) }
            }
        }
        viewModelScope.launch {
            userCookieStateFlow.collect { value ->
                _uiState.update { it.copy(userLoginCookie = value, remoteLoginCookie = value) }
            }
        }
        viewModelScope.launch {
            getRemoteLoginCookie()
            getAirConditionConfig(onSuccess = {}, onFailure = {})
            getCurrentBillData()
        }
    }

    suspend fun getRemoteLoginCookie() {
        appNetworkRepo.configService()
            .onSuccess { res ->
                _uiState.update { it.copy(remoteLoginCookie = res.acCookieValue) }
            }
    }

    // 刷新配置
    suspend fun refreshConfig() {
        Log.i("TAG666 airCookie", getCookieByType().toString())
        if (getCookieByType() != ACCookie()) {
            if (_uiState.value.buildingCode.isNotEmpty() && _uiState.value.roomCode.isNotEmpty()) {
                getCurrentBillData()
                getBillRecords()
                getBuyRecords()
            }
        }
    }

    // 用于获取 areaId 验证cookie
    suspend fun getAirConditionConfig(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val cookie = getCookieByType()
        networkRepo.getAirConditionAreaService("shiroJID=${cookie.shiroJID}", cookie.ymId)
            .onSuccess { res ->
                _uiState.update { it.copy(customConfig = res.rows?.first()) }
                changeCookieValidState(true)
                onSuccess()
            }
            .onFailure { res ->
                changeCookieValidState(false)
                onFailure()
            }
    }

    // 当前电量
    suspend fun getCurrentBillData() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        networkRepo.getAirConditionCurrentBillDataService(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        ).onSuccess { res ->
            _uiState.update { it.copy(billData = res) }
        }.onFailure {
            changeCookieValidState(false)
        }
    }

    // 用电记录
    suspend fun getBillRecords() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        networkRepo.getAirConditionBillRecordsService(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode,
            mdType = _uiState.value.billData?.data?.surplusList?.first()?.mdtype ?: ""
        ).onSuccess { res ->
            _uiState.update { it.copy(billRecords = res) }
        }
    }

    // 充值记录
    suspend fun getBuyRecords() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        networkRepo.getAirConditionBuyRecordsService(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        ).onSuccess { res ->
            _uiState.update { it.copy(buyRecords = res) }
        }
    }

    // 自定义
    fun changeUserLoginCookie(shiroJID: String, ymId: String) {
        viewModelScope.launch {
            dataStoreRepo.saveAirConditionUserCookie(
                ACCookie(
                    shiroJID = shiroJID,
                    ymId = ymId
                )
            )
        }
    }

    fun saveACConfig(buildingId: String, roomId: String, shiroJID: String, ymId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingConfig = true) }
            dataStoreRepo.changeBuildingId(buildingId)
            dataStoreRepo.changeRoomId(roomId)
            changeUserLoginCookie(shiroJID, ymId)
            getRemoteLoginCookie()
            getAirConditionConfig(
                onSuccess = {
                    showSnackBar("配置成功")
                },
                onFailure = {
                    showSnackBar("Cookie 无效，请重新填写")
                }
            )
            _uiState.update { it.copy(isCheckingConfig = false) }
        }
    }

    fun changeLoginCookieType(type: Int) {
        viewModelScope.launch {
            dataStoreRepo.saveAirConditionCookieType(type)
        }
    }

    private fun changeCookieValidState(state: Boolean) {
        _uiState.update { it.copy(isCookieValid = state) }
    }

    fun showSnackBar(message: String, actionLabel: String? = null) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message, actionLabel)
        }
    }

    fun getCookieByType(): ACCookie {
        return when (_uiState.value.cookieType) {
            0 -> _uiState.value.remoteLoginCookie ?: ACCookie("", "")
            1 -> _uiState.value.userLoginCookie ?: ACCookie("", "")
            else -> ACCookie("", "")
        }
    }
}