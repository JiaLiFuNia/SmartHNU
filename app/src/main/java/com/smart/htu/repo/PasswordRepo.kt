package com.smart.htu.repo

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.smart.htu.di.PasswordEncryptor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepo @Inject constructor(
    @ApplicationContext context: Context,
    private val passwordEncryptor: PasswordEncryptor
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val JWC_PASSWORD = "jwc_password"
        const val PASSWORD = "password"
        const val SC_PASSWORD = "sc_password"
    }

    fun savePassword(password: String, key: String) {
        val encryptedPassword = passwordEncryptor.encrypt(password)
        sharedPreferences.edit {
            putString(key, encryptedPassword)
        }
    }

    fun getPassword(key: String): String? {
        val encryptedPassword = sharedPreferences.getString(key, null) ?: return null
        return try {
            passwordEncryptor.decrypt(encryptedPassword)
        } catch (e: Exception) {
            ""
        }
    }

    fun clearPassword() {
        sharedPreferences.edit {
            remove(PASSWORD)
            remove(JWC_PASSWORD)
        }
    }
}
