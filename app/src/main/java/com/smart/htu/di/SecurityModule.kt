package com.smart.htu.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun providePasswordEncryptor(): PasswordEncryptor {
        return PasswordEncryptor()
    }

}


@Singleton
class PasswordEncryptor @Inject constructor() {
    private val keyStore = java.security.KeyStore.getInstance("AndroidKeyStore")
    private val keyAlias = "htu_password_key"
    private val transformation =
        "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
    private val IV_SEPARATOR = "]"

    init {
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            createKey()
        }
    }

    private fun createKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encrypt(plaintext: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        // 将IV和密文一起编码为Base64字符串
        val ivAndEncryptedBytes = iv + IV_SEPARATOR.toByteArray(Charsets.UTF_8) + encryptedBytes
        return Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(ciphertext: String): String {
        val decodedBytes = Base64.decode(ciphertext, Base64.DEFAULT)

        // 分离IV和密文
        val ivAndEncryptedData = String(decodedBytes, Charsets.ISO_8859_1)
        val ivEndIndex = ivAndEncryptedData.indexOf(IV_SEPARATOR)
        val iv = ivAndEncryptedData.substring(0, ivEndIndex).toByteArray(Charsets.ISO_8859_1)
        val encryptedBytes =
            ivAndEncryptedData.substring(ivEndIndex + 1).toByteArray(Charsets.ISO_8859_1)

        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun getKey(): Key {
        return keyStore.getKey(keyAlias, null)
    }
}