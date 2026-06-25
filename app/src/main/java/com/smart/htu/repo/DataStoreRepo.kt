package com.smart.htu.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smart.htu.api.DataStoreService
import com.smart.htu.api.module.ACCookie
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseItemEntity
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.api.module.SCHourEntity
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.courseTable.CourseTableSettings
import com.smart.htu.screens.application.physicalTest.PhysicalTestScore
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.screens.setting.HomeFocusItem
import com.smart.htu.utils.Constants.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.utils.TermUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("App")

@Singleton
class DataStoreRepo @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DataStoreService {

    companion object {
        val USERNAME = stringPreferencesKey("USERNAME")
        val LOGIN_STATE = intPreferencesKey("LOGIN_STATE")
        val LOGIN_JWC_STATE = intPreferencesKey("LOGIN_JWC_STATE")
        val LOGIN_SC_STATE = intPreferencesKey("LOGIN_SC_STATE")
        val LOGIN_LIB_STATE = intPreferencesKey("LOGIN_LIB_STATE")
        val SECOND_CLASS_SID = stringPreferencesKey("SECOND_CLASS_SID")
        val LIB_META_SESSION = stringPreferencesKey("LIB_SESSION")
        val TOKEN = stringPreferencesKey("TOKEN")
        val DARK_THEME = intPreferencesKey("DARK_THEME")
        val THEME_MODE = intPreferencesKey("THEME_MODE")
        val COMMON_APP_LIST = stringPreferencesKey("COMMON_APP_LIST")
        val AUTH_COOKIE = stringPreferencesKey("AUTH_COOKIE")
        val MOBILE_CODE = stringPreferencesKey("MOBILE_CODE")
        val WAITING_BORROWED_BOOK_LIST = stringPreferencesKey("WAITING_BORROWED_BOOK_LIST")
        val BLUR_EFFECT = booleanPreferencesKey("BLUR_EFFECT")
        val STUDENT_ID = stringPreferencesKey("STUDENT_ID")
        val DORM_ROOM_ID = stringPreferencesKey("DORM_ROOM_ID")
        val ROOM_ID = stringPreferencesKey("ROOM_ID")
        val GLOBAL_TERM = stringPreferencesKey("GLOBAL_TERM")
        val NOTICE_READ_ID_LIST = stringPreferencesKey("NOTICE_READ_ID_LIST")
        val AIR_CONDITION_COOKIE_TYPE = intPreferencesKey("AIR_CONDITION_COOKIE_TYPE")
        val AIR_CONDITION_USER_COOKIE = stringPreferencesKey("AIR_CONDITION_USER_COOKIE")
        val BOOK_SEARCH_HISTORY_LIST = stringPreferencesKey("BOOK_SEARCH_HISTORY_LIST")
        val IS_WRITE_CALENDAR_PERMISSION_GRANTED =
            booleanPreferencesKey("IS_WRITE_CALENDAR_PERMISSION_GRANTED")
        val AI_FUNCTION_ENABLED = booleanPreferencesKey("AI_FUNCTION_ENABLED")
        val AI_MODEL_KEY = stringPreferencesKey("AI_MODEL_KEY")
        val SELECTED_AI_MODEL = intPreferencesKey("SELECTED_AI_MODEL")
        val LOAD_IMG_ENABLED = booleanPreferencesKey("LOAD_IMG_ENABLED")
        val NEWS_HISTORY_LIST = stringPreferencesKey("NEWS_HISTORY_LIST")
        val NEWS_FAVORITE_LIST = stringPreferencesKey("NEWS_FAVORITE_LIST")
        val NEWS_FONT_SIZE = intPreferencesKey("NEWS_FONT_SIZE")
        val NEWS_FONT_FAMILY = intPreferencesKey("NEWS_FONT_FAMILY")
        val EXAM_SCHEDULE_LIST = stringPreferencesKey("EXAM_SCHEDULE_LIST")
        val PHYSICAL_TEST_SCORE_LIST = stringPreferencesKey("PHYSICAL_TEST_SCORE_LIST")
        val COURSE_TABLE_SETTINGS = stringPreferencesKey("COURSE_TABLE_SETTINGS")
        val WEEKEND_COURSE_SHOW_STATE = booleanPreferencesKey("WEEKEND_COURSE_SHOW_STATE")
        val SECOND_CLASS_DATA = stringPreferencesKey("SECOND_CLASS_DATA")
        val UPDATE_RES = stringPreferencesKey("UPDATE_RES")
        val TARGET_SELECT_COURSE_LIST = stringPreferencesKey("TARGET_SELECT_COURSE_LIST")
        val TASK_LIST = stringPreferencesKey("TASK_LIST")
        val COURSE_TABLE_DATA = stringPreferencesKey("COURSE_TABLE_DATA")
        val HOME_COURSE_STATE_ENABLED = booleanPreferencesKey("HOME_COURSE_STATE_ENABLED")
        val HOME_FOCUS_ENABLED = booleanPreferencesKey("HOME_FOCUS_ENABLED")
        val HOME_FOCUS_ITEM_STATE = stringPreferencesKey("HOME_FOCUS_ITEM_STATE")
        val HOME_TODAY_COURSE_ENABLED = booleanPreferencesKey("HOME_TODAY_COURSE_ENABLED")
        val HOME_SHOW_ALL_TODAY_COURSE_ENABLED =
            booleanPreferencesKey("HOME_SHOW_ALL_TODAY_COURSE_ENABLED")
        val HOME_TODAY_TASK_ENABLED = booleanPreferencesKey("HOME_TODAY_TASK_ENABLED")
        val HOME_SHOW_ALL_TODAY_TASK_ENABLED =
            booleanPreferencesKey("HOME_SHOW_ALL_TODAY_TASK_ENABLED")
        val HOME_FREE_CLASSROOM_ENABLED = booleanPreferencesKey("HOME_FREE_CLASSROOM_ENABLED")
        val HOME_NEWS_ENABLED = booleanPreferencesKey("HOME_NEWS_ENABLED")
        val ENABLE_FLOATING_BOTTOM_BAR = booleanPreferencesKey("ENABLE_FLOATING_BOTTOM_BAR")
        val ENABLE_FLOATING_BOTTOM_BAR_BLUR =
            booleanPreferencesKey("ENABLE_FLOATING_BOTTOM_BAR_BLUR")
        val ENABLE_PREDICTIVE_BACK = booleanPreferencesKey("ENABLE_PREDICTIVE_BACK")
        val PAGE_SCALE = stringPreferencesKey("PAGE_SCALE")

        const val DEFAULT_EMPTY_LIST = "[]"
        const val DEFAULT_EMPTY_STRING = ""
        const val DEFAULT_EMPTY_MAP = "{}"
        const val DEFAULT_COOKIES = "[]"
        const val DEFAULT_MESSAGE_READ_ID = "[]"
        const val DEFAULT_THEME_MODE = 0
        const val DEFAULT_BLUR_EFFECT = true
        const val DEFAULT_AI_FUNCTION_ENABLED = false
        const val DEFAULT_AI_MODEL_KEY = ""
        const val DEFAULT_TOKEN = ""
        const val DEFAULT_LOGIN_STATE = 0
        const val DEFAULT_DARK_THEME = 0
        const val DEFAULT_USERNAME = "未登录"
        const val DEFAULT_PASSWORD = ""
        const val DEFAULT_STUDENT_ID = ""
        const val DEFAULT_BUILDING_ID = ""
        const val DEFAULT_ROOM_ID = ""
        const val DEFAULT_MOBILE_CODE = ""
        const val DEFAULT_PHYSICAL_TEST_SCORE_LIST = "{}"
        const val DEFAULT_WRITE_CALENDAR_PERMISSION_GRANTED = false
        const val DEFAULT_LOAD_IMG_ENABLED = true
        const val DEFAULT_BOOK_SEARCH_HISTORY_LIST = "[]"
        const val DEFAULT_EXAM_SCHEDULE_LIST = "[]"
        const val DEFAULT_NEWS_HISTORY_LIST = "[]"
        const val DEFAULT_NEWS_FAVORITE_LIST = "[]"
        const val DEFAULT_NEWS_FONT_SIZE = 17
        const val DEFAULT_NEWS_FONT_FAMILY = 0
        const val DEFAULT_AIR_CONDITION_USER_COOKIE = """{"shiroJID":"", "ymId":""}"""
        const val DEFAULT_AIR_CONDITION_COOKIE_TYPE = 0
        const val DEFAULT_HOME_COURSE_STATE_ENABLED = true
        const val DEFAULT_HOME_FOCUS_ENABLED = true
        const val DEFAULT_HOME_TODAY_COURSE_ENABLED = true
        const val DEFAULT_HOME_SHOW_ALL_TODAY_COURSE_ENABLED = true
        const val DEFAULT_HOME_TODAY_TASK_ENABLED = false
        const val DEFAULT_HOME_SHOW_ALL_TODAY_TASK_ENABLED = false
        const val DEFAULT_HOME_FREE_CLASSROOM_ENABLED = false
        const val DEFAULT_HOME_NEWS_ENABLED = false
        const val DEFAULT_ENABLE_FLOATING_BOTTOM_BAR = false
        const val DEFAULT_ENABLE_FLOATING_BOTTOM_BAR_BLUR = false
        const val DEFAULT_ENABLE_PREDICTIVE_BACK = false
        const val DEFAULT_PAGE_SCALE = "1.0"
    }

    override suspend fun changeThemeMode(enabled: Int) {
        context.dataStore.edit { it[THEME_MODE] = enabled }
    }

    override suspend fun changeDarkTheme(isDarkTheme: Int) {
        context.dataStore.edit { it[DARK_THEME] = isDarkTheme }
    }

    override suspend fun setCommonApp(appList: List<ApplicationEntity>) {
        context.dataStore.edit { it[COMMON_APP_LIST] = Json.encodeToString(appList) }
    }

    override suspend fun changeUsername(name: String) {
        context.dataStore.edit { it[USERNAME] = name }
    }

    override suspend fun changeLoginState(state: Int) {
        context.dataStore.edit { it[LOGIN_STATE] = state }
    }

    override suspend fun saveAuthCookie(cookie: List<Cookie>) {
        context.dataStore.edit { it[AUTH_COOKIE] = Gson().toJson(cookie) }
    }

    override suspend fun addWaitingBorrowedBookList(waitingBorrowedBookList: List<LibraryDetailEntity>) {
        context.dataStore.edit {
            it[WAITING_BORROWED_BOOK_LIST] = Json.encodeToString(waitingBorrowedBookList)
        }
    }

    override suspend fun changeBlurState(state: Boolean) {
        context.dataStore.edit { it[BLUR_EFFECT] = state }
    }

    override suspend fun saveStudentId(id: String) {
        context.dataStore.edit { it[STUDENT_ID] = id }
    }

    override suspend fun saveDormRoomId(id: String) {
        context.dataStore.edit { it[DORM_ROOM_ID] = id }
    }

    override suspend fun changeRoomId(room: String) {
        context.dataStore.edit { it[ROOM_ID] = room }
    }

    override suspend fun saveAirConditionCookieType(type: Int) {
        context.dataStore.edit { it[AIR_CONDITION_COOKIE_TYPE] = type }
    }

    override suspend fun saveAirConditionUserCookie(cookie: ACCookie) {
        context.dataStore.edit { it[AIR_CONDITION_USER_COOKIE] = Json.encodeToString(cookie) }
    }

    override suspend fun changeBookSearchHistoryList(list: List<String>) {
        context.dataStore.edit { it[BOOK_SEARCH_HISTORY_LIST] = Json.encodeToString(list) }
    }

    override suspend fun changeLoginJWCState(state: Int) {
        context.dataStore.edit { it[LOGIN_JWC_STATE] = state }
    }

    override suspend fun changeLoginSCState(state: Int) {
        context.dataStore.edit { it[LOGIN_SC_STATE] = state }
    }

    override suspend fun saveSecondClassSid(sid: String) {
        context.dataStore.edit { it[SECOND_CLASS_SID] = sid }
    }

    override suspend fun setJWCToken(token: String) {
        context.dataStore.edit { it[TOKEN] = token }
    }

    override suspend fun addReadNoticeId(id: List<String>) {
        context.dataStore.edit { it[NOTICE_READ_ID_LIST] = Json.encodeToString(id) }
    }

    override suspend fun setGlobalTermCode(term: String) {
        context.dataStore.edit { it[GLOBAL_TERM] = term }
    }

    override suspend fun saveMobileCode(mobileCode: String) {
        context.dataStore.edit { it[MOBILE_CODE] = mobileCode }
    }

    override suspend fun setIsWriteCalendarPermissionGranted(enable: Boolean) {
        context.dataStore.edit { it[IS_WRITE_CALENDAR_PERMISSION_GRANTED] = enable }
    }

    override suspend fun changeAIFunctionEnabled(enabled: Boolean) {
        context.dataStore.edit { it[AI_FUNCTION_ENABLED] = enabled }
    }

    override suspend fun changeSelectedAIModel(index: Int) {
        context.dataStore.edit { it[SELECTED_AI_MODEL] = index }
    }

    override suspend fun saveAIModelKey(key: String) {
        context.dataStore.edit { it[AI_MODEL_KEY] = key }
    }

    override suspend fun changeLoadImgEnabled(enable: Boolean) {
        context.dataStore.edit { it[LOAD_IMG_ENABLED] = enable }
    }

    override suspend fun changeNewsHistoryList(newsItem: NewsMarkEntity) {
        context.dataStore.edit {
            val currentList = Json.decodeFromString<List<NewsMarkEntity>>(
                it[NEWS_HISTORY_LIST] ?: DEFAULT_NEWS_HISTORY_LIST
            ).toMutableList()
            if (currentList.map { it.title }.contains(newsItem.title))
                currentList.removeIf { it.title == newsItem.title }
            currentList.add(0, newsItem)
            if (currentList.size > 100) {
                currentList.removeLastOrNull()
            }
            it[NEWS_HISTORY_LIST] = Json.encodeToString(currentList)
        }
    }

    override suspend fun addNewsFavoriteList(newsList: List<NewsMarkEntity>) {
        context.dataStore.edit {
            it[NEWS_FAVORITE_LIST] = Json.encodeToString(newsList)
        }
    }

    override suspend fun changeNewsFontSize(size: Int) {
        context.dataStore.edit { it[NEWS_FONT_SIZE] = size }
    }

    override suspend fun changeNewsFontFamily(index: Int) {
        context.dataStore.edit { it[NEWS_FONT_FAMILY] = index }
    }

    override suspend fun saveExamScheduleList(examList: List<ExamEntity>) {
        context.dataStore.edit { it[EXAM_SCHEDULE_LIST] = Json.encodeToString(examList) }
    }

    override suspend fun savePhysicalTestScoreList(score: Map<Int, PhysicalTestScore>) {
        context.dataStore.edit { it[PHYSICAL_TEST_SCORE_LIST] = Json.encodeToString(score) }
    }

    override suspend fun setCourseTableSettings(data: CourseTableSettings) {
        context.dataStore.edit { it[COURSE_TABLE_SETTINGS] = Json.encodeToString(data) }
    }

    override suspend fun saveSecondClassData(data: SCHourEntity) {
        context.dataStore.edit { it[SECOND_CLASS_DATA] = Json.encodeToString(data) }
    }

    override suspend fun saveUpdateRes(result: CaptchaVersionEntity) {
        context.dataStore.edit { it[UPDATE_RES] = Json.encodeToString(result) }
    }

    override suspend fun changeLoginLibraryState(state: Int) {
        context.dataStore.edit { it[LOGIN_LIB_STATE] = state }
    }

    override suspend fun saveTargetCourseList(list: List<CourseItemEntity>) {
        context.dataStore.edit { it[TARGET_SELECT_COURSE_LIST] = Json.encodeToString(list) }
    }

    override suspend fun saveTaskList(taskList: List<TaskEntity>) {
        context.dataStore.edit { it[TASK_LIST] = Json.encodeToString(taskList) }
    }

    override suspend fun saveCourseTableData(data: Map<String, List<List<List<CourseEntity>>>>) {
        context.dataStore.edit {
            it[COURSE_TABLE_DATA] = Json.encodeToString(data)
        }
    }

    override suspend fun changeHomeCourseStateEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_COURSE_STATE_ENABLED] = enabled }
    }

    override suspend fun changeHomeFocusEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_FOCUS_ENABLED] = enabled }
    }

    override suspend fun changeHomeFocusItemState(item: String, state: Boolean) {
        context.dataStore.edit {
            val currentState = Json.decodeFromString<Map<String, Boolean>>(
                it[HOME_FOCUS_ITEM_STATE] ?: Json.encodeToString(HomeFocusItem.entries.associate { it.name to it.state })
            ).toMutableMap()
            currentState[item] = state
            it[HOME_FOCUS_ITEM_STATE] = Json.encodeToString(currentState)
        }
    }

    override suspend fun changeHomeTodayCourseEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_TODAY_COURSE_ENABLED] = enabled }
    }

    override suspend fun changeHomeShowAllTodayCourseEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_SHOW_ALL_TODAY_COURSE_ENABLED] = enabled }
    }

    override suspend fun changeHomeTodayTaskEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_TODAY_TASK_ENABLED] = enabled }
    }

    override suspend fun changeHomeShowAllTodayTaskEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_SHOW_ALL_TODAY_TASK_ENABLED] = enabled }
    }

    override suspend fun changeHomeFreeClassroomEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_FREE_CLASSROOM_ENABLED] = enabled }
    }

    override suspend fun changeHomeNewsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HOME_NEWS_ENABLED] = enabled }
    }

    override suspend fun changeEnableFloatingBottomBar(enabled: Boolean) {
        context.dataStore.edit { it[ENABLE_FLOATING_BOTTOM_BAR] = enabled }
    }

    override suspend fun changeEnableFloatingBottomBarBlur(enabled: Boolean) {
        context.dataStore.edit { it[ENABLE_FLOATING_BOTTOM_BAR_BLUR] = enabled }
    }

    override suspend fun changeEnablePredictiveBack(enabled: Boolean) {
        context.dataStore.edit { it[ENABLE_PREDICTIVE_BACK] = enabled }
    }

    override suspend fun changePageScale(scale: Float) {
        context.dataStore.edit { it[PAGE_SCALE] = scale.toString() }
    }


    override fun observeThemeMode(): Flow<Int> {
        return context.dataStore.data.map { it[THEME_MODE] ?: DEFAULT_THEME_MODE }
    }

    override fun observeDarkTheme(): Flow<Int> {
        return context.dataStore.data.map { it[DARK_THEME] ?: DEFAULT_DARK_THEME }
    }

    override fun observeCommonAppList(): Flow<List<ApplicationEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<ApplicationEntity>>(
                it[COMMON_APP_LIST] ?: Json.encodeToString(
                    INIT_COMMON_APP_LIST
                )
            )
        }
    }

    override fun observeUsername(): Flow<String> {
        return context.dataStore.data.map { it[USERNAME] ?: DEFAULT_USERNAME }
    }

    override fun observeLoginState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeAuthCookie(): Flow<List<Cookie>> {
        return context.dataStore.data.map {
            val json = it[AUTH_COOKIE] ?: DEFAULT_COOKIES
            if (json == DEFAULT_COOKIES) {
                emptyList()
            } else {
                val typeOfT = object : TypeToken<List<Cookie>>() {}.type
                Gson().fromJson(json, typeOfT)
            }
        }
    }

    override fun observeWaitingBorrowedBookList(): Flow<List<LibraryDetailEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<LibraryDetailEntity>>(
                it[WAITING_BORROWED_BOOK_LIST] ?: "[]"
            )
        }
    }

    override fun observerBlurState(): Flow<Boolean> {
        return context.dataStore.data.map { it[BLUR_EFFECT] ?: DEFAULT_BLUR_EFFECT }
    }

    override fun observeStudentId(): Flow<String> {
        return context.dataStore.data.map { it[STUDENT_ID] ?: DEFAULT_STUDENT_ID }
    }

    override fun observeBuildingId(): Flow<String> {
        return context.dataStore.data.map { it[DORM_ROOM_ID] ?: DEFAULT_BUILDING_ID }
    }

    override fun observeRoomId(): Flow<String> {
        return context.dataStore.data.map { it[ROOM_ID] ?: DEFAULT_ROOM_ID }
    }

    override fun observeAirConditionCookieType(): Flow<Int> {
        return context.dataStore.data.map {
            it[AIR_CONDITION_COOKIE_TYPE] ?: DEFAULT_AIR_CONDITION_COOKIE_TYPE
        }
    }

    override fun observeAirConditionUserCookie(): Flow<ACCookie> {
        return context.dataStore.data.map {
            Json.decodeFromString<ACCookie>(
                it[AIR_CONDITION_USER_COOKIE] ?: DEFAULT_AIR_CONDITION_USER_COOKIE
            )
        }
    }

    override fun observeBookSearchHistoryList(): Flow<List<String>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<String>>(
                it[BOOK_SEARCH_HISTORY_LIST] ?: DEFAULT_BOOK_SEARCH_HISTORY_LIST
            )
        }
    }

    override fun observeLoginJWCState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_JWC_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeLoginSCState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_SC_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeSecondClassSid(): Flow<String> {
        return context.dataStore.data.map { it[SECOND_CLASS_SID] ?: "" }
    }

    override fun observeJWCToken(): Flow<String> {
        return context.dataStore.data.map { it[TOKEN] ?: DEFAULT_TOKEN }
    }

    override fun observeReadNoticeIdList(): Flow<List<String>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<String>>(
                it[NOTICE_READ_ID_LIST] ?: DEFAULT_MESSAGE_READ_ID
            )
        }
    }

    override fun observeGlobalTermCode(): Flow<String> {
        return context.dataStore.data.map { it[GLOBAL_TERM] ?: TermUtil.getCurrentTerm() }
    }

    override fun observeMobileCode(): Flow<String> {
        return context.dataStore.data.map { it[MOBILE_CODE] ?: DEFAULT_MOBILE_CODE }
    }

    override fun observeIsWriteCalendarPermissionGranted(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[IS_WRITE_CALENDAR_PERMISSION_GRANTED] ?: DEFAULT_WRITE_CALENDAR_PERMISSION_GRANTED
        }
    }

    override fun observeAIFunctionEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[AI_FUNCTION_ENABLED] ?: DEFAULT_AI_FUNCTION_ENABLED }
    }

    override fun observeSelectedAIModel(): Flow<Int> {
        return context.dataStore.data.map { it[SELECTED_AI_MODEL] ?: 0 }
    }

    override fun observeAIModelKey(): Flow<String> {
        return context.dataStore.data.map { it[AI_MODEL_KEY] ?: DEFAULT_AI_MODEL_KEY }
    }

    override fun observeLoadImgEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[LOAD_IMG_ENABLED] ?: DEFAULT_LOAD_IMG_ENABLED }
    }

    override fun observeNewsHistoryList(): Flow<List<NewsMarkEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<NewsMarkEntity>>(
                it[NEWS_HISTORY_LIST] ?: DEFAULT_NEWS_HISTORY_LIST
            )
        }
    }

    override fun observeNewsFavoriteList(): Flow<List<NewsMarkEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<NewsMarkEntity>>(
                it[NEWS_FAVORITE_LIST] ?: DEFAULT_NEWS_FAVORITE_LIST
            )
        }
    }

    override fun observeNewsFontSize(): Flow<Int> {
        return context.dataStore.data.map { it[NEWS_FONT_SIZE] ?: DEFAULT_NEWS_FONT_SIZE }
    }

    override fun observeNewsFontFamily(): Flow<Int> {
        return context.dataStore.data.map { it[NEWS_FONT_FAMILY] ?: DEFAULT_NEWS_FONT_FAMILY }
    }

    override fun observeExamScheduleList(): Flow<List<ExamEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<ExamEntity>>(
                it[EXAM_SCHEDULE_LIST] ?: DEFAULT_EXAM_SCHEDULE_LIST
            )
        }
    }

    override fun observePhysicalTestScoreList(): Flow<Map<Int, PhysicalTestScore>> {
        return context.dataStore.data.map {
            Json.decodeFromString<Map<Int, PhysicalTestScore>>(
                it[PHYSICAL_TEST_SCORE_LIST] ?: DEFAULT_PHYSICAL_TEST_SCORE_LIST
            )
        }
    }

    override fun observeCourseTableSettings(): Flow<CourseTableSettings> {
        return context.dataStore.data.map {
            Json.decodeFromString<CourseTableSettings>(
                it[COURSE_TABLE_SETTINGS] ?: Json.encodeToString(CourseTableSettings())
            )
        }
    }

    override fun observeSecondClassData(): Flow<SCHourEntity?> {
        return context.dataStore.data.map {
            val json = it[SECOND_CLASS_DATA]
            if (json != null) Json.decodeFromString<SCHourEntity>(json)
            else null
        }
    }

    override fun observeUpdateRes(): Flow<CaptchaVersionEntity> {
        return context.dataStore.data.map {
            Json.decodeFromString<CaptchaVersionEntity>(
                it[UPDATE_RES] ?: Json.encodeToString(CaptchaVersionEntity())
            )
        }
    }

    override fun observeLoginLibraryState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_LIB_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeLibrarySession(): Flow<String> {
        return context.dataStore.data.map { it[LIB_META_SESSION] ?: "" }
    }

    override fun observeTargetCourseList(): Flow<List<CourseItemEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<CourseItemEntity>>(
                it[TARGET_SELECT_COURSE_LIST] ?: DEFAULT_EMPTY_LIST
            )
        }
    }

    override fun observeTaskList(): Flow<List<TaskEntity>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<TaskEntity>>(
                it[TASK_LIST] ?: DEFAULT_EMPTY_LIST
            )
        }
    }

    override fun observeCourseTableData(): Flow<Map<String, List<List<List<CourseEntity>>>>> {
        return context.dataStore.data.map {
            Json.decodeFromString<Map<String, List<List<List<CourseEntity>>>>>(
                it[COURSE_TABLE_DATA] ?: DEFAULT_EMPTY_MAP
            )
        }
    }

    override fun observeHomeCourseStateEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_COURSE_STATE_ENABLED] ?: DEFAULT_HOME_COURSE_STATE_ENABLED
        }
    }

    override fun observeHomeFocusEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[HOME_FOCUS_ENABLED] ?: DEFAULT_HOME_FOCUS_ENABLED }
    }

    override fun observeHomeFocusItemState(): Flow<Map<String, Boolean>> {
        return context.dataStore.data.map {
            Json.decodeFromString<Map<String, Boolean>>(
                it[HOME_FOCUS_ITEM_STATE]
                    ?: Json.encodeToString(HomeFocusItem.entries.associate { it.name to it.state })
            )
        }
    }

    override fun observeHomeTodayCourseEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_TODAY_COURSE_ENABLED] ?: DEFAULT_HOME_TODAY_COURSE_ENABLED
        }
    }

    override fun observeHomeShowAllTodayCourseEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_SHOW_ALL_TODAY_COURSE_ENABLED] ?: DEFAULT_HOME_SHOW_ALL_TODAY_COURSE_ENABLED
        }
    }

    override fun observeHomeTodayTaskEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_TODAY_TASK_ENABLED] ?: DEFAULT_HOME_TODAY_TASK_ENABLED
        }
    }

    override fun observeHomeShowAllTodayTaskEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_SHOW_ALL_TODAY_TASK_ENABLED] ?: DEFAULT_HOME_SHOW_ALL_TODAY_TASK_ENABLED
        }
    }

    override fun observeHomeFreeClassroomEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[HOME_FREE_CLASSROOM_ENABLED] ?: DEFAULT_HOME_FREE_CLASSROOM_ENABLED
        }
    }

    override fun observeHomeNewsEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[HOME_NEWS_ENABLED] ?: DEFAULT_HOME_NEWS_ENABLED }
    }

    override fun observeEnableFloatingBottomBar(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[ENABLE_FLOATING_BOTTOM_BAR] ?: DEFAULT_ENABLE_FLOATING_BOTTOM_BAR
        }
    }

    override fun observeEnableFloatingBottomBarBlur(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[ENABLE_FLOATING_BOTTOM_BAR_BLUR] ?: DEFAULT_ENABLE_FLOATING_BOTTOM_BAR_BLUR
        }
    }

    override fun observeEnablePredictiveBack(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[ENABLE_PREDICTIVE_BACK] ?: DEFAULT_ENABLE_PREDICTIVE_BACK
        }
    }

    override fun observePageScale(): Flow<Float> {
        return context.dataStore.data.map { (it[PAGE_SCALE] ?: DEFAULT_PAGE_SCALE).toFloat() }
    }


}