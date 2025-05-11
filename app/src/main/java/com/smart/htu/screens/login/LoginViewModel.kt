package com.smart.htu.screens.login

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessageEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BUILDING_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MOBILE_CODE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_QQ_NUMBER
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_ROOM_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_STUDENT_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN_VALIDITY
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
    val isLogSuccess: Boolean = false,
    val loginState: Int = DEFAULT_LOGIN_STATE, // -1 失败   0 未登录   1 登录成功
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val isGuest: Boolean = false,
    val isLoading: Boolean = false,
    val qqNumber: String = DEFAULT_QQ_NUMBER,
    val username: String = DEFAULT_USERNAME,
    val studentID: String = DEFAULT_STUDENT_ID,
    val password: String = DEFAULT_PASSWORD,
    val jwcPassword: String = DEFAULT_PASSWORD,
    val mobileCode: String = DEFAULT_MOBILE_CODE,
    val personalMessage: ResultWithStatus<PersonalMessageEntity> = ResultWithStatus(),
    val cookies: List<Cookie> = emptyList(),
    val token: String = DEFAULT_TOKEN,
    val isTokenValid: Boolean = DEFAULT_TOKEN_VALIDITY,
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

    private val qqNumberStateFlow = dataStoreRepo.observeQQNumber()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeQQNumber().first()
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

    private val tokenValidStateFlow = dataStoreRepo.observeTokenValidity()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTokenValidity().first()
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
            )
        }
        _uiState.update {
            it.copy(
                jwcPassword = passwordRepo.getPassword(JWC_PASSWORD) ?: DEFAULT_PASSWORD
            )
        }
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
            mobileCodeStateFlow.collect { value ->
                _uiState.update { it.copy(mobileCode = value) }
            }
        }
        viewModelScope.launch {
            checkJWCToken()
            if (_uiState.value.isTokenValid) getPersonalMessage()
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (_uiState.value.loginJWCState != 1) jwcLogin() // 智慧教务
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

    private suspend fun jwcLogin() {
        try {
            val logState = jwcNetworkRepo.jwcLogin(
                username = _uiState.value.studentID,
                password = _uiState.value.jwcPassword
            )
            logState.onSuccess {
                changeLoginJWCState(1)
                setTokenValid(true)
                getPersonalMessage()
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                changeUsername(it.user?.username ?: DEFAULT_USERNAME)
                dataStoreRepo.saveStudentId(_uiState.value.studentID)
                passwordRepo.savePassword(_uiState.value.jwcPassword, JWC_PASSWORD)
            }
            logState.onFailure {
                changeLoginJWCState(-1)
                showSnackBar(it.message ?: "智慧教务登录失败")
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login $e")
        }
    }

    private suspend fun checkJWCToken() {
        try {
            if (_uiState.value.studentID.isNotEmpty() && _uiState.value.jwcPassword.isNotEmpty()) {
                val res = jwcNetworkRepo.checkJWCTokenService()
                res.onSuccess {
                    setTokenValid(true)
                    changeLoginJWCState(1)
                }
                res.onFailure {
                    setTokenValid(false)
                    changeLoginJWCState(-1)
                }
            } else {
                setTokenValid(false)
                changeLoginJWCState(0)
            }
        } catch (e: Exception) {
            Log.i("TAG666 check", "Failed to check JWC token $e")
            changeLoginJWCState(1)
        }
    }

    suspend fun getPersonalMessage() {
        try {
            val res = jwcNetworkRepo.getPersonalMessageService()
            // Log.i("TAG666 longViewModel", res.toString())
            if (res?.code == 200) changeUsername(res.personalMessage?.username ?: DEFAULT_USERNAME)
            _uiState.update { it.copy(personalMessage = ResultWithStatus(res?.personalMessage)) }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to get student info $e")
        }
    }

    fun guest() = viewModelScope.launch {
        _uiState.update { it.copy(isGuest = true) }
        changeUsername("HNUer")
    }

    private fun changeLoginAuthState(state: Int) {
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
            dataStoreRepo.changeQQNumber(customQQNumber)
            _uiState.update { it.copy(qqNumber = customQQNumber) }
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
            _uiState.update { it.copy(username = username) }
        }
    }

    fun setTokenValid(valid: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setTokenValidity(valid)
            _uiState.update { it.copy(isTokenValid = valid) }
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
        editQQNumber(DEFAULT_QQ_NUMBER)
        setJWCLogToken(DEFAULT_TOKEN)
        setTokenValid(DEFAULT_TOKEN_VALIDITY)
        dataStoreRepo.saveCookies(emptyList())
        dataStoreRepo.changeRoomId(DEFAULT_BUILDING_ID)
        dataStoreRepo.changeBuildingId(DEFAULT_ROOM_ID)
    }

}