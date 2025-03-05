package com.smart.htu.utils

import com.smart.htu.App.Companion.context
import com.smart.htu.R
import net.i2p.crypto.eddsa.EdDSAEngine
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Base64

object GenerateQWeather {

    //用于生成和风天气密钥
    fun getQWeatherAuth(): String {
        return "Bearer " + generateJWT(
            privateKeyStr = RSAUtil.getPrivateKeyFromRaw(context, R.raw.private_key),
            projectID = "2AKUWC7K78",
            keyID = "CHPN45DFAX"
        )
    }

    @Throws(
        InvalidKeySpecException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        SignatureException::class
    )
    fun generateJWT(
        privateKeyStr: String,
        projectID: String,
        keyID: String
    ): String {
        val privateKeyBytes = Base64.getDecoder().decode(
            privateKeyStr.trim()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
        )
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val privateKey = EdDSAPrivateKey(keySpec)

        val headerJson = "{\"alg\": \"EdDSA\", \"kid\": \"$keyID\"}"

        val iat = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() - 30
        val exp = iat + 900
        val payloadJson = "{\"sub\": \"$projectID\", \"iat\": $iat, \"exp\": $exp}"

        val headerEncoded =
            Base64.getUrlEncoder().encodeToString(headerJson.toByteArray(StandardCharsets.UTF_8))
        val payloadEncoded =
            Base64.getUrlEncoder().encodeToString(payloadJson.toByteArray(StandardCharsets.UTF_8))
        val data = "$headerEncoded.$payloadEncoded"

        val spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519)

        val signature = EdDSAEngine(MessageDigest.getInstance(spec.hashAlgorithm)).apply {
            initSign(privateKey)
            update(data.toByteArray(StandardCharsets.UTF_8))
        }.sign()

        val signatureString = Base64.getUrlEncoder().encodeToString(signature)

        return "$data.$signatureString"
    }
}
