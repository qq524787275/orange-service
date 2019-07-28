package com.zhuzichu.orange.core.utils

import org.apache.commons.codec.binary.Base64
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:23:24
 *@Desciption:
 **/

object ProjectPolicyUtils {
    private const val DEFAULT_CHARSET = "UTF-8"
    private const val AES_KEY = "0987654321orange"

    // 加密
    @Throws(Exception::class)
    fun encrypt(sSrc: String, sKey: String?): String {
        requireNotNull(sKey) { "sKey can't be null." }
        // 判断Key是否为16位
        require(sKey?.length == 16) { "Key长度不是16位" }
        val raw = sKey?.toByteArray(charset(DEFAULT_CHARSET))
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")// "算法/模式/补码方式"
        val iv = IvParameterSpec("0102030405060708".toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
        val encrypted = cipher.doFinal(sSrc.toByteArray(charset(DEFAULT_CHARSET)))
        return Base64.encodeBase64URLSafeString(encrypted)// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */

    @Throws(Exception::class)
    fun decrypt(sSrc: String, sKey: String?): String? {
        try {
            // 判断Key是否正确
            requireNotNull(sKey) { "sKey can't be null." }
            // 判断Key是否为16位
            require(sKey?.length == 16) { "Key长度不是16位" }
            val raw = sKey?.toByteArray(charset(DEFAULT_CHARSET))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val iv = IvParameterSpec("0102030405060708".toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val encrypted1 = Base64.decodeBase64(sSrc)// 先用base64解密
            try {
                val original = cipher.doFinal(encrypted1)
                return String(original, charset(DEFAULT_CHARSET))
            } catch (e: Exception) {
                return null
            }
        } catch (ex: Exception) {
            return null
        }
    }

    fun decryptPolicy(policy: String): String {
        var p = policy
        try {
            if (p.isNotBlank()) {
                p = URLDecoder.decode(p, "UTF-8")
                val result = decrypt(p, AES_KEY)
                return result.toString()
            }
        } catch (e: Exception) {
        }

        return ""
    }

    /***
     * 加密
     * @param policy
     * @return
     */
    fun encryptPolicy(policy: String): String {
        try {
            if (policy.isNotBlank()) {
                return URLEncoder.encode(encrypt(policy, AES_KEY), "UTF-8")
            }
        } catch (e: Exception) {
        }
        return ""
    }


    // MD5変換
    fun md5(str: String?): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest: ByteArray = instance.digest(str?.toByteArray())
            var sb = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                var i: Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                //如果是一位的话，补0
                if (hexString.length < 2) hexString = "0$hexString"
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}