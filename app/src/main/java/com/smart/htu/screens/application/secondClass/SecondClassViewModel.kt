package com.smart.htu.screens.application.secondClass

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.HourScoreEntity
import com.smart.htu.api.module.Term
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_STUDENT_ID
import com.smart.htu.repo.PasswordRepo
import com.smart.htu.repo.PasswordRepo.Companion.SC_PASSWORD
import com.smart.htu.repo.SecondClassNetworkRepo
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
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import javax.inject.Inject

data class SecondClassUiState(
    val scLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
    val studentID: String = DEFAULT_STUDENT_ID,
    val password: String = DEFAULT_PASSWORD,
    val cookie: String = "",
    val isLoading: Boolean = false,
    val hourList: List<HourScoreEntity>? = null,
    val termList: List<Term>? = emptyList(),
    val hazeEnabled: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class SecondClassViewModel @Inject constructor(
    private val scNetworkRepo: SecondClassNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val passwordRepo: PasswordRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecondClassUiState())
    val uiState: StateFlow<SecondClassUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()
    val category = listOf("经典", "报告", "活动", "实践", "竞赛", "劳动")

    private val sidStateFlow = dataStoreRepo.observeSecondClassSid()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeSecondClassSid().first()
            }
        )

    private val studentIdStateFlow = dataStoreRepo.observeStudentId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeStudentId().first()
            }
        )

    private val loginSCStateStateFlow = dataStoreRepo.observeLoginSCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginSCState().first()
            }
        )

    private val tempHourData = dataStoreRepo.observeSecondClassData()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeSecondClassData().first()
            }
        )

    init {
        viewModelScope.launch {
            sidStateFlow.collect { value ->
                _uiState.update { it.copy(cookie = value) }
            }
        }
        viewModelScope.launch {
            studentIdStateFlow.collect { value ->
                _uiState.update { it.copy(studentID = value) }
            }
        }
        viewModelScope.launch {
            loginSCStateStateFlow.collect { value ->
                _uiState.update { it.copy(scLoginState = value) }
            }
        }
        viewModelScope.launch {
            tempHourData.collect { value ->
                if (_uiState.value.scLoginState != 1)
                    _uiState.update {
                        it.copy(
                            hourList = value?.data,
                            termList = value?.termIndex
                        )
                    }
            }
        }
        viewModelScope.launch {
            getHourList()
        }
    }


    suspend fun getHourList() {
        // Log.i("TAG666 sc", "getHourList: ${_uiState.value.cookie}")
        if (_uiState.value.scLoginState == 1) {
            scNetworkRepo.getHourList("sid=${_uiState.value.cookie}")
                .onSuccess { res ->
                    _uiState.update {
                        it.copy(
                            hourList = res.data,
                            termList = res.termIndex.plus(Term("全部", 100))
                        )
                    }
                    dataStoreRepo.saveSecondClassData(res)
                }.onFailure { res ->
                    viewModelScope.launch {
                        showSnackbar(snackBarHostState, res.message.toString())
                    }
                    dataStoreRepo.changeLoginSCState(-2)
                }
        }
    }


    suspend fun loadSecondClassSid() {
        val sid = scNetworkRepo.getSCLoginPage()
        _uiState.update { it.copy(cookie = sid) }
    }

    suspend fun secondClassLogin(
        studentID: String = _uiState.value.studentID,
        password: String = _uiState.value.password,
        verifyCode: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        try {
            changeLoginSCState(2) // 登录中
            scNetworkRepo.scLogin(
                studentID = studentID,
                password = password,
                verifyCode = verifyCode,
                sid = _uiState.value.cookie
            ).onSuccess {
                onSuccess()
                changeLoginSCState(1)
                dataStoreRepo.saveSecondClassSid(_uiState.value.cookie)
                passwordRepo.savePassword(_uiState.value.password, SC_PASSWORD)
            }.onFailure {
                loadSecondClassSid()
                onFailure(it.message.toString())
                changeLoginSCState(-1)
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed $e")
        }
    }


    private suspend fun changeLoginSCState(state: Int) {
        dataStoreRepo.changeLoginSCState(state = state)
    }

}