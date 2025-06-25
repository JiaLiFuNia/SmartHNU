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
    val setCookieType: Int = 0,
    val billData: BillDetail? = null,
    val billRecords: BillRecords? = null,
    val buyRecords: BuyRecords? = null,
    val isLoadingBillRecords: Boolean = true,
    val isCookieValid: Boolean = false
)

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
                _uiState.update { it.copy(setCookieType = value) }
            }
        }
        viewModelScope.launch {
            userCookieStateFlow.collect { value ->
                _uiState.update { it.copy(userLoginCookie = value, remoteLoginCookie = value) }
            }
        }
        viewModelScope.launch {
            getRemoteLoginCookie()
            changeLoadingState(false)
        }
    }

    suspend fun getRemoteLoginCookie() {
        appNetworkRepo.configService()
            .onSuccess {
                setRemoteLoginCookie(it.acCookieValue)
            }
    }

    // 刷新配置
    suspend fun refreshConfig() {
        getRemoteLoginCookie()
        Log.i("TAG666 airCookie", getCookieByType().toString())
        if (getCookieByType() != ACCookie()) {
            getAirConditionConfig()
            if (_uiState.value.buildingCode.isNotEmpty() && _uiState.value.roomCode.isNotEmpty()) {
                getBillDetailService()
                getBillRecords()
                getBuyRecords()
            }
        }
    }

    // 用于获取 areaId
    suspend fun getAirConditionConfig() {
        val cookie = getCookieByType()
        val res = networkRepo.getAirConditionAreaService("shiroJID=${cookie.shiroJID}", cookie.ymId)
        res.onSuccess {
            _uiState.update { uiState ->
                uiState.copy(customConfig = it.rows?.first())
            }
            changeCookieValidState(true)
            showSnackBar("已配置有效 Cookie")
        }
        res.onFailure {
            changeCookieValidState(false)
            showSnackBar("请重新配置 Cookie")
        }
        Log.i("TAG666 air", res.getOrNull().toString())
    }

    // 当前电量
    suspend fun getBillDetailService() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val billData = networkRepo.getAirConditionBillService(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        )
        billData.onSuccess {
            _uiState.update { uiState ->
                uiState.copy(billData = it)
            }
        }
    }

    // 用电记录
    suspend fun getBillRecords() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val billRecords = networkRepo.getAirConditionBillRecords(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode,
            mdType = _uiState.value.billData?.data?.surplusList?.first()?.mdtype ?: ""
        )
        billRecords.onSuccess {
            _uiState.update { uiState ->
                uiState.copy(billRecords = it)
            }
        }
    }

    // 充值记录
    suspend fun getBuyRecords() {
        val cookie = getCookieByType()
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val buyRecords = networkRepo.getAirConditionBuyRecords(
            shiroJID = "shiroJID=${cookie.shiroJID}",
            ymId = cookie.ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        )
        buyRecords.onSuccess {
            _uiState.update { uiState ->
                uiState.copy(buyRecords = it)
            }
        }
    }

    private fun setRemoteLoginCookie(loginCookie: ACCookie) {
        viewModelScope.launch {
            _uiState.update { it.copy(remoteLoginCookie = loginCookie) }
            if (_uiState.value.setCookieType == 0)
                dataStoreRepo.saveAirConditionUserCookie(loginCookie)
        }
    }

    fun changeUserCookieSY(
        shiroJID: String = _uiState.value.userLoginCookie?.shiroJID ?: "",
        ymId: String = _uiState.value.userLoginCookie?.ymId ?: ""
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userLoginCookie = ACCookie(
                        shiroJID = shiroJID,
                        ymId = ymId
                    )
                )
            }
            dataStoreRepo.saveAirConditionUserCookie(_uiState.value.userLoginCookie!!)
        }
    }

    fun saveBuildingAndRoomId(buildingId: String, roomId: String) {
        viewModelScope.launch {
            dataStoreRepo.changeBuildingId(buildingId)
            dataStoreRepo.changeRoomId(roomId)
            refreshConfig()
            showSnackBar("配置保存成功")
        }
    }

    fun changeCookieType(type: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(setCookieType = type) }
            changeCookieValidState(false)
            dataStoreRepo.saveAirConditionCookieType(type)
        }
    }

    private fun changeLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoadingBillRecords = state) }
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
        return when (_uiState.value.setCookieType) {
            0 -> _uiState.value.remoteLoginCookie ?: ACCookie("", "")
            1 -> _uiState.value.userLoginCookie ?: ACCookie("", "")
            else -> ACCookie("", "")
        }
    }
}