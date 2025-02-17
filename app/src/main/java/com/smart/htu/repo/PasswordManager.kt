package com.smart.htu.repo

import android.content.SharedPreferences
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun savePassword(password: String, key: String = "password") {
        sharedPreferences.edit()
            .putString(key, password)
            .apply()
    }

    fun getPassword(key: String = "password"): String? {
        return sharedPreferences.getString(key, DEFAULT_PASSWORD)
    }

}
