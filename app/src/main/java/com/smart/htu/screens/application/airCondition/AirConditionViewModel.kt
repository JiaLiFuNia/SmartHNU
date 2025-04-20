package com.smart.htu.screens.application.airCondition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.MainActivity
import com.smart.htu.api.module.AreaData
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuyRecords
import com.smart.htu.api.module.GiteeEntity
import com.smart.htu.api.module.LoginCookie
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
    val remoteLoginCookie: LoginCookie? = LoginCookie(shiroJID = "", ymId = ""),
    val userLoginCookie: LoginCookie? = LoginCookie("", ""),
    val customConfig: AreaData? = null,
    val buildingCode: String = "",
    val roomCode: String = "",
    val setCookieType: Int = 0,
    val billData: BillDetail? = null,
    val config: GiteeEntity? = null,
    val billRecords: BillRecords? = null,
    val buyRecords: BuyRecords? = null,
    val isLoadingBillRecords: Boolean = true,
    val isCookieValid: Boolean = false
)

@HiltViewModel
class AirConditionViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(AirConditionUiState())
    val uiState: StateFlow<AirConditionUiState> = _uiState.asStateFlow()

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val _buildingIdStateFlow = dataStoreRepo.observeBuildingId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBuildingId().first()
            }
        )

    private val _roomIdStateFlow = dataStoreRepo.observeRoomId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeRoomId().first()
            }
        )

    private val _cookieTypeStateFlow = dataStoreRepo.observeAirConditionCookieType()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAirConditionCookieType().first()
            }
        )

    private val _userCookieStateFlow = dataStoreRepo.observeAirConditionUserCookie()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LoginCookie("", "")
        )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            _buildingIdStateFlow.collect { value ->
                _uiState.update { it.copy(buildingCode = value) }
            }
        }
        viewModelScope.launch {
            _roomIdStateFlow.collect { value ->
                _uiState.update { it.copy(roomCode = value) }
            }
        }
        viewModelScope.launch {
            _cookieTypeStateFlow.collect { value ->
                _uiState.update { it.copy(setCookieType = value) }
            }
        }
        viewModelScope.launch {
            _userCookieStateFlow.collect { value ->
                _uiState.update { it.copy(userLoginCookie = value) }
            }
        }
        viewModelScope.launch {
            getGiteeConfigService()
            getAirConditionConfig()
            if (_uiState.value.buildingCode.isNotEmpty() && _uiState.value.roomCode.isNotEmpty()) {
                getBillDetailService()
                getBillRecords()
                getBuyRecords()
            }
            changeLoadingState(false)
        }
    }

    private suspend fun getGiteeConfigService() {
        val res = networkRepo.getGiteeConfig()
        res.onSuccess {
            changeRemoteLoginCookie(res.getOrNull()?.airConditionCookie ?: LoginCookie("", ""))
        }
    }

    // 用于获取 areaId
    suspend fun getAirConditionConfig() {
        val (shiroJID, ymId) = when (_uiState.value.setCookieType) {
            0 -> _uiState.value.remoteLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")

            1 -> _uiState.value.userLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")
            else -> ("" to "")
        }
        val res = networkRepo.getAirConditionAreaService("shiroJID=$shiroJID", ymId)
        res.onSuccess {
            _uiState.update { it.copy(customConfig = res.getOrNull()?.rows?.first()) }
            changeCookieValidState(true)
            MainActivity.snackBarHostState.showSnackbar("已配置有效 Cookie")
        }
        res.onFailure {
            changeCookieValidState(false)
            MainActivity.snackBarHostState.showSnackbar("请重新配置 Cookie")
        }
        Log.i("TAG666 air", res.getOrNull().toString())
    }

    suspend fun getBillDetailService() {
        val (shiroJID, ymId) = when (_uiState.value.setCookieType) {
            0 -> _uiState.value.remoteLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")

            1 -> _uiState.value.userLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")
            else -> ("" to "")
        }
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val billData = networkRepo.getAirConditionBillService(
            shiroJID = "shiroJID=$shiroJID",
            ymId = ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        )
        billData.onSuccess {
            _uiState.update { it.copy(billData = billData.getOrNull()) }
        }
    }

    suspend fun getBillRecords() {
        val (shiroJID, ymId) = when (_uiState.value.setCookieType) {
            0 -> _uiState.value.remoteLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")

            1 -> _uiState.value.userLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")
            else -> ("" to "")
        }
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val billRecords = networkRepo.getAirConditionBillRecords(
            shiroJID = "shiroJID=$shiroJID",
            ymId = ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode,
            mdType = _uiState.value.billData?.data?.surplusList?.first()?.mdtype ?: ""
        )
        billRecords.onSuccess {
            _uiState.update { it.copy(billRecords = billRecords.getOrNull()) }
        }
    }

    suspend fun getBuyRecords() {
        val (shiroJID, ymId) = when (_uiState.value.setCookieType) {
            0 -> _uiState.value.remoteLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")

            1 -> _uiState.value.userLoginCookie?.let { it.shiroJID to it.ymId } ?: ("" to "")
            else -> ("" to "")
        }
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        val buyRecords = networkRepo.getAirConditionBuyRecords(
            shiroJID = "shiroJID=$shiroJID",
            ymId = ymId,
            areaId = _uiState.value.customConfig?.id ?: "",
            buildingCode = buildingCode,
            floorCode = floorCode,
            roomCode = roomCode
        )
        buyRecords.onSuccess {
            _uiState.update { it.copy(buyRecords = buyRecords.getOrNull()) }
        }
    }

    private fun changeRemoteLoginCookie(loginCookie: LoginCookie) {
        _uiState.update { it.copy(remoteLoginCookie = loginCookie) }
    }

    fun changeUserShiroJid(text: String) {
        _uiState.update {
            it.copy(
                userLoginCookie = LoginCookie(
                    shiroJID = text,
                    ymId = _uiState.value.userLoginCookie?.ymId ?: ""
                )
            )
        }
    }

    fun changeUserYmId(text: String) {
        _uiState.update {
            it.copy(
                userLoginCookie = LoginCookie(
                    shiroJID = _uiState.value.userLoginCookie?.shiroJID ?: "",
                    ymId = text
                )
            )
        }
    }

    fun saveUserCookie() {
        viewModelScope.launch {
            dataStoreRepo.saveAirConditionUserCookie(_uiState.value.userLoginCookie!!)
        }
    }

    fun saveBuildingAndRoomId(buildingId: String, roomId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    roomCode = roomId,
                    buildingCode = buildingId
                )
            }
            dataStoreRepo.changeBuildingId(buildingId)
            dataStoreRepo.changeRoomId(roomId)
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
}