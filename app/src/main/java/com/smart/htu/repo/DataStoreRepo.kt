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
import com.smart.htu.api.module.AIModelConfigEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.screens.application.entity.ApplicationEntity
import com.smart.htu.utils.Constants.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.utils.Term
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
    @ApplicationContext private val context: Context
) : DataStoreService {

    companion object {
        val QQ_NUMBER = stringPreferencesKey("QQ_NUMBER")
        val USERNAME = stringPreferencesKey("USERNAME")
        val LOGIN_STATE = intPreferencesKey("LOGIN_STATE")
        val LOGIN_JWC_STATE = intPreferencesKey("LOGIN_JWC_STATE")
        val TOKEN = stringPreferencesKey("TOKEN")
        val TOKEN_VALIDITY = booleanPreferencesKey("TOKEN_VALIDITY")
        val DARK_THEME = intPreferencesKey("DARK_THEME")
        val THEME_MODE = intPreferencesKey("THEME_MODE")
        val COMMON_APP_LIST = stringPreferencesKey("COMMON_APP_LIST")
        val SSO_TICKET = stringPreferencesKey("SSO_TICKET")
        val MOBILE_CODE = stringPreferencesKey("MOBILE_CODE")
        val WAITING_BORROWED_BOOK_LIST = stringPreferencesKey("WAITING_BORROWED_BOOK_LIST")
        val BLUR_EFFECT = booleanPreferencesKey("BLUR_EFFECT")
        val STUDENT_ID = stringPreferencesKey("STUDENT_ID")
        val BUILDING_ID = stringPreferencesKey("BUILDING_ID")
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
        val BIONIC_READING_ENABLED = booleanPreferencesKey("BIONIC_READING_ENABLED")
        val LOAD_IMG_ENABLED = booleanPreferencesKey("LOAD_IMG_ENABLED")

        const val DEFAULT_COOKIES = "[]"
        const val DEFAULT_MESSAGE_READ_ID = "[]"
        const val DEFAULT_THEME_MODE = 0
        const val DEFAULT_BLUR_EFFECT = false
        const val DEFAULT_TOKEN_VALIDITY = false
        const val DEFAULT_AI_FUNCTION_ENABLED = false
        const val DEFAULT_AI_MODEL_KEY = ""
        const val DEFAULT_TOKEN = ""
        const val DEFAULT_LOGIN_STATE = 0
        const val DEFAULT_DARK_THEME = 0
        const val DEFAULT_QQ_NUMBER = ""
        const val DEFAULT_USERNAME = "未登录"
        const val DEFAULT_PASSWORD = ""
        const val DEFAULT_STUDENT_ID = ""
        const val DEFAULT_BUILDING_ID = ""
        const val DEFAULT_ROOM_ID = ""
        const val DEFAULT_MOBILE_CODE = ""
        const val DEFAULT_WRITE_CALENDAR_PERMISSION_GRANTED = false
        const val DEFAULT_LOAD_IMG_ENABLED = true
        const val DEFAULT_BOOK_SEARCH_HISTORY_LIST = "[]"
        const val DEFAULT_AIR_CONDITION_USER_COOKIE = """{"shiroJID":"", "ymId":""}"""
        const val DEFAULT_AIR_CONDITION_COOKIE_TYPE = 0
        const val DEFAULT_BIONIC_READING_ENABLED = true
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

    override suspend fun changeQQNumber(qqNumber: String) {
        context.dataStore.edit { it[QQ_NUMBER] = qqNumber }
    }

    override suspend fun changeUsername(name: String) {
        context.dataStore.edit { it[USERNAME] = name }
    }

    override suspend fun changeLoginState(state: Int) {
        context.dataStore.edit { it[LOGIN_STATE] = state }
    }

    override suspend fun saveCookies(cookies: List<Cookie>) {
        context.dataStore.edit { it[SSO_TICKET] = Gson().toJson(cookies) }
    }

    override suspend fun addWaitingBorrowedBookList(waitingBorrowedBookList: List<LibraryDetailEntity>) {
        context.dataStore.edit {
            it[WAITING_BORROWED_BOOK_LIST] = Gson().toJson(waitingBorrowedBookList)
        }
    }

    override suspend fun changeBlurState(state: Boolean) {
        context.dataStore.edit { it[BLUR_EFFECT] = state }
    }

    override suspend fun saveStudentId(id: String) {
        context.dataStore.edit { it[STUDENT_ID] = id }
    }

    override suspend fun changeBuildingId(id: String) {
        context.dataStore.edit { it[BUILDING_ID] = id }
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

    override suspend fun setJWCToken(token: String) {
        context.dataStore.edit { it[TOKEN] = token }
    }

    override suspend fun addReadNoticeId(id: List<Int>) {
        context.dataStore.edit { it[NOTICE_READ_ID_LIST] = Json.encodeToString(id) }
    }

    override suspend fun setTokenValidity(valid: Boolean) {
        context.dataStore.edit { it[TOKEN_VALIDITY] = valid }
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

    override suspend fun saveAIModelConfig(config: AIModelConfigEntity) {
        context.dataStore.edit { it[AI_MODEL_KEY] = config.key }
    }

    override suspend fun changeBionicReadingEnabled(enabled: Boolean) {
        context.dataStore.edit { it[BIONIC_READING_ENABLED] = enabled }
    }

    override suspend fun changeLoadImgEnabled(enable: Boolean) {
        context.dataStore.edit { it[LOAD_IMG_ENABLED] = enable }
    }


    override fun observeThemeMode(): Flow<Int> {
        return context.dataStore.data.map { it[THEME_MODE] ?: DEFAULT_THEME_MODE }
    }

    override fun observeDarkTheme(): Flow<Int> {
        return context.dataStore.data.map { it[DARK_THEME] ?: DEFAULT_DARK_THEME }
    }

    override fun observeCommonAppList(): Flow<List<ApplicationEntity>> {
        return context.dataStore.data
            .map {
                Json.decodeFromString<List<ApplicationEntity>>(
                    it[COMMON_APP_LIST] ?: Json.encodeToString(
                        INIT_COMMON_APP_LIST
                    )
                )
            }
    }

    override fun observeQQNumber(): Flow<String> {
        return context.dataStore.data.map { it[QQ_NUMBER] ?: DEFAULT_QQ_NUMBER }
    }

    override fun observeUsername(): Flow<String> {
        return context.dataStore.data.map { it[USERNAME] ?: DEFAULT_USERNAME }
    }

    override fun observeLoginState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeCookies(): Flow<List<Cookie>> {
        return context.dataStore.data.map {
            val json = it[SSO_TICKET] ?: DEFAULT_COOKIES
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
            val json = it[WAITING_BORROWED_BOOK_LIST] ?: ""
            if (json == "") {
                emptyList()
            } else {
                val typeOfT = object : TypeToken<List<LibraryDetailEntity>>() {}.type
                Gson().fromJson(json, typeOfT)
            }
        }
    }

    override fun observerBlurState(): Flow<Boolean> {
        return context.dataStore.data.map { it[BLUR_EFFECT] ?: DEFAULT_BLUR_EFFECT }
    }

    override fun observeStudentId(): Flow<String> {
        return context.dataStore.data.map { it[STUDENT_ID] ?: DEFAULT_STUDENT_ID }
    }

    override fun observeBuildingId(): Flow<String> {
        return context.dataStore.data.map { it[BUILDING_ID] ?: DEFAULT_BUILDING_ID }
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

    override fun observeJWCToken(): Flow<String> {
        return context.dataStore.data.map { it[TOKEN] ?: DEFAULT_TOKEN }
    }

    override fun observeReadNoticeIdList(): Flow<List<Int>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<Int>>(
                it[NOTICE_READ_ID_LIST] ?: DEFAULT_MESSAGE_READ_ID
            )
        }
    }

    override fun observeTokenValidity(): Flow<Boolean> {
        return context.dataStore.data.map { it[TOKEN_VALIDITY] ?: DEFAULT_TOKEN_VALIDITY }
    }

    override fun observeGlobalTermCode(): Flow<String> {
        return context.dataStore.data.map { it[GLOBAL_TERM] ?: Term.getCurrentTerm() }
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

    override fun observeAIModelConfig(): Flow<String> {
        return context.dataStore.data.map { it[AI_MODEL_KEY] ?: DEFAULT_AI_MODEL_KEY }
    }

    override fun observeBionicReadingEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[BIONIC_READING_ENABLED] ?: DEFAULT_BIONIC_READING_ENABLED }
    }

    override fun observeLoadImgEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[LOAD_IMG_ENABLED] ?: DEFAULT_LOAD_IMG_ENABLED }
    }
}