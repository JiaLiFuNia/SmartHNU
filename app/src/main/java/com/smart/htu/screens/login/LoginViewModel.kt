package com.smart.htu.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_EDITABLE_PERSONAL_MESSAGE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
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
    val loginState: Int = 0, // -1 密码错误   0 未登录   1 登录成功   2 账号或密码为空  3 登录过期
    val isLoading: Boolean = false,
    var qqNumber: String = "",
    var uneditableMessage: PersonalMessage, // 联网获取
    val studentID: String = "",
    val password: String = "",
    val username: String = DEFAULT_USERNAME,
    val cookies: List<Cookie> = emptyList()
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(
            uneditableMessage = DEFAULT_MESSAGE
        )
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _editablePersonalMessage = dataStoreRepo.observePersonalMessage().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DEFAULT_EDITABLE_PERSONAL_MESSAGE
    )

    private val _loginState = dataStoreRepo.observeLoginState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private val _cookie = dataStoreRepo.observeCookies().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _username = dataStoreRepo.observeUsername().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DEFAULT_USERNAME
    )

    init {
        viewModelScope.launch {
            _editablePersonalMessage.collect { value ->
                _uiState.update {
                    it.copy(qqNumber = value)
                }
            }
        }
        viewModelScope.launch {
            _loginState.collect { value ->
                _uiState.update {
                    it.copy(isLogSuccess = value == 1)
                }
            }
        }
        viewModelScope.launch {
            _cookie.collect { value ->
                _uiState.update {
                    it.copy(cookies = value)
                }
            }
        }
        viewModelScope.launch {
            _username.collect { value ->
                _uiState.update {
                    it.copy(username = value)
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val res = networkRepo.authLogin(_uiState.value.studentID, _uiState.value.password)
                Log.i("TAG666 longViewModel", res.toString())
                changLoginState(res)

                Log.i("TAG666 loginCookie", _uiState.value.cookies.toString())
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
                Log.i("TAG666 longViewModel", res.toString())
                dataStoreRepo.changeUsername(res?.username ?: DEFAULT_USERNAME)
                dataStoreRepo.changeLoginState(1)
                _uiState.update { it.copy(loginState = 1) }
                _uiState.update { it.copy(uneditableMessage = res!!) }
                _uiState.update { it.copy(username = res?.username ?: DEFAULT_USERNAME) }
            } catch (e: Exception) {
                Log.i("TAG666 viewModel", "Failed to get student info")
                dataStoreRepo.changeLoginState(3)
                _uiState.update { it.copy(loginState = 3) }
            }
        }
    }

    fun changLoginState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginState(state)
            _uiState.update {
                it.copy(loginState = state)
            }
        }
    }

    fun setLogSuccess(isLogSuccess: Boolean) {
        _uiState.update {
            it.copy(isLogSuccess = isLogSuccess)
        }
    }

    fun changeEditableMessage(customQQNumber: String) {
        viewModelScope.launch {
            dataStoreRepo.changPersonalMessage(customQQNumber)
            _uiState.update {
                it.copy(qqNumber = customQQNumber)
            }

        }
    }

    fun changeStudentID(studentID: String) {
        _uiState.update {
            it.copy(studentID = studentID)
        }
    }

    fun changePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun cleanCookies() {
        networkCookieJar.clear()
    }

}