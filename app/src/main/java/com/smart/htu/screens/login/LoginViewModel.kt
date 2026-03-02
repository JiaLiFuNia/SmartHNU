package com.smart.htu.screens.login

import android.util.Log
import android.webkit.CookieManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessageEntity
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BUILDING_ID
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_EMPTY_STRING
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
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import javax.inject.Inject

data class LoginUiState(
    val authLoginState: Int = DEFAULT_LOGIN_STATE, // -1 失败   0 未登录   1 登录成功 2 登录中
    val jwcLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
    val scLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
    val libraryLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未登录 1 登录成功 -1 登录失败 -2 token过期
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

    private val loginAuthStateStateFlow = dataStoreRepo.observeLoginState()
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

    private val loginLibStateStateFlow = dataStoreRepo.observeLoginLibraryState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginLibraryState().first()
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

    private val loginSCStateStateFlow = dataStoreRepo.observeLoginSCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginSCState().first()
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
            loginAuthStateStateFlow.collect { value ->
                _uiState.update { it.copy(authLoginState = value) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(jwcLoginState = value) }
            }
        }
        viewModelScope.launch {
            loginSCStateStateFlow.collect { value ->
                _uiState.update { it.copy(scLoginState = value) }
            }
        }
        viewModelScope.launch {
            loginLibStateStateFlow.collect { value ->
                _uiState.update { it.copy(libraryLoginState = value) }
            }
        }
        viewModelScope.launch {
            tokenStateFlow.collect { value ->
                _uiState.update { it.copy(token = value) }
            }
        }
        viewModelScope.launch {
            cookieStateFlow.collect { value ->
                _uiState.update { it.copy(cookies = networkCookieJar.loadAllCookies()) }
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
            getPersonalMessage()
        }
    }

    suspend fun authLogin(
        studentID: String = _uiState.value.studentID,
        password: String = _uiState.value.password,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        try {
            changeLoginAuthState(2) // 登录中
            clearAllCookies()
            val logState = networkRepo.authLogin(
                studentId = studentID,
                password = password
            )
            logState.onSuccess {
                onSuccess()
                changeLoginAuthState(1)
                passwordRepo.savePassword(_uiState.value.password, PASSWORD)
            }
            logState.onFailure {
                onFailure()
                changeLoginAuthState(-1)
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed $e")
        }
    }

    suspend fun jwcLogin(onFailure: (String) -> Unit = {}, onSuccess: () -> Unit) {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val logState = jwcNetworkRepo.jwcLogin(
                username = _uiState.value.studentID,
                password = _uiState.value.jwcPassword
            )
            logState.onSuccess {
                changeLoginJWCState(1)
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                changeUsername(it.user?.username ?: DEFAULT_USERNAME)
                dataStoreRepo.saveStudentId(_uiState.value.studentID)
                passwordRepo.savePassword(_uiState.value.jwcPassword, JWC_PASSWORD)
                onSuccess()
            }
            logState.onFailure {
                changeLoginJWCState(-1)
                onFailure(it.message.toString())
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login $e")
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    suspend fun wechatLogin(code: String, onFailure: (String) -> Unit = {}, onSuccess: () -> Unit) {
        try {
            val logState = jwcNetworkRepo.wechatLogin(code)
            logState.onSuccess {
                changeLoginJWCState(1)
                setJWCLogToken(it.user?.token ?: DEFAULT_TOKEN)
                changeUsername(it.user?.username ?: DEFAULT_USERNAME)
                dataStoreRepo.saveStudentId(it.user?.studentId ?: DEFAULT_STUDENT_ID)
                onSuccess()
            }
            logState.onFailure {
                changeLoginJWCState(-1)
                onFailure(it.message.toString())
            }
        } catch (e: Exception) {
            Log.i("TAG666 viewModel", "Failed to login $e")
        }
    }

    suspend fun testLogin() {
        changeLoginJWCState(1)
    }

    suspend fun getPersonalMessage() {
        jwcNetworkRepo.getPersonalMessageService()
            .onSuccess { res ->
                changeUsername(res.personalMessage?.username ?: DEFAULT_USERNAME)
                _uiState.update { it.copy(personalMessage = res.personalMessage) }
            }
    }

    fun guestLogin() {
        _uiState.update { it.copy(isGuestModeEnable = true) }
        changeUsername("HNUer")
    }

    private suspend fun changeLoginAuthState(state: Int) {
        dataStoreRepo.changeLoginState(state)
    }

    private suspend fun changeLoginJWCState(state: Int) {
        dataStoreRepo.changeLoginJWCState(state = state)
    }

    private suspend fun changeLoginSCState(state: Int) {
        dataStoreRepo.changeLoginSCState(state = state)
    }

    private suspend fun changeLoginLibState(state: Int) {
        dataStoreRepo.changeLoginLibraryState(state = state)
    }

    fun setJWCLogToken(token: String) {
        viewModelScope.launch {
            dataStoreRepo.setJWCToken(token = token)
        }
    }

    fun changeStudentID(studentID: String) {
        _uiState.update { it.copy(jwcLoginState = 0) }
        _uiState.update { it.copy(studentID = studentID) }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(authLoginState = 0) }
        _uiState.update { it.copy(password = password) }
    }

    fun changeJWCPassword(password: String) {
        _uiState.update { it.copy(jwcLoginState = 0) }
        _uiState.update { it.copy(jwcPassword = password) }
    }

    private fun changeUsername(username: String) {
        viewModelScope.launch {
            dataStoreRepo.changeUsername(username)
        }
    }

    suspend fun clearAllCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        networkCookieJar.clearCookies()
        changeLoginAuthState(DEFAULT_LOGIN_STATE)
        changeLoginSCState(DEFAULT_LOGIN_STATE)
        changeLoginLibState(DEFAULT_LOGIN_STATE)
    }

    suspend fun logout() {
        clearAllCookies()
        changeLoginAuthState(DEFAULT_LOGIN_STATE)
        changeLoginJWCState(DEFAULT_LOGIN_STATE)
        changeLoginSCState(DEFAULT_LOGIN_STATE)
        changeLoginLibState(DEFAULT_LOGIN_STATE)
        changeUsername(DEFAULT_USERNAME)
        setJWCLogToken(DEFAULT_TOKEN)
        passwordRepo.clearPassword()
        dataStoreRepo.changeRoomId(DEFAULT_BUILDING_ID)
        dataStoreRepo.saveDormRoomId(DEFAULT_ROOM_ID)
        dataStoreRepo.saveSecondClassSid(DEFAULT_EMPTY_STRING)
    }

}