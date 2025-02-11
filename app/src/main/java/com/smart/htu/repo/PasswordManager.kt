package com.smart.htu.repo

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun savePassword(password: String) {
        sharedPreferences.edit()
            .putString("password", password)
            .apply()
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", null)
    }

}
