package com.xhand.hnu2.components

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import android.util.Base64

object RSAEncryptionHelper {

    private const val RSA_ALGORITHM = "RSA"
    private const val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"

    private val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
    private val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)

    fun getPublicKeyFromString(): PublicKey? =
        try {
            val publicKeyString = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ=="
            val keySpec =
                X509EncodedKeySpec(Base64.decode(publicKeyString.toByteArray(), Base64.DEFAULT))
            keyFactory.generatePublic(keySpec)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }


    fun encryptText(plainText: String, publicKey: PublicKey): String? =
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
}