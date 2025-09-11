package com.smart.htu.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.smart.htu.App.Companion.context
import com.smart.htu.api.module.AIMessageEntity
import com.smart.htu.api.module.AIModelConfigEntity
import com.smart.htu.api.module.AIModulePostEntity
import com.smart.htu.api.module.AIRole
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_THEME_MODE
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.CoilUtil.formatFileSize
import com.smart.htu.utils.CoilUtil.getDirectorySize
import com.smart.htu.utils.TermUtil.getCurrentTerm
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
    val blurEnabled: Boolean = true,
    val selectedLanguageIndex: Int = 0,
    val updateInfo: UpdateEntity = UpdateEntity(),
    val aiFunctionEnabled: Boolean = false,
    val aiModuleConfig: AIModelConfigEntity,
    val isTestLoading: Boolean = false,
    val isUpdate: Boolean = false,
    val termCode: String,
    val cacheSize: String = "计算中...",
    val loadImgEnabled: Boolean = true,
    val newsFontSize: Float = 17f,
    val bionicReadingEnabled: Boolean = true,
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    /*private val languageMap = mapOf(
        "中文(简体)" to "zh",
        "English" to "en"
    )*/

    private val _uiState = MutableStateFlow(
        SettingUiState(
            termCode = getCurrentTerm(),
            aiModuleConfig = AIModelConfigEntity(
                url = "https://chat.htu.edu.cn/api/chat/completions",
                module = "DeepSeek-R1-Distill-Llama-70B"
            )
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

    private val blurEnabledStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val aiFunctionEnabledStateFlow = dataStoreRepo.observeAIFunctionEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAIFunctionEnabled().first()
            }
        )

    private val aiModelKeyStateFlow = dataStoreRepo.observeAIModelConfig()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAIModelConfig().first()
            }
        )

    private val loadImgEnabledStateFlow = dataStoreRepo.observeLoadImgEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoadImgEnabled().first()
            }
        )

    private val newsFontSizeStateFlow = dataStoreRepo.observeNewsFontSize()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeNewsFontSize().first()
            }
        )

    private val bionicReadingEnabledStateFlow = dataStoreRepo.observeBionicReadingEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBionicReadingEnabled().first()
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
            blurEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(blurEnabled = value) }
            }
        }
        viewModelScope.launch {
            aiModelKeyStateFlow.collect { value ->
                _uiState.update { it.copy(aiModuleConfig = AIModelConfigEntity(key = value)) }
            }
        }
        viewModelScope.launch {
            aiFunctionEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(aiFunctionEnabled = value) }
            }
        }
        viewModelScope.launch {
            loadImgEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(loadImgEnabled = value) }
            }
        }
        viewModelScope.launch {
            newsFontSizeStateFlow.collect { value ->
                _uiState.update { it.copy(newsFontSize = value.toFloat()) }
            }
        }
        viewModelScope.launch {
            bionicReadingEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(bionicReadingEnabled = value) }
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

    fun changeLoadImgEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeLoadImgEnabled(enabled)
        }
    }

    fun changeAiFunctionEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeAIFunctionEnabled(enabled)
        }
    }

    fun saveAIModelKey(key: String) {
        viewModelScope.launch {
            dataStoreRepo.saveAIModelConfig(AIModelConfigEntity(key = key))
        }
    }

    fun setAIModuleConfig(
        url: String = _uiState.value.aiModuleConfig.url,
        module: String = _uiState.value.aiModuleConfig.module,
        key: String = _uiState.value.aiModuleConfig.key
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(aiModuleConfig = AIModelConfigEntity(url, module, key)) }
        }
    }

    fun changeNewsFontSize(size: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeNewsFontSize(size)
        }
    }

    fun changeBionicReadingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeBionicReadingEnabled(enabled)
        }
    }

    fun testAIService(onResponse: (String) -> Unit) = viewModelScope.launch {
        _uiState.update { it.copy(isTestLoading = true) }
        val res = networkRepo.chatService(
            url = _uiState.value.aiModuleConfig.url,
            key = _uiState.value.aiModuleConfig.key,
            data = AIModulePostEntity(
                messages = listOf(
                    AIMessageEntity(
                        content = "我通过接收信息来测试API是否正常工作，不需要思考，只需要向用户回复“测试成功”即可。",
                        role = AIRole.SYSTEM.value
                    ),
                    AIMessageEntity(
                        content = "你好",
                        role = AIRole.USER.value
                    )
                ),
                model = _uiState.value.aiModuleConfig.module
            )
        )
        _uiState.update { it.copy(isTestLoading = false) }
        res.onSuccess {
            if (it.choices.first().message.result.contains("测试成功")) {
                onResponse("测试成功")
                saveAIModelKey(_uiState.value.aiModuleConfig.key)
            } else {
                onResponse("测试失败: ${it.choices.first().message.result}")
            }
        }.onFailure {
            onResponse("测试失败: " + (it.message ?: "请检查API配置"))
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