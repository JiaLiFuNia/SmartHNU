package com.smart.htu.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.MainActivity
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BUILDING_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_IS_TOKEN_VALID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_QQ_NUMBER
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_ROOM_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_STUDENT_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.PasswordManager
import com.smart.htu.repo.PasswordManager.Companion.JWC_PASSWORD
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
    val qqNumber: String = DEFAULT_QQ_NUMBER,
    val username: String = DEFAULT_USERNAME,
    val studentID: String = DEFAULT_STUDENT_ID,
    val password: String = DEFAULT_PASSWORD,
    val jwcPassword: String = DEFAULT_PASSWORD,
    val uneditableMessage: PersonalMessage = DEFAULT_MESSAGE,
    val cookies: List<Cookie> = emptyList(),
    val token: String = DEFAULT_TOKEN,
    val isTokenValid: Boolean = DEFAULT_IS_TOKEN_VALID,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar,
    private val passwordManager: PasswordManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val qqNumberStateFlow = dataStoreRepo.observePersonalMessage()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observePersonalMessage().first()
            }
        )

    private val loginStateStateFlow = dataStoreRepo.observeLoginState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginState().first()
            }
        )

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    private val cookieStateFlow = dataStoreRepo.observeCookies()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeCookies().first()
            }
        )

    private val tokenStateFlow = dataStoreRepo.observeJWCToken()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeJWCToken().first()
            }
        )

    private val usernameStateFlow = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeUsername().first()
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

    private val tokenValidStateFlow = dataStoreRepo.observeTokenValid()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTokenValid().first()
            }
        )

    init {
        _uiState.update { it.copy(password = passwordManager.getPassword() ?: "") }
        _uiState.update { it.copy(jwcPassword = passwordManager.getPassword("jwc_password") ?: "") }
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            qqNumberStateFlow.collect { value ->
                _uiState.update { it.copy(qqNumber = value) }
            }
        }
        viewModelScope.launch {
            loginStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginState = value) }
                _uiState.update { it.copy(isLogSuccess = value == 1) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
        viewModelScope.launch {
            tokenStateFlow.collect { value ->
                _uiState.update { it.copy(token = value) }
            }
        }
        viewModelScope.launch {
            tokenValidStateFlow.collect { value ->
                _uiState.update { it.copy(isTokenValid = value) }
            }
        }
        viewModelScope.launch {
            cookieStateFlow.collect { value ->
                _uiState.update { it.copy(cookies = value) }
            }
        }
        viewModelScope.launch {
            usernameStateFlow.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            studentIdStateFlow.collect { value ->
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
            if (_uiState.value.loginState != 1) authLogin() // 统一认证登录
            if (_uiState.value.loginJWCState != 1) jwcLogin() // 智慧教务
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
                passwordManager.savePassword(_uiState.value.password)
            }
            logState.onFailure {
                changLoginState(-1)
                MainActivity.snackBarHostState.showSnackbar(it.message ?: "统一认证登录失败")
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }

    private suspend fun jwcLogin() {
        try {
            val logState = jwcNetworkRepo.jwcLogin(
                username = _uiState.value.studentID,
                password = _uiState.value.jwcPassword
            )
            logState.onSuccess {
                changeLoginJWCState(1)
                setTokenValid(true)
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                passwordManager.savePassword(_uiState.value.jwcPassword, JWC_PASSWORD)
            }
            logState.onFailure {
                changeLoginJWCState(-1)
                MainActivity.snackBarHostState.showSnackbar(it.message ?: "智慧教务登录失败")
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }

    private suspend fun checkJWCToken() {
        try {
            val res = jwcNetworkRepo.checkJWCTokenService()
            res.onSuccess {
                setTokenValid(true)
                changeLoginJWCState(1)
            }
            res.onFailure {
                setTokenValid(false)
                changeLoginJWCState(-1)
            }
        } catch (e: Exception) {
            changeLoginJWCState(1)
        }
    }

    suspend fun getStudentInfo() {
        try {
            val res = networkRepo.getStudentInfo()
            // Log.i("TAG666 longViewModel", res.toString())
            changeUsername(res?.username ?: DEFAULT_USERNAME)
            changLoginState(1)
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

    fun setJWCLogToken(token: String) {
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
        _uiState.update { it.copy(studentID = studentID) }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(loginState = 0) }
        _uiState.update { it.copy(password = password) }
    }

    fun changeJWCPassword(password: String) {
        _uiState.update { it.copy(loginJWCState = 0) }
        _uiState.update { it.copy(jwcPassword = password) }
    }

    private fun changeUsername(username: String) {
        viewModelScope.launch {
            dataStoreRepo.changeUsername(username)
            _uiState.update { it.copy(username = username) }
        }
    }

    fun setTokenValid(valid: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setTokenValid(valid)
            _uiState.update { it.copy(isTokenValid = valid) }
        }
    }

    private fun clearCookies() {
        viewModelScope.launch {
            networkCookieJar.clearCookies()
        }
    }

    fun logout() = viewModelScope.launch {
        clearCookies()
        changLoginState(DEFAULT_LOGIN_STATE)
        changeLoginJWCState(DEFAULT_LOGIN_STATE)
        changeUsername(DEFAULT_USERNAME)
        changePassword(DEFAULT_PASSWORD)
        changeJWCPassword(DEFAULT_PASSWORD)
        editQQNumber(DEFAULT_QQ_NUMBER)
        setJWCLogToken(DEFAULT_TOKEN)
        dataStoreRepo.saveCookies(emptyList())
        dataStoreRepo.changeRoomId(DEFAULT_BUILDING_ID)
        dataStoreRepo.changeBuildingId(DEFAULT_ROOM_ID)
    }

}