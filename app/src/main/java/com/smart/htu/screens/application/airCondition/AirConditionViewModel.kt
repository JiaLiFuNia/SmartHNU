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
import com.smart.htu.utils.ToastUtil.showSnackbar
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
    val dormRoomId: String = "",
    val campusId: Int = 0,
    val buildingId: String = "",
    val roomId: String = "",
    val cookieType: Int = 0,
    val billData: BillDetail? = null,
    val billRecords: BillRecords? = null,
    val buyRecords: BuyRecords? = null,
    val isCheckingConfig: Boolean = false,
    val isCookieValid: Boolean = true
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

    private val dormRoomIdStateFlow = dataStoreRepo.observeBuildingId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBuildingId().first()
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
            dormRoomIdStateFlow.collect { value ->
                _uiState.update {
                    it.copy(dormRoomId = value)
                }
                if (_uiState.value.dormRoomId.length == 6) {
                    val buildingCode = value.substring(0, 2).toInt()
                    _uiState.update {
                        it.copy(
                            campusId = if (buildingCode <= 20) 0 else 1,
                            buildingId = if (buildingCode <= 20) value.substring(
                                0,
                                2
                            ) else (buildingCode - 20).toString(),
                            roomId = value.substring(2, 6)
                        )
                    }
                }
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
            getAirConditionConfig()
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
            if (_uiState.value.dormRoomId.isNotEmpty()) {
                getCurrentBillData()
                getBillRecords()
                getBuyRecords()
            }
        }
    }

    // 用于获取 areaId
    suspend fun getAirConditionConfig(
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
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
    suspend fun getCurrentBillData(
        onSuccess: (String) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        try {
            val cookie = getCookieByType()
            val buildingCode = _uiState.value.dormRoomId.substring(0, 2)
            val floorCode = _uiState.value.dormRoomId.substring(0, 4)
            val roomCode = _uiState.value.dormRoomId
            Log.i("TAG666", "getCurrentBillData: $buildingCode $floorCode $roomCode")
            networkRepo.getAirConditionCurrentBillDataService(
                shiroJID = "shiroJID=${cookie.shiroJID}",
                ymId = cookie.ymId,
                areaId = _uiState.value.customConfig?.id ?: "",
                buildingCode = buildingCode,
                floorCode = floorCode,
                roomCode = roomCode
            ).onSuccess { res ->
                _uiState.update { it.copy(billData = res) }
                onSuccess(res.data?.displayRoomName ?: "")
            }.onFailure {
                changeCookieValidState(false)
                onFailure(it.message.toString())
            }
        } catch (e: Exception) {
            Log.e("TAG666", "getCurrentBillData: ${e.message}")
            onFailure(e.message.toString())
        }
    }

    // 用电记录
    suspend fun getBillRecords() {
        try {
            val cookie = getCookieByType()
            val buildingCode = _uiState.value.dormRoomId.substring(0, 2)
            val floorCode = _uiState.value.dormRoomId.substring(0, 4)
            val roomCode = _uiState.value.dormRoomId
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
        } catch (e: Exception) {
            Log.e("TAG666", "getBillRecords: ${e.message}")
        }
    }

    // 充值记录
    suspend fun getBuyRecords() {
        try {
            val cookie = getCookieByType()
            val buildingCode = _uiState.value.dormRoomId.substring(0, 2)
            val floorCode = _uiState.value.dormRoomId.substring(0, 4)
            val roomCode = _uiState.value.dormRoomId
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
        } catch (e: Exception) {
            Log.e("TAG666", "getBuyRecords: ${e.message}")
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

    suspend fun saveACConfig(
        campusLocation: Int,
        buildingId: String,
        roomId: String,
        shiroJID: String,
        ymId: String
    ) {
        _uiState.update { it.copy(isCheckingConfig = true) }
        _uiState.update {
            it.copy(
                campusId = campusLocation,
                buildingId = buildingId,
                roomId = roomId,
                dormRoomId = (if (campusLocation == 0) buildingId else (buildingId.toInt() + 20).toString()) + roomId
            )
        }
        Log.i("TAG666 dormRoomId", _uiState.value.dormRoomId)
        // 保存用户自定义 cookie
        changeUserLoginCookie(shiroJID, ymId)
        // 刷新 cookie 和 配置
        getRemoteLoginCookie()
        // 验证cookie
        getAirConditionConfig(
            onSuccess = { },
            onFailure = {
                showSnackBar("配置保存失败，请检查 Cookie")
            }
        )
        // 验证寝室信息
        getCurrentBillData(
            onSuccess = {
                showSnackBar("配置保存成功, $it")
                viewModelScope.launch { dataStoreRepo.saveDormRoomId(_uiState.value.dormRoomId) }
            },
            onFailure = {
                showSnackBar("配置保存失败，${it}")
            }
        )
        _uiState.update { it.copy(isCheckingConfig = false) }
    }

    fun changeLoginCookieType(type: Int) {
        viewModelScope.launch {
            dataStoreRepo.saveAirConditionCookieType(type)
        }
    }

    private fun changeCookieValidState(state: Boolean) {
        _uiState.update { it.copy(isCookieValid = state) }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            showSnackbar(snackBarHostState, message)
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