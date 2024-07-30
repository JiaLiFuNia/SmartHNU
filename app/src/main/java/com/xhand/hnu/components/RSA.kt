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
    fun getScPublicKeyFromString(): PublicKey {
        val publicKeyString =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIDV8I1zpoazcFmv3VNtG/E9/QC14gDhBoW9Yq6o9UNLaOZC41yoGa7hjHqjuPOcmPJ61Wmv7i5UbB5BceGRl2i0pSyOzeAeYpoY5cNRStfQlXFlwV1Ig1P081rxBcCgkWZvhodsWp9yRdKOTTHUCj0FpgD94/2QhvqkxOaW9vAwIDAQAB"
        val keySpec =
            X509EncodedKeySpec(Base64.decode(publicKeyString.toByteArray(), Base64.DEFAULT))
        return keyFactory.generatePublic(keySpec)
    }

    fun encryptText(plainText: String, publicKey: PublicKey): String {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
        } catch (e: Exception) {
            return "error"
        }
    }
}