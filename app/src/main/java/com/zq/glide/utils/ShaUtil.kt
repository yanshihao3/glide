package com.zq.glide.utils

import java.security.MessageDigest
import kotlin.experimental.and

object ShaUtil {
    @JvmStatic
    fun SHA256(str: String): String {
        val messageDigest: MessageDigest
        var encodeStr = ""
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(str.toByteArray(charset("UTF-8")))
            encodeStr = byte2Hex(messageDigest.digest())
        } catch (e: Exception) {
            println("getSHA256 is error" + e.message)
        }
        return encodeStr
    }

    private fun byte2Hex(bytes: ByteArray): String {
        val builder = StringBuilder()
        var temp: String
        for (i in bytes.indices) {
            temp = Integer.toHexString(bytes[i].and(0xFF.toByte()).toInt())
            if (temp.length == 1) {
                builder.append("0")
            }
            builder.append(temp)
        }
        return builder.toString()
    }
}