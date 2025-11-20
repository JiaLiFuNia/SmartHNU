package com.smart.htu.screens.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.smart.htu.App.Companion.context
import com.smart.htu.api.module.AIModelEntity
import com.smart.htu.api.module.AIRole
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.api.module.ChatRequest
import com.smart.htu.api.module.Message
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.api.module.UpdateRes
import com.smart.htu.repo.AIChatNetworkRepo
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_THEME_MODE
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.CoilUtil.formatFileSize
import com.smart.htu.utils.CoilUtil.getDirectorySize
import com.smart.htu.utils.TermUtil.getCurrentTerm
import com.smart.htu.utils.ToastUtil.showToast
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
    val updateInfo: UpdateRes? = UpdateRes(0, "", UpdateEntity(), CaptchaVersionEntity()),
    val captchaLocalInfo: CaptchaVersionEntity = CaptchaVersionEntity(),
    val isUpdate: Boolean = false,
    val isCaptchaUpdate: Boolean = false,
    val aiFunctionEnabled: Boolean = false,
    val selectedAIModelIndex: Int = 0,
    val aiModelKey: String,
    val isTestLoading: Boolean = false,
    val termCode: String,
    val cacheSize: String = "计算中...",
    val loadImgEnabled: Boolean = true,
    val newsFontSize: Float = 17f,
    val bionicReadingEnabled: Boolean = true,
)

val AI_MODEL_LIST = listOf(
    AIModelEntity(
        name = "Qwen3-8B",
        model = "Qwen/Qwen3-8B"
    ),
    AIModelEntity(
        name = "DeepSeek-R1-0528-Qwen3-8B",
        model = "deepseek-ai/DeepSeek-R1-0528-Qwen3-8B"
    )
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val appNetworkRepo: AppNetworkRepo,
    private val aiChatNetworkRepo: AIChatNetworkRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    /*private val languageMap = mapOf(
        "中文(简体)" to "zh",
        "English" to "en"
    )*/

    private val _uiState = MutableStateFlow(
        SettingUiState(
            termCode = getCurrentTerm(),
            aiModelKey = "sk-spvvubnanqwdszsdcyhorkezayflhskjkqquchmxzdiiqtmx"
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

    private val selectedAIModelStateFlow = dataStoreRepo.observeSelectedAIModel()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeSelectedAIModel().first()
            }
        )

    private val aiModelKeyStateFlow = dataStoreRepo.observeAIModelKey()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAIModelKey().first()
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

    private val captchaLocalInfoStateFlow = dataStoreRepo.observeUpdateRes()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeUpdateRes().first()
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
                // _uiState.update { it.copy(aiModelKey = value) }
            }
        }
        viewModelScope.launch {
            selectedAIModelStateFlow.collect { value ->
                _uiState.update { it.copy(selectedAIModelIndex = value) }
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
            captchaLocalInfoStateFlow.collect { value ->
                _uiState.update {
                    it.copy(
                        captchaLocalInfo = value,
                        isCaptchaUpdate = value.versionCode > it.captchaLocalInfo.versionCode
                    )
                }
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
            getUpdate()
        }
        calculateCacheSize()
    }

    suspend fun getUpdate(
        onAppUpdate: ((Boolean) -> Unit) = {},
        onCaptchaModelUpdate: ((Boolean) -> Unit) = {}
    ) {
        appNetworkRepo.updateService()
            .onSuccess { res ->
                _uiState.update {
                    it.copy(
                        updateInfo = res,
                        isUpdate = res.data.isNeedUpdate
                    )
                }
                _uiState.update {
                    it.copy(
                        isCaptchaUpdate = res.captchaModelVersion.versionCode > it.captchaLocalInfo.versionCode
                    )
                }
                onAppUpdate(_uiState.value.isUpdate)
                onCaptchaModelUpdate(_uiState.value.isCaptchaUpdate)
            }
            .onFailure {
                showToast(context, "获取更新信息失败：${it.message}")
            }
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

    fun saveAIModelKey(key: String, test: Boolean = true) {
        viewModelScope.launch {
            if (test) dataStoreRepo.saveAIModelKey(key)
            else _uiState.update { it.copy(aiModelKey = key) }
        }
    }

    fun selectAIModel(index: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeSelectedAIModel(index)
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

    suspend fun testAIService(onResult: (String) -> Unit) {
        _uiState.update { it.copy(isTestLoading = true) }
        val res = aiChatNetworkRepo.chatService(
            key = _uiState.value.aiModelKey,
            data = ChatRequest(
                messages = listOf(
                    Message(
                        content = "通过接收信息来测试API是否正常工作，不需要思考，只需要向用户回复“测试成功，欢迎使用 YunAI”即可。",
                        role = AIRole.SYSTEM.value
                    ),
                    Message(
                        content = "你好",
                        role = AIRole.USER.value
                    )
                ),
                model = AI_MODEL_LIST[_uiState.value.selectedAIModelIndex].model,
                stream = false
            )
        )
        _uiState.update { it.copy(isTestLoading = false) }
        Log.i("TAG666 testAIService", "$res")
        res.onSuccess {
            val content = it.choices.firstOrNull()?.message?.content ?: ""
            if (content.contains("测试成功")) {
                onResult(content.replace("\n", ""))
                saveAIModelKey(_uiState.value.aiModelKey)
            } else {
                onResult("测试失败: $content")
            }
        }.onFailure {
            onResult("测试失败: " + (it.message ?: "请检查API配置"))
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


    suspend fun setLocalCaptchaVersion(update: CaptchaVersionEntity) {
        dataStoreRepo.saveUpdateRes(update)
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