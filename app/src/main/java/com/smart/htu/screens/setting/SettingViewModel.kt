package com.smart.htu.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.smart.htu.App.Companion.context
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_THEME_MODE
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.CoilUtil.formatFileSize
import com.smart.htu.utils.CoilUtil.getDirectorySize
import com.smart.htu.utils.Term.getCurrentTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

data class SettingUiState(
    val themeMode: Int = DEFAULT_THEME_MODE,
    val isDarkTheme: Int = 0,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val selectedLanguageIndex: Int = 0,
    val updateInfo: UpdateEntity = UpdateEntity(),
    val isUpdate: Boolean = false,
    val termCode: String,
    val cacheSize: String = "计算中..."
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val languageMap = mapOf(
        "中文(简体)" to "zh",
        "English" to "en"
    )

    private val _uiState = MutableStateFlow(
        SettingUiState(
            termCode = getCurrentTerm()
        )
    )

    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val themeModeStateFlow = dataStoreRepo.observeThemeMode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeThemeMode().first()
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

    init {
        viewModelScope.launch {
            themeModeStateFlow.collect { value ->
                _uiState.update { it.copy(themeMode = value) }
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
            sharedDataRepository.termIndex
                .collect { termIndex ->
                    _uiState.update {
                        it.copy(termCode = termIndex?.termCode ?: getCurrentTerm())
                    }
                }
        }
        viewModelScope.launch {
            sharedDataRepository.update
                .collect { config ->
                    _uiState.update {
                        it.copy(
                            updateInfo = config ?: UpdateEntity(),
                            isUpdate = config?.isNeedUpdate == true
                        )
                    }
                }
        }
        calculateCacheSize()
    }


    suspend fun getUpdate(): Boolean {
        sharedDataRepository.getUpdate()
            .onSuccess {
                return it.isNeedUpdate
            }
        return false
    }

    fun changeDynamicTheme(mode: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeThemeMode(mode)
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

    fun calculateCacheSize() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val coilCacheDir = File(context.cacheDir, "image_cache")
                    val size = if (coilCacheDir.exists() && coilCacheDir.isDirectory) {
                        getDirectorySize(coilCacheDir)
                    } else {
                        0L
                    }
                    _uiState.update { it.copy(cacheSize = formatFileSize(size)) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _uiState.update { it.copy(cacheSize = "计算失败") }
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    fun clearCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val imageLoader = context.imageLoader
                    imageLoader.memoryCache?.clear()
                    imageLoader.diskCache?.clear()

                    val cacheDir = context.cacheDir
                    if (cacheDir.exists() && cacheDir.isDirectory) {
                        cacheDir.listFiles()?.forEach { file ->
                            if (file.isDirectory) {
                                file.deleteRecursively()
                            } else {
                                file.delete()
                            }
                        }
                    }

                    calculateCacheSize()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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