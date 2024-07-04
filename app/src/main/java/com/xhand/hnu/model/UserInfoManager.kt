package com.xhand.hnu.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.xhand.hnu.model.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInfoManager(private val context: Context) {

    companion object {
        private val Context.userStore: DataStore<Preferences> by preferencesDataStore("user_token")
        val LOGGED = intPreferencesKey("LOGGED")
        val USERINFO = stringPreferencesKey("USERINFO")
    }

    private val gson = Gson()
    val logged: Flow<Int> = context.userStore.data.map { it[LOGGED] ?: 0 }
    val userInfo: Flow<UserInfoEntity> = context.userStore.data.map {
        val json = it[USERINFO] ?: ""
        gson.fromJson(json, UserInfoEntity::class.java)
    }

    suspend fun save(userInfo: UserInfoEntity, loginCode: Int) {
        context.userStore.edit {
            it[LOGGED] = loginCode
            it[USERINFO] = gson.toJson(userInfo)
        }
    }


}