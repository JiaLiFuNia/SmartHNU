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
import com.smart.htu.R
import com.smart.htu.api.DataStoreService
import com.smart.htu.api.module.LoginCookie
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.application.librarySearch.RentBookEntity
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.HOT_WATER_WASHER_ALIPAY_URL
import com.smart.htu.utils.Constants.Companion.SHOWER_ALIPAY_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
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
        val EDITABLE_PERSONAL_MESSAGE = stringPreferencesKey("EDITABLE_PERSONAL_MESSAGE")
        val USERNAME = stringPreferencesKey("USERNAME")
        val LOGIN_STATE = intPreferencesKey("LOGIN_STATE")
        val LOGIN_JWC_STATE = intPreferencesKey("LOGIN_JWC_STATE")
        val TOKEN = stringPreferencesKey("TOKEN")
        val DARK_THEME = intPreferencesKey("DARK_THEME")
        val DYNAMIC_COLOR = booleanPreferencesKey("DYNAMIC_COLOR")
        val COMMON_APP_LIST = stringPreferencesKey("COMMON_APP_LIST")
        val SSO_TICKET = stringPreferencesKey("SSO_TICKET")
        val RENT_BOOK_LIST = stringPreferencesKey("RENT_BOOK_LIST")
        val BLUR_EFFECT = booleanPreferencesKey("BLUR_EFFECT")
        val STUDENT_ID = stringPreferencesKey("STUDENT_ID")
        val BUILDING_ID = stringPreferencesKey("BUILDING_ID")
        val ROOM_ID = stringPreferencesKey("ROOM_ID")
        val NOTICE_READ_ID_LIST = stringPreferencesKey("NOTICE_READ_ID_LIST")
        val AIR_CONDITION_COOKIE_TYPE = intPreferencesKey("AIR_CONDITION_COOKIE_TYPE")
        val AIR_CONDITION_USER_COOKIE = stringPreferencesKey("AIR_CONDITION_USER_COOKIE")
        val BOOK_SEARCH_HISTORY_LIAT = stringPreferencesKey("BOOK_SEARCH_HISTORY_LIAT")

        const val DEFAULT_VALUE_COOKIES = "[]"
        const val DEFAULT_MESSAGE_READ_ID = "[]"
        const val DEFAULT_DYNAMIC_COLOR = true
        const val DEFAULT_BLUR_EFFECT = true
        const val DEFAULT_LOGIN_STATE = 0
        const val DEFAULT_DARK_THEME = 0
        const val DEFAULT_QQ_NUMBER = ""
        const val DEFAULT_PASSWORD = ""
        const val DEFAULT_USERNAME = "未登录"
        const val DEFAULT_STUDENT_ID = ""
        const val DEFAULT_BUILDING_ID = ""
        const val DEFAULT_ROOM_ID = ""
        const val DEFAULT_TOKEN = ""
        const val DEFAULT_BOOK_SEARCH_HISTORY_LIST = "[]"
        const val DEFAULT_AIR_CONDITION_USER_COOKIE = ""
        const val DEFAULT_AIR_CONDITION_COOKIE_TYPE = 0
        val DEFAULT_MESSAGE = PersonalMessage(
            username = DEFAULT_USERNAME,
            academic = "-",
            studentId = "-",
            phoneNumber = "-"
        )
        val ALL_APP_LIST = listOf(
            SmallCardContent(
                guestEnable = false,
                icon = R.drawable.today_24px,
                label = R.string.today_course,
            ),
            SmallCardContent(
                guestEnable = false,
                label = R.string.dorm_air_conditioner,
                icon = R.drawable.bolt_24px,
                route = Destinations.AirCondition.route
            ),
            SmallCardContent(
                guestEnable = false,
                label = R.string.classroom_search,
                icon = R.drawable.apartment_24px,
                route = Destinations.ClassroomSearch.route
            ),
            SmallCardContent(
                guestEnable = true,
                label = R.string.book_search,
                icon = R.drawable.book_4_24px,
                route = Destinations.LibrarySearch.route
            ),
            SmallCardContent(
                guestEnable = false,
                icon = R.drawable.finance_24px,
                label = R.string.course_grade,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.near_me_24px,
                label = R.string.live_service,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.bathtub_24px,
                description = "支付宝-卡博士",
                label = R.string.shower_water,
                appUrl = SHOWER_ALIPAY_URL
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.water_voc_24px,
                description = "支付宝-胖乖生活",
                label = R.string.water_washer,
                appUrl = HOT_WATER_WASHER_ALIPAY_URL
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.format_paint_24px,
                label = R.string.second_class,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.book_4_24px,
                label = R.string.textbook_select,
                route = null
            )
        )
        val INIT_COMMON_APP_LIST = listOf(
            ALL_APP_LIST[1],
            ALL_APP_LIST[2],
            ALL_APP_LIST[3]
        )
    }

    override suspend fun changeDynamicTheme(enabled: Boolean) {
        context.dataStore.edit { it[DYNAMIC_COLOR] = enabled }
    }

    override suspend fun changeDarkTheme(isDarkTheme: Int) {
        context.dataStore.edit { it[DARK_THEME] = isDarkTheme }
    }

    override suspend fun saveSmallCard(cardList: List<SmallCardContent>) {
        context.dataStore.edit { it[COMMON_APP_LIST] = Json.encodeToString(cardList) }
    }

    override suspend fun changPersonalMessage(message: String) {
        context.dataStore.edit { it[EDITABLE_PERSONAL_MESSAGE] = message }
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

    override suspend fun changeRentBookList(list: List<RentBookEntity>) {
        context.dataStore.edit { it[RENT_BOOK_LIST] = Gson().toJson(list) }
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

    override suspend fun saveAirConditionUserCookie(cookie: LoginCookie) {
        context.dataStore.edit { it[AIR_CONDITION_USER_COOKIE] = Json.encodeToString(cookie) }
    }

    override suspend fun changeBookSearchHistoryList(list: List<String>) {
        context.dataStore.edit { it[BOOK_SEARCH_HISTORY_LIAT] = Json.encodeToString(list) }
    }

    override suspend fun changeLoginJWCState(state: Int) {
        context.dataStore.edit { it[LOGIN_JWC_STATE] = state }
    }

    override suspend fun setJWCToken(token: String) {
        context.dataStore.edit { it[TOKEN] = token }
    }

    override suspend fun saveNoticeReadId(id: List<String>) {
        context.dataStore.edit { it[NOTICE_READ_ID_LIST] = Json.encodeToString(id) }
    }


    override fun observeDynamicTheme(): Flow<Boolean> {
        return context.dataStore.data
            .map { it[DYNAMIC_COLOR] ?: DEFAULT_DYNAMIC_COLOR }
    }

    override fun observeDarkTheme(): Flow<Int> {
        return context.dataStore.data
            .map { it[DARK_THEME] ?: DEFAULT_DARK_THEME }
    }

    override fun observeSmallCard(): Flow<List<SmallCardContent>> {
        return context.dataStore.data
            .map {
                Json.decodeFromString<List<SmallCardContent>>(
                    it[COMMON_APP_LIST] ?: Json.encodeToString(
                        INIT_COMMON_APP_LIST
                    )
                )
            }
    }

    override fun observePersonalMessage(): Flow<String> {
        return context.dataStore.data.map { it[EDITABLE_PERSONAL_MESSAGE] ?: "" }
    }

    override fun observeUsername(): Flow<String> {
        return context.dataStore.data.map { it[USERNAME] ?: DEFAULT_USERNAME }
    }

    override fun observeLoginState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_STATE] ?: 0 }
    }

    override fun observeCookies(): Flow<List<Cookie>> {
        return context.dataStore.data.map {
            val json = it[SSO_TICKET] ?: DEFAULT_VALUE_COOKIES
            if (json == DEFAULT_VALUE_COOKIES) {
                emptyList()
            } else {
                val typeOfT = object : TypeToken<List<Cookie>>() {}.type
                Gson().fromJson(json, typeOfT)
            }
        }
    }

    override fun observeRentBookList(): Flow<List<RentBookEntity>> {
        return context.dataStore.data.map {
            val json = it[RENT_BOOK_LIST] ?: ""
            if (json == "") {
                emptyList()
            } else {
                val typeOfT = object : TypeToken<List<RentBookEntity>>() {}.type
                Gson().fromJson(json, typeOfT)
            }
        }
    }

    override fun observerBlurState(): Flow<Boolean> {
        return context.dataStore.data.map { it[BLUR_EFFECT] ?: DEFAULT_BLUR_EFFECT }
    }

    override fun observeStudentId(): Flow<String> {
        return context.dataStore.data.map { it[STUDENT_ID] ?: "" }
    }

    override fun observeBuildingId(): Flow<String> {
        return context.dataStore.data.map { it[BUILDING_ID] ?: "" }
    }

    override fun observeRoomId(): Flow<String> {
        return context.dataStore.data.map { it[ROOM_ID] ?: "" }
    }

    override fun observeAirConditionCookieType(): Flow<Int> {
        return context.dataStore.data.map {
            it[AIR_CONDITION_COOKIE_TYPE] ?: DEFAULT_AIR_CONDITION_COOKIE_TYPE
        }
    }

    override fun observeAirConditionUserCookie(): Flow<LoginCookie> {
        return context.dataStore.data.map {
            val json = it[AIR_CONDITION_USER_COOKIE] ?: DEFAULT_AIR_CONDITION_USER_COOKIE
            if (json == "") {
                LoginCookie("", "")
            } else {
                Json.decodeFromString(json)
            }
        }
    }

    override fun observeBookSearchHistoryList(): Flow<List<String>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<String>>(
                it[BOOK_SEARCH_HISTORY_LIAT] ?: DEFAULT_BOOK_SEARCH_HISTORY_LIST
            )
        }
    }

    override fun observeLoginJWCState(): Flow<Int> {
        return context.dataStore.data.map { it[LOGIN_JWC_STATE] ?: DEFAULT_LOGIN_STATE }
    }

    override fun observeJWCToken(): Flow<String> {
        return context.dataStore.data.map { it[TOKEN] ?: DEFAULT_TOKEN }
    }

    override fun observeNoticeReadIdList(): Flow<List<String>> {
        return context.dataStore.data.map {
            Json.decodeFromString<List<String>>(
                it[NOTICE_READ_ID_LIST] ?: DEFAULT_MESSAGE_READ_ID
            )
        }
    }
}