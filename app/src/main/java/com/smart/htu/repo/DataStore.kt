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
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.login.EditablePersonalMessage
import com.smart.htu.screens.navigation.Destinations
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
        val LOGIN_STATE = intPreferencesKey("LOGIN_STATE")
        val DARK_THEME = intPreferencesKey("DARK_THEME")
        val DYNAMIC_COLOR = booleanPreferencesKey("DYNAMIC_COLOR")
        val COMMON_APP_LIST = stringPreferencesKey("COMMON_APP_LIST")
        val COOKIES = stringPreferencesKey("COOKIES")

        const val DEFAULT_VALUE_COOKIES = "[]"
        const val DEFAULT_DYNAMIC_COLOR = true
        const val DEFAULT_DARK_THEME = 0
        val DEFAULT_EDITABLE_PERSONAL_MESSAGE = EditablePersonalMessage("新用户", "")
        val INIT_COMMON_APP_LIST = listOf(
            SmallCardContent(
                label = R.string.classroom_search,
                icon = R.drawable.apartment_24px,
                route = Destinations.ClassroomSearch.route
            ),
            SmallCardContent(
                icon = R.drawable.today_24px,
                description = "没有课程",
                label = R.string.today_course,
                route = ""
            ),
            SmallCardContent(
                label = R.string.dorm_air_conditioner,
                icon = R.drawable.bolt_24px,
                description = "电费剩余 00 度",
                url = "https://houqin.htu.edu.cn/one/plan/"
            )
        )
    }

    override suspend fun changeDynamicTheme(enabled: Boolean) {
        context.dataStore.edit {
            it[DYNAMIC_COLOR] = enabled
        }
    }

    override suspend fun changeDarkTheme(isDarkTheme: Int) {
        context.dataStore.edit {
            it[DARK_THEME] = isDarkTheme
        }
    }

    override suspend fun saveSmallCard(cardList: List<SmallCardContent>) {
        context.dataStore.edit {
            it[COMMON_APP_LIST] = Json.encodeToString(cardList)
        }
    }

    override suspend fun changPersonalMessage(message: EditablePersonalMessage) {
        context.dataStore.edit {
            it[EDITABLE_PERSONAL_MESSAGE] = Json.encodeToString(message)
        }
    }

    override suspend fun changeLoginState(state: Int) {
        context.dataStore.edit {
            it[LOGIN_STATE] = state
        }
    }

    override suspend fun changeCookies(cookies: List<Cookie>) {
        context.dataStore.edit {
            it[COOKIES] = Gson().toJson(cookies)
        }
    }


    override fun observeDynamicTheme(): Flow<Boolean> {
        return context.dataStore.data
            .map {
                it[DYNAMIC_COLOR] ?: DEFAULT_DYNAMIC_COLOR
            }
    }

    override fun observeDarkTheme(): Flow<Int> {
        return context.dataStore.data
            .map {
                it[DARK_THEME] ?: DEFAULT_DARK_THEME
            }
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

    override fun observePersonalMessage(): Flow<EditablePersonalMessage> {
        return context.dataStore.data
            .map {
                Json.decodeFromString<EditablePersonalMessage>(
                    it[EDITABLE_PERSONAL_MESSAGE] ?: Json.encodeToString(
                        DEFAULT_EDITABLE_PERSONAL_MESSAGE
                    )
                )
            }
    }

    override fun observeLoginState(): Flow<Int> {
        return context.dataStore.data.map {
            it[LOGIN_STATE] ?: 0
        }
    }

    override fun observeCookies(): Flow<List<Cookie>> {
        return context.dataStore.data.map {
            val json = it[COOKIES] ?: DEFAULT_VALUE_COOKIES
            if (json == DEFAULT_VALUE_COOKIES) {
                emptyList<Cookie>()
            } else {
                val typeOfT = object : TypeToken<List<Cookie>>() {}.type
                Gson().fromJson(json, typeOfT)
            }
        }
    }
}