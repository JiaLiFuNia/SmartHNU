package com.smart.htu.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BUILDING_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_QQ_NUMBER
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_ROOM_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_STUDENT_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
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
import okhttp3.Cookie
import javax.inject.Inject

data class LoginUiState(
    val isLogSuccess: Boolean = false,
    val loginState: Int = DEFAULT_LOGIN_STATE, // -1 失败   0 未登录   1 登录成功   2 登录过期
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val isGuest: Boolean = false,
    val isLoading: Boolean = false,
    val logTipMessage: String = "",
    val qqNumber: String = DEFAULT_QQ_NUMBER,
    val username: String = DEFAULT_USERNAME,
    val studentID: String = DEFAULT_STUDENT_ID,
    val password: String = DEFAULT_PASSWORD,
    val uneditableMessage: PersonalMessage = DEFAULT_MESSAGE,
    val cookies: List<Cookie> = emptyList(),
    val token: String = DEFAULT_TOKEN,
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
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val _qqNumberStateFlow = dataStoreRepo.observePersonalMessage()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observePersonalMessage().first()
            }
        )

    private val _loginStateStateFlow = dataStoreRepo.observeLoginState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginState().first()
            }
        )

    private val _loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    private val _cookieStateFlow = dataStoreRepo.observeCookies()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeCookies().first()
            }
        )

    private val _tokenStateFlow = dataStoreRepo.observeJWCToken()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeJWCToken().first()
            }
        )

    private val _usernameStateFlow = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeUsername().first()
            }
        )

    private val _studentIdStateFlow = dataStoreRepo.observeStudentId()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeStudentId().first()
            }
        )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            _qqNumberStateFlow.collect { value ->
                _uiState.update { it.copy(qqNumber = value) }
            }
        }
        viewModelScope.launch {
            _loginStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginState = value) }
                _uiState.update { it.copy(isLogSuccess = value == 1) }
            }
        }
        viewModelScope.launch {
            _loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
        viewModelScope.launch {
            _tokenStateFlow.collect { value ->
                _uiState.update { it.copy(token = value) }
            }
        }
        viewModelScope.launch {
            _cookieStateFlow.collect { value ->
                _uiState.update { it.copy(cookies = value) }
            }
        }
        viewModelScope.launch {
            _usernameStateFlow.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            _studentIdStateFlow.collect { value ->
                _uiState.update { it.copy(studentID = value) }
            }
        }
        viewModelScope.launch {
            getStudentInfo()
            if (_uiState.value.token != DEFAULT_TOKEN) {
                checkJWCToken()
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authLogin() // 统一认证登录
            jwcLogin() // 智慧教务
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun authLogin() {
        try {
            clearCookies()
            val logState = networkRepo.authLogin(
                studentId = _uiState.value.studentID,
                password = _uiState.value.password
            )
            logState.onSuccess {
                changLoginState(1)
                getStudentInfo()
                dataStoreRepo.saveStudentId(_uiState.value.studentID)
                changeLogTipMessage("统一身份认证登录成功")
            }
            logState.onFailure {
                changLoginState(-1)
                changeLogTipMessage(it.message ?: "登录失败")
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }

    private suspend fun jwcLogin() {
        try {
            val logState = networkRepo.jwcLogin(
                username = _uiState.value.studentID,
                password = _uiState.value.password
            )
            logState.onSuccess {
                changeLoginJWCState(1)
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                changeLogTipMessage("河南师大智慧教务登录成功")
            }
            logState.onFailure { changeLoginJWCState(-1) }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }

    private suspend fun checkJWCToken() {
        try {
            val res = networkRepo.checkJWCTokenService(_uiState.value.token)
            res.onSuccess { changeLoginJWCState(1) }
            res.onFailure { jwcLogin() }
        } catch (e: Exception) {
            changeLoginJWCState(-1)
        }
    }

    suspend fun getStudentInfo() {
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

    fun guest() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGuest = true) }
            changeUsername("HNUer")
        }
    }

    private fun changLoginState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginState(state)
            _uiState.update { it.copy(loginState = state) }
        }
    }

    private fun changeLoginJWCState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginJWCState(state = state)
            _uiState.update { it.copy(loginJWCState = state) }
        }
    }

    private fun setJWCLogToken(token: String) {
        viewModelScope.launch {
            dataStoreRepo.setJWCToken(token = token)
            _uiState.update { it.copy(token = token) }
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
        changLoginState(DEFAULT_LOGIN_STATE)
        changeUsername(DEFAULT_USERNAME)
        changePassword(DEFAULT_PASSWORD)
        editQQNumber(DEFAULT_QQ_NUMBER)
        dataStoreRepo.saveCookies(emptyList())
        dataStoreRepo.changeRoomId(DEFAULT_BUILDING_ID)
        dataStoreRepo.changeBuildingId(DEFAULT_ROOM_ID)
    }

}