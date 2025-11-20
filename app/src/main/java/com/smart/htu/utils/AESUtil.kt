package com.smart.htu.utils

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESUtils {
    private const val AES_CHARS = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678"

    fun encryptPassword(password: String, key: String, mode: String): String {
        return if (key.isEmpty()) password
        else aesEncrypt(
            data = password,
            key = key.toByteArray(),
            mode = mode
        )
    }

    fun aesEncrypt(
        data: String,
        key: ByteArray,
        iv: ByteArray = randomString(16).toByteArray(),
        mode: String,
        padding: String = "PKCS5Padding"
    ): String {
        val transformation = "AES/$mode/$padding"
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(key, "AES")
        if (mode == "ECB") {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun randomString(length: Int): String {
        val random = SecureRandom()
        val sb = StringBuilder(length)
        repeat(length) {
            val index = random.nextInt(AES_CHARS.length)
            sb.append(AES_CHARS[index])
        }
        return sb.toString()
    }

}