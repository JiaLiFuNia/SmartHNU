package com.smart.htu.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.component.SelectionItem
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.utils.Term.getCurrentTerm
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
import javax.inject.Inject

data class SettingUiState(
    val dynamicColor: Boolean = true,
    val isDarkTheme: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val languageList: List<SelectionItem<String>>,
    val selectedLanguageIndex: Int = 0,
    val updateState: Boolean = true,
    val termCode: String
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val languageMap = mapOf(
        "中文(简体)" to "zh",
        "English" to "en"
    )

    private val _uiState = MutableStateFlow(
        SettingUiState(
            termCode = getCurrentTerm(),
            languageList = languageMap.map {
                SelectionItem(it.key, it.value)
            }
        )
    )

    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val dynamicColorStateFlow = dataStoreRepo.observeDynamicTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeDynamicTheme().first()
            }
        )

    private val darkThemeStateFlow = dataStoreRepo.observeDarkTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeDarkTheme().first()
            }
        )

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val termCodeStateFlow = dataStoreRepo.observeOverallTermCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            getCurrentTerm()
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
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            termCodeStateFlow.collect { value ->
                _uiState.update { it.copy(termCode = value) }
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

    fun changeBlurState(state: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeBlurState(state = state)
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