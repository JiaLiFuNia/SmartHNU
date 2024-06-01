package com.xhand.hnu.model.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xhand.hnu.model.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInfoManager(private val context: Context) {
    companion object {
        val Context.userStore by preferencesDataStore(name = "user_store", corruptionHandler = null)

        val LOGGED = booleanPreferencesKey("LOGGED")
        val USERNAME = stringPreferencesKey("USERNAME")
        val STUID = stringPreferencesKey("STUID")
        val ACCADAMIC = stringPreferencesKey("ACCADAMIC")
    }

    val logged: Flow<Boolean> = context.userStore.data.map { it[LOGGED] ?: false }
    val userName: Flow<String> = context.userStore.data.map { it[USERNAME] ?: "" }
    val stuID: Flow<String> = context.userStore.data.map { it[STUID] ?: "" }
    val accadamic: Flow<String> = context.userStore.data.map { it[ACCADAMIC] ?: "" }

    suspend fun save(userInfoEntity: UserInfoEntity) {
        context.userStore.edit {
            it[LOGGED] = userInfoEntity.name.isNotEmpty()
            it[USERNAME] = userInfoEntity.name
            it[STUID] = userInfoEntity.studentID
            it[ACCADAMIC] = userInfoEntity.academy

        }
    }
}