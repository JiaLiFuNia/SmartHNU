package com.smart.htu.screens.application.airCondition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.AreaData
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.LoginCookie
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AirConditionUiState(
    var blurEffect: Boolean = true,
    val remoteLoginCookie: LoginCookie? = LoginCookie(shiroJID = "", ymId = ""),
    val userLoginCookie: LoginCookie? = LoginCookie("", ""),
    val customConfig: AreaData? = null,
    val buildingCode: String = "",
    val roomCode: String = "",
    val setCookieType: Int = 0,
    val billData: BillDetail? = null
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
            DEFAULT_BLUR_EFFECT
        )

    private val _buildingIdStateFlow = dataStoreRepo.observeBuildingId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ""
        )

    private val _roomIdStateFlow = dataStoreRepo.observeRoomId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ""
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
        getAirConditionConfig()
    }

    fun getAirConditionConfig(
        shiroJID: String = ("shiroJID=" + _uiState.value.remoteLoginCookie?.shiroJID),
        ymID: String = _uiState.value.remoteLoginCookie?.ymId ?: ""
    ) {
        viewModelScope.launch {
            val res = networkRepo.getAirConditionAreaService(shiroJID, ymID)
            res.onSuccess { _uiState.update { it.copy(customConfig = res.getOrNull()?.rows?.first()) } }
            Log.i("TAG666 air", res.getOrNull().toString())
        }
    }

    fun getBillDetailService() {
        val buildingCode = _uiState.value.buildingCode.takeLast(2)
        val floorCode = buildingCode + _uiState.value.roomCode.take(2)
        val roomCode = buildingCode + _uiState.value.roomCode
        viewModelScope.launch {
            val billData = networkRepo.getAirConditionBillService(
                shiroJID = "shiroJID=" + _uiState.value.remoteLoginCookie?.shiroJID,
                ymID = _uiState.value.remoteLoginCookie?.ymId ?: "",
                areaId = _uiState.value.customConfig?.id ?: "",
                buildingCode = buildingCode,
                floorCode = floorCode,
                roomCode = roomCode
            )
            billData.onSuccess {
                _uiState.update { it.copy(billData = billData.getOrNull()) }
            }
        }
    }

    fun changeShiroJid(text: String) {
        _uiState.update {
            it.copy(
                userLoginCookie = LoginCookie(
                    shiroJID = text,
                    ymId = _uiState.value.userLoginCookie?.ymId ?: ""
                )
            )
        }
    }

    fun changeYmld(text: String) {
        _uiState.update {
            it.copy(
                userLoginCookie = LoginCookie(
                    shiroJID = _uiState.value.userLoginCookie?.shiroJID ?: "",
                    ymId = text
                )
            )
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
        _uiState.update { it.copy(setCookieType = type) }
    }
}