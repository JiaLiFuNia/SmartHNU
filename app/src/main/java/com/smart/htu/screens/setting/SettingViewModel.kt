package com.smart.htu.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.component.SelectionItem
import com.smart.htu.di.DataStoreRepo
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
    var dynamicColor: Boolean = true,
    var isDarkTheme: Int = 0,
    val languageList: List<SelectionItem<String>>,
    val selectedLanguageIndex: Int = 0,
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

    private val dynamicColorStateFlow = dataStoreRepo.observeDynamicTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true
        )

    private val darkThemeStateFlow = dataStoreRepo.observeDarkTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    init {
        viewModelScope.launch {
            dynamicColorStateFlow.collect { value ->
                _uiState.update { it.copy(dynamicColor = value) }
            }
        }
        viewModelScope.launch {
            darkThemeStateFlow.collect { value ->
                _uiState.update { it.copy(isDarkTheme = value) }
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