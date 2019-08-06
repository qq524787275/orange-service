package com.zhuzichu.orange

import okhttp3.Cache.key
import org.springframework.cglib.core.CollectionUtils.bucket
import com.qiniu.util.Auth


/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:12:21
 *@Desciption:
 **/

object Constants {
    private const val QINIU_ACCESSKEY = "W88LIHs3q2MAwXwMzLuXRRWeb8Yh9zACA85eRRaV"
    private const val QINIU_SECRETKEY = "UHwmRClWqtCUfT3Fyw0RWaenLKHWSeS-A5zR7Lk6"
    private const val QINIU_BUCKET = "orange"
    const val KEY_ORANGE = "orange"
    const val KEY_USER_ID = "uid"
    const val KEY_USER_USERNAME = "username"

    private const val API_BASE = "/api/"
    const val API_USER = API_BASE.plus("user")
    const val API_SMS = API_BASE.plus("sms")

    fun getRegistCodeKey(phone: String?): String {
        return "RegistCode-".plus(phone)
    }

    fun getLoginCodeKey(phone: String?): String {
        return "LoginCode-".plus(phone)
    }

    fun getAvatarToken(uid: Long): String {
        val auth = Auth.create(QINIU_ACCESSKEY, QINIU_SECRETKEY)
        return auth.uploadToken(QINIU_BUCKET, "avatar_".plus(uid))
    }
}
