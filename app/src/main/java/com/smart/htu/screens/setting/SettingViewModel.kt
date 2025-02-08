package com.smart.htu.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.component.SelectionItem
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val dynamicColor: Boolean = true,
    val isDarkTheme: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val languageList: List<SelectionItem<String>>,
    val selectedLanguageIndex: Int = 0,
    val updateState: Boolean = true
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private var _languageMap = mapOf(
        "中文(简体)" to "zh",
        "English" to "en"
    )

    private val _uiState = MutableStateFlow(
        SettingUiState(
            languageList = _languageMap.map {
                SelectionItem(it.key, it.value)
            }
        )
    )

    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val _dynamicColorStateFlow = dataStoreRepo.observeDynamicTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true
        )

    private val _darkThemeStateFlow = dataStoreRepo.observeDarkTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_BLUR_EFFECT
        )

    init {
        viewModelScope.launch {
            _dynamicColorStateFlow.collect { value ->
                _uiState.update { it.copy(dynamicColor = value) }
            }
        }
        viewModelScope.launch {
            _darkThemeStateFlow.collect { value ->
                _uiState.update { it.copy(isDarkTheme = value) }
            }
        }
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
    }

    fun changeDynamicTheme(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeDynamicTheme(enabled)
        }
    }

    fun changDarkMode(isDarkTheme: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeDarkTheme(isDarkTheme)
        }
    }

    fun changeBlurState() {
        viewModelScope.launch {
            dataStoreRepo.changeBlurState(state = !_uiState.value.blurEffect)
        }
    }

    /*fun changeLanguage(index: Int, context: Context) {
        viewModelScope.launch {
            val selectedLanguage = _uiState.value.languageList[index].value
            val locale = Locale(selectedLanguage)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            _uiState.update {
                it.copy(selectedLanguageIndex = index)
            }
        }
    }*/
}