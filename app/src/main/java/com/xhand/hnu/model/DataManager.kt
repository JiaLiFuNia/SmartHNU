package com.xhand.hnu.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataStore")
        val HISTORY_LIST = stringSetPreferencesKey("HISTORY_LIST")
    }

    suspend fun saveHistoryList(historyList: List<String>) {
        context.dataStore.edit { it[HISTORY_LIST] = historyList.toSet() }
    }

    val historyList: Flow<Set<String>> = context.dataStore.data.map { it[HISTORY_LIST] ?: emptySet()   }

}