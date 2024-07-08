package com.xhand.hnu.components

import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAEncryptionHelper {

    private const val RSA_ALGORITHM = "RSA"
    private const val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"

    private val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
    private val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)

    fun getPublicKeyFromString(): PublicKey {
        val publicKeyString =
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ=="
        val keySpec =
            X509EncodedKeySpec(Base64.decode(publicKeyString.toByteArray(), Base64.DEFAULT))
        return keyFactory.generatePublic(keySpec)
    }

    fun encryptText(plainText: String, publicKey: PublicKey): String {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
    }
}