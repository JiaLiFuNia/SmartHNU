package com.smart.htu.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_QQ_NUMBER
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Cookie
import javax.inject.Inject

data class LoginUiState(
    val isLogSuccess: Boolean = false,
    val loginState: Int = DEFAULT_LOGIN_STATE, // -1 失败   0 未登录   1 登录成功   2 登录过期
    val isGuest: Boolean = false,
    val isLoading: Boolean = false,
    val logTipMessage: String = "",
    val qqNumber: String = DEFAULT_QQ_NUMBER,
    val username: String = DEFAULT_USERNAME,
    val studentID: String = "",
    val password: String = "",
    val uneditableMessage: PersonalMessage = DEFAULT_MESSAGE,
    val cookies: List<Cookie> = emptyList(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_BLUR_EFFECT
        )

    private val _qqNumber = dataStoreRepo.observePersonalMessage()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_QQ_NUMBER
        )

    private val _loginState = dataStoreRepo.observeLoginState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_LOGIN_STATE
        )

    private val _cookie = dataStoreRepo.observeCookies()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _username = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_USERNAME
        )

    private val _studentId = dataStoreRepo.observeStudentId()
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
            _qqNumber.collect { value ->
                _uiState.update { it.copy(qqNumber = value) }
            }
        }
        viewModelScope.launch {
            _loginState.collect { value ->
                _uiState.update { it.copy(isLogSuccess = value == 1) }
            }
        }
        viewModelScope.launch {
            _cookie.collect { value ->
                _uiState.update { it.copy(cookies = value) }
            }
        }
        viewModelScope.launch {
            _username.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            _studentId.collect { value ->
                _uiState.update { it.copy(studentID = value) }
            }
        }
        getStudentInfo()
    }

    fun login() {
        viewModelScope.launch {
            try {
                clearCookies()
                _uiState.update { it.copy(isLoading = true) }
                val logState = networkRepo.authLogin(
                    studentId = _uiState.value.studentID,
                    password = _uiState.value.password
                )
                changeLogTipMessage(logState.toString())
                logState.onSuccess {
                    changLoginState(1)
                    getStudentInfo()
                }
                logState.onFailure { changLoginState(-1) }
            } catch (e: Exception) {
                Log.i("TAG666 viewModel", "Failed to login")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun jwcLogin() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val logState = networkRepo.jwcLogin(
                    username = _uiState.value.studentID,
                    password = _uiState.value.password
                )
            } catch (e: Exception) {
                Log.i("TAG666 viewModel", "Failed to login")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun getStudentInfo() {
        viewModelScope.launch {
            try {
                val res = networkRepo.getStudentInfo()
                // Log.i("TAG666 longViewModel", res.toString())
                changeUsername(username = res?.username ?: DEFAULT_USERNAME)
                changLoginState(state = 1)
                _uiState.update { it.copy(uneditableMessage = res!!) }
            } catch (e: Exception) {
                // Log.i("TAG666 viewModel", "Failed to get student info")
                changLoginState(if (_uiState.value.loginState == 1) 2 else 0)
            }
        }
    }

    fun guest() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGuest = true) }
            dataStoreRepo.changeUsername(name = "游客")
        }
    }

    private fun changLoginState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginState(state)
            _uiState.update { it.copy(loginState = state) }
        }
    }

    fun editQQNumber(customQQNumber: String) {
        viewModelScope.launch {
            dataStoreRepo.changPersonalMessage(customQQNumber)
            _uiState.update { it.copy(qqNumber = customQQNumber) }
        }
    }

    fun changeStudentID(studentID: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(studentID = studentID) }
            dataStoreRepo.saveStudentId(studentID)
        }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    private fun changeUsername(username: String) {
        viewModelScope.launch {
            dataStoreRepo.changeUsername(username)
            _uiState.update { it.copy(username = username) }
        }
    }

    private fun changeLogTipMessage(message: String) {
        _uiState.update { it.copy(logTipMessage = message) }
    }

    private fun clearCookies() {
        viewModelScope.launch {
            networkCookieJar.clearCookies()
        }
    }

    fun logout() = viewModelScope.launch {
        clearCookies()
        changLoginState(0)
        changeUsername(DEFAULT_USERNAME)
        changePassword("")
        editQQNumber("")
        dataStoreRepo.saveCookies(emptyList())
    }

}