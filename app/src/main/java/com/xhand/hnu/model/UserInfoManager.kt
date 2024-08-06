package com.xhand.hnu.model

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
import com.xhand.hnu.model.entity.HourListEntity
import com.xhand.hnu.model.entity.LoginPostEntity
import com.xhand.hnu.model.entity.SecondClassInfo
import com.xhand.hnu.model.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInfoManager(private val context: Context) {

    companion object {
        private val Context.userStore: DataStore<Preferences> by preferencesDataStore("userStore")
        val LOGGED = intPreferencesKey("LOGGED")
        val USERINFO = stringPreferencesKey("USERINFO")
        val SCUSERINFO = stringPreferencesKey("SCUSERINFO")
        val LOGINFO = stringPreferencesKey("LOGINFO")
        val SCHOURS = stringPreferencesKey("SCHOURS")
        val UI = booleanPreferencesKey("UI")
    }

    private val gson = Gson()
    val logged: Flow<Int> = context.userStore.data.map { it[LOGGED] ?: 0 }

    val userInfo: Flow<UserInfoEntity> = context.userStore.data.map {
        val json = it[USERINFO] ?: ""
        gson.fromJson(json, UserInfoEntity::class.java)
    }

    val logInfo: Flow<LoginPostEntity> = context.userStore.data.map {
        val json = it[LOGINFO] ?: ""
        gson.fromJson(json, LoginPostEntity::class.java)
    }

    val scUserInfo: Flow<SecondClassInfo> = context.userStore.data.map {
        val json = it[SCUSERINFO] ?: ""
        gson.fromJson(json, SecondClassInfo::class.java)
    }

    val scHours: Flow<MutableList<HourListEntity>> = context.userStore.data.map {
        val json = it[SCHOURS] ?: ""
        val type = object : TypeToken<List<HourListEntity>>(){}.type
        gson.fromJson(json, type)
    }

    suspend fun saveScHours(scHours: List<HourListEntity>) {
        context.userStore.edit {
            it[SCHOURS] = gson.toJson(scHours)
        }
    }

    suspend fun saveSecondClassInfo(secondClassInfo: SecondClassInfo) {
        context.userStore.edit {
            it[SCUSERINFO] = gson.toJson(secondClassInfo)
        }
    }

    suspend fun save(userInfo: UserInfoEntity, loginCode: Int, logInfo: LoginPostEntity) {
        context.userStore.edit {
            it[LOGGED] = loginCode
            it[USERINFO] = gson.toJson(userInfo)
            it[LOGINFO] = gson.toJson(logInfo)
        }
    }

    suspend fun clear() {
        context.userStore.edit {
            it[LOGGED] = 0
            it[USERINFO] = ""
            it[SCUSERINFO] = ""
        }
    }
}