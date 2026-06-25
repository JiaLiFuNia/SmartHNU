package com.smart.htu.screens.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.AIModelEntity
import com.smart.htu.api.module.AIModelType
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
import com.smart.htu.utils.CacheUtil
import com.smart.htu.utils.CoilUtil.formatFileSize
import com.smart.htu.utils.MIStepsUtil
import com.smart.htu.utils.TermUtil.getCurrentTerm
import com.smart.htu.utils.ToastUtil.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
import javax.inject.Inject

data class SettingUiState(
    val themeMode: Int = DEFAULT_THEME_MODE,
    val keyColorSeedIndex: Int = 0,
    val isDarkTheme: Int = 0,
    val blurEnabled: Boolean = true,
    val enableFloatingBottomBar: Boolean = false,
    val enableFloatingBottomBarBlur: Boolean = false,
    val enablePredictiveBack: Boolean = false,
    val pageScale: Float = 1f,
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
    val newsFontFamilyIndex: Int = 0,
    val homeCourseStateEnabled: Boolean = true,
    val homeFocusEnabled: Boolean = true,
    val homeFocusItemStateMap: Map<String, Boolean> = HomeFocusItem.entries.associate { it.name to it.state },
    val homeTodayCourseEnabled: Boolean = true,
    val homeShowAllTodayCourseEnabled: Boolean = true,
    val homeTodayTaskEnabled: Boolean = true,
    val homeShowAllTodayTaskEnabled: Boolean = false,
    val homeFreeClassroomEnabled: Boolean = false,
    val homeNewsEnabled: Boolean = false,
    val steps: Map<Long, Int> = mapOf()
)

val AI_MODEL_LIST = listOf(
    AIModelEntity(
        name = "Qwen3-8B",
        model = "Qwen/Qwen3-8B",
        type = AIModelType.Text
    ),
    AIModelEntity(
        name = "GLM-4.1V-9B-Thinking",
        model = "THUDM/GLM-4.1V-9B-Thinking",
        type = AIModelType.Image
    )
)

enum class HomeFocusItem(val state: Boolean) {
    WEEK(true),
    WEATHER(true),
    AIR_BOLT(true),
    STUDY_HOUR(true),
    BOOK(false),
    SCHOOL_CARD(false)
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val appNetworkRepo: AppNetworkRepo,
    private val aiChatNetworkRepo: AIChatNetworkRepo,
    private val sharedDataRepo: SharedDataRepository,
    @param:ApplicationContext private val context: Context
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

    private val newsFontFamilyStateFlow = dataStoreRepo.observeNewsFontFamily()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeNewsFontFamily().first()
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
    private val homeCourseStateEnabledFlow = dataStoreRepo.observeHomeCourseStateEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeCourseStateEnabled().first()
        })
    private val homeFocusEnabledStateFlow = dataStoreRepo.observeHomeFocusEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeFocusEnabled().first()
        })
    private val homeFocusItemStateFlow = dataStoreRepo.observeHomeFocusItemState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeFocusItemState().first()
        })
    private val homeTodayCourseEnabledStateFlow = dataStoreRepo.observeHomeTodayCourseEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeTodayCourseEnabled().first()
        })
    private val homeShowAllTodayCourseEnabledStateFlow =
        dataStoreRepo.observeHomeShowAllTodayCourseEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
                dataStoreRepo.observeHomeShowAllTodayCourseEnabled().first()
            })
    private val homeTodayTaskEnabledStateFlow = dataStoreRepo.observeHomeTodayTaskEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeTodayTaskEnabled().first()
        })
    private val homeShowAllTodayTaskEnabledStateFlow =
        dataStoreRepo.observeHomeShowAllTodayTaskEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
                dataStoreRepo.observeHomeShowAllTodayTaskEnabled().first()
            })
    private val homeFreeClassroomEnabledStateFlow = dataStoreRepo.observeHomeFreeClassroomEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeFreeClassroomEnabled().first()
        })
    private val homeNewsEnabledStateFlow = dataStoreRepo.observeHomeNewsEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeHomeNewsEnabled().first()
        })

    private val enableFloatingBottomBarStateFlow = dataStoreRepo.observeEnableFloatingBottomBar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeEnableFloatingBottomBar().first()
        })

    private val enableFloatingBottomBarBlurStateFlow =
        dataStoreRepo.observeEnableFloatingBottomBarBlur()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
                dataStoreRepo.observeEnableFloatingBottomBarBlur().first()
            })

    private val enablePredictiveBackStateFlow = dataStoreRepo.observeEnablePredictiveBack()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observeEnablePredictiveBack().first()
        })

    private val pageScaleStateFlow = dataStoreRepo.observePageScale()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), runBlocking {
            dataStoreRepo.observePageScale().first()
        })

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
            newsFontFamilyStateFlow.collect { value ->
                _uiState.update { it.copy(newsFontFamilyIndex = value) }
            }
        }
        viewModelScope.launch {
            homeCourseStateEnabledFlow.collect { value ->
                _uiState.update { it.copy(homeCourseStateEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeFocusEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeFocusEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeFocusItemStateFlow.collect { value ->
                _uiState.update {
                    it.copy(
                        homeFocusItemStateMap = value
                    )
                }
            }
        }
        viewModelScope.launch {
            homeTodayCourseEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeTodayCourseEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeShowAllTodayCourseEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeShowAllTodayCourseEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeTodayTaskEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeTodayTaskEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeShowAllTodayTaskEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeShowAllTodayTaskEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeFreeClassroomEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeFreeClassroomEnabled = value) }
            }
        }
        viewModelScope.launch {
            homeNewsEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(homeNewsEnabled = value) }
            }
        }
        viewModelScope.launch {
            MIStepsUtil.queryTodaySteps(context).onSuccess { res ->
                _uiState.update { it.copy(steps = res) }
            }
        }
        viewModelScope.launch {
            enableFloatingBottomBarStateFlow.collect { value ->
                _uiState.update { it.copy(enableFloatingBottomBar = value) }
            }
        }
        viewModelScope.launch {
            enableFloatingBottomBarBlurStateFlow.collect { value ->
                _uiState.update { it.copy(enableFloatingBottomBarBlur = value) }
            }
        }
        viewModelScope.launch {
            enablePredictiveBackStateFlow.collect { value ->
                _uiState.update { it.copy(enablePredictiveBack = value) }
            }
        }
        viewModelScope.launch {
            pageScaleStateFlow.collect { value ->
                _uiState.update { it.copy(pageScale = value) }
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
            sharedDataRepo.currentTermCode
                .collect { value ->
                    _uiState.update { it.copy(termCode = value) }
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
                showToast(context, "${it.message}")
            }
    }

    fun insertSteps(newStep: Int, onResult: (String) -> Unit) {
        val now = System.currentTimeMillis()
        val tenMinutesAgo = now - 10 * 60 * 1000

        MIStepsUtil.insertStepsWithSystemUser(
            beginTime = tenMinutesAgo,
            endTime = now,
            mode = 2,
            steps = newStep
        ).onSuccess {
            viewModelScope.launch {
                MIStepsUtil.queryTodaySteps(context).onSuccess { res ->
                    _uiState.update { it.copy(steps = res) }
                }
                onResult("已修改为${_uiState.value.steps.values.sum()}")
            }
        }.onFailure {
            onResult(it.message.toString())
            Log.e("TAG666 insert", it.message.toString())
        }
    }

    fun changeDynamicTheme(mode: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeThemeMode(mode)
        }
    }

    fun changKeyColorSeedIndex(index: Int) {
        _uiState.update { it.copy(keyColorSeedIndex = index) }
    }

    fun changeBlurEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeBlurState(enabled)
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

    fun changeNewsFontFamily(index: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeNewsFontFamily(index)
        }
    }

    fun changeHomeCourseStateEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeCourseStateEnabled(enabled) }
    }

    fun changeHomeFocusEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeFocusEnabled(enabled) }
    }

    fun changeHomeFocusItemState(
        item: String,
        enabled: Boolean
    ) {
        viewModelScope.launch {
            dataStoreRepo.changeHomeFocusItemState(item, enabled)
        }
    }

    fun changeHomeTodayCourseEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeTodayCourseEnabled(enabled) }
    }

    fun changeHomeShowAllTodayCourseEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeShowAllTodayCourseEnabled(enabled) }
    }

    fun changeHomeTodayTaskEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeTodayTaskEnabled(enabled) }
    }

    fun changeHomeShowAllTodayTaskEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeShowAllTodayTaskEnabled(enabled) }
    }

    fun changeHomeFreeClassroomEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeFreeClassroomEnabled(enabled) }
    }

    fun changeHomeNewsEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeHomeNewsEnabled(enabled) }
    }

    fun changeEnableFloatingBottomBar(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeEnableFloatingBottomBar(enabled) }
    }

    fun changeEnableFloatingBottomBarBlur(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeEnableFloatingBottomBarBlur(enabled) }
    }

    fun changeEnablePredictiveBack(enabled: Boolean) {
        viewModelScope.launch { dataStoreRepo.changeEnablePredictiveBack(enabled) }
    }

    fun changePageScale(scale: Float) {
        viewModelScope.launch { dataStoreRepo.changePageScale(scale) }
    }

    suspend fun testAIService(onResult: (String) -> Unit) {
        _uiState.update { it.copy(isTestLoading = true) }
        val res = aiChatNetworkRepo.chatService(
            key = _uiState.value.aiModelKey,
            data = ChatRequest(
                messages = listOf(
                    Message(
                        content = "通过接收信息来测试API是否正常工作，需要向用户回复“测试成功，欢迎使用 YunAI”。",
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
                    val totalSize = CacheUtil.getCacheSize(context)
                    val formattedSize = formatFileSize(totalSize)
                    _uiState.update { it.copy(cacheSize = formattedSize) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _uiState.update { it.copy(cacheSize = "计算失败") }
                }
            }
        }
    }

    fun clearCache(
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                CacheUtil.clear(context)
            }
            result.onSuccess {
                onResult("清理成功")
            }.onFailure {
                onResult("清理失败: ${it.message}")
            }
            calculateCacheSize()
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