package com.smart.htu.screens.login

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessageEntity
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BUILDING_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MOBILE_CODE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_ROOM_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_STUDENT_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.PasswordRepo
import com.smart.htu.repo.PasswordRepo.Companion.JWC_PASSWORD
import com.smart.htu.repo.PasswordRepo.Companion.PASSWORD
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
    val loginState: Int = DEFAULT_LOGIN_STATE, // -1 失败   0 未登录   1 登录成功
    val loginJWCState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
    val isGuestModeEnable: Boolean = false,
    val isLoading: Boolean = false,
    val username: String = DEFAULT_USERNAME,
    val studentID: String = DEFAULT_STUDENT_ID,
    val password: String = DEFAULT_PASSWORD,
    val jwcPassword: String = DEFAULT_PASSWORD,
    val mobileCode: String = DEFAULT_MOBILE_CODE,
    val personalMessage: PersonalMessageEntity? = null,
    val cookies: List<Cookie> = emptyList(),
    val token: String = DEFAULT_TOKEN,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar,
    private val passwordRepo: PasswordRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
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

    private val cookieStateFlow = dataStoreRepo.observeAuthCookie()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAuthCookie().first()
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

    private val mobileCodeStateFlow = dataStoreRepo.observeMobileCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeMobileCode().first()
            }
        )

    init {
        _uiState.update {
            it.copy(
                password = passwordRepo.getPassword(PASSWORD) ?: DEFAULT_PASSWORD,
                jwcPassword = passwordRepo.getPassword(JWC_PASSWORD) ?: DEFAULT_PASSWORD
            )
        }
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            loginStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginState = value) }
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
            mobileCodeStateFlow.collect { value ->
                _uiState.update { it.copy(mobileCode = value) }
            }
        }
        viewModelScope.launch {
            checkJWCToken()
            if (_uiState.value.loginJWCState == 1)
                getPersonalMessage()
        }
    }

    fun login(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (_uiState.value.loginJWCState != 1) jwcLogin(onSuccess) // 智慧教务
            // if (_uiState.value.loginState != 1) authLogin() // 统一认证登录
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
                changeLoginAuthState(1)
                passwordRepo.savePassword(_uiState.value.password, PASSWORD)
            }
            logState.onFailure {
                changeLoginAuthState(-1)
            }
        } catch (_: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }

    /*private suspend fun getRefreshToken() {
        try {
            val logState = networkRepo.getAppTokenService(
                mobileCode = _uiState.value.mobileCode
            )
            logState.onSuccess {
                changLoginAuthState(1)
            }
        } catch (_: Exception) {
            Log.i("TAG666 viewModel", "Failed to login")
        }
    }*/

    suspend fun jwcLogin(onSuccess: () -> Unit = {}) {
        try {
            val logState = jwcNetworkRepo.jwcLogin(
                username = _uiState.value.studentID,
                password = _uiState.value.jwcPassword
            )
            logState.onSuccess {
                onSuccess()
                changeLoginJWCState(1)
                getPersonalMessage()
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                changeUsername(it.user?.username ?: DEFAULT_USERNAME)
                dataStoreRepo.saveStudentId(_uiState.value.studentID)
                passwordRepo.savePassword(_uiState.value.jwcPassword, JWC_PASSWORD)
            }
            logState.onFailure {
                changeLoginJWCState(-1)
                showSnackBar(it.message.toString())
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login $e")
        }
    }

    private suspend fun checkJWCToken() {
        try {
            if (_uiState.value.studentID.isNotEmpty() && _uiState.value.jwcPassword.isNotEmpty()) {
                jwcNetworkRepo.checkJWCTokenService()
                    .onSuccess { changeLoginJWCState(1) }
                    .onFailure { changeLoginJWCState(-2) }
            } else {
                changeLoginJWCState(0)
            }
        } catch (e: Exception) {
            Log.i("TAG666 check", "Failed to check JWC token $e")
            changeLoginJWCState(0)
        }
    }

    suspend fun getPersonalMessage() {
        jwcNetworkRepo.getPersonalMessageService()
            .onSuccess { res ->
                changeUsername(res.personalMessage?.username ?: DEFAULT_USERNAME)
                _uiState.update { it.copy(personalMessage = res.personalMessage) }
            }
    }

    fun guest() = viewModelScope.launch {
        _uiState.update { it.copy(isGuestModeEnable = true) }
        changeUsername("HNUer")
    }

    private fun changeLoginAuthState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginState(state)
        }
    }

    private fun changeLoginJWCState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginJWCState(state = state)
        }
    }

    fun setJWCLogToken(token: String) {
        viewModelScope.launch {
            dataStoreRepo.setJWCToken(token = token)
        }
    }

    fun changeStudentID(studentID: String) {
        _uiState.update { it.copy(loginJWCState = 0) }
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
        }
    }

    private fun clearCookies() {
        viewModelScope.launch {
            networkCookieJar.clearCookies()
        }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    fun logout() = viewModelScope.launch {
        clearCookies()
        changeLoginAuthState(DEFAULT_LOGIN_STATE)
        changeLoginJWCState(DEFAULT_LOGIN_STATE)
        changeUsername(DEFAULT_USERNAME)
        setJWCLogToken(DEFAULT_TOKEN)
        passwordRepo.clearPassword()
        dataStoreRepo.saveAuthCookie(emptyList())
        dataStoreRepo.changeRoomId(DEFAULT_BUILDING_ID)
        dataStoreRepo.changeBuildingId(DEFAULT_ROOM_ID)
    }

}