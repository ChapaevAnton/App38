package com.w4eret1ckrtb1tch.app38

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class Test {


    fun getHashMd5v1(publicKey: String, privateKey: String): String? {
        val dataStamp = Date().time.toString()
        val hash = "$dataStamp$privateKey$publicKey"
        var digest: ByteArray = byteArrayOf()
        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            digest = messageDigest.digest(hash.toByteArray())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return Base64.getEncoder().encodeToString(digest)
    }

    fun getHashMd5v2(publicKey: String, privateKey: String): String? {
        val dataStamp = Date().time.toString()
        val hash = "$dataStamp$privateKey$publicKey"
        var digest: ByteArray = byteArrayOf()
        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            digest = messageDigest.digest(hash.toByteArray())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        val md5Hex = BigInteger(1, digest)
            .toString(16)
            .let {
                if (it.length < 32) "0$it" else it
            }
        return md5Hex
    }
}