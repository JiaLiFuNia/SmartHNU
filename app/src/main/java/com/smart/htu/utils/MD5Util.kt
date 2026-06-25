package com.smart.htu.utils

import java.security.MessageDigest

object MD5Util {

    fun md5(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(text.toByteArray())
        return digest.joinToString("") {
            "%02x".format(it)
        }
    }

}