package com.smart.htu.utils

import android.content.Context
import android.util.Base64
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAUtil {

    private const val RSA_ALGORITHM = "RSA"
    private const val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"

    fun getPrivateKeyFromRaw(context: Context, resId: Int): String {
        return try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            reader.readText()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s".toRegex(), "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getPublicKeyFromRaw(context: Context, resId: Int): PublicKey? {
        return try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            val keyPEM = reader.readText()
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\s".toRegex(), "")

            val keyBytes = Base64.decode(keyPEM, Base64.DEFAULT)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
            keyFactory.generatePublic(keySpec)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun encryptText(plainText: String, publicKey: PublicKey): String? {
        return try {
            val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}