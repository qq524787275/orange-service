package com.zhuzichu.orange

import com.qiniu.util.Auth
import com.taobao.api.DefaultTaobaoClient


/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:12:21
 *@Desciption:
 **/

object Constants {
    private const val TAOBAO_SERVER_URL = "https://eco.taobao.com/router/rest"
    private const val TAOBAO_APPKAY = "27640838"
    private const val TAOBAO_APPSECRET = "4971c2e8fe569a9447f47df40aa3bc0a"
    const val TAOBAO_PID = 109076000438L
    private const val QINIU_ACCESSKEY = "W88LIHs3q2MAwXwMzLuXRRWeb8Yh9zACA85eRRaV"
    private const val QINIU_SECRETKEY = "UHwmRClWqtCUfT3Fyw0RWaenLKHWSeS-A5zR7Lk6"
    private const val QINIU_BUCKET_ORANGE_PICTURE = "orange-picture"

    const val AVATAR_DEFAULT_URL = "http://pw41jmzgi.bkt.clouddn.com/"
    const val AVATAR_DEFAULT = "avatar_default.jpeg"

    const val KEY_ORANGE = "orange"
    const val KEY_USER_ID = "uid"
    const val KEY_USER_USERNAME = "username"

    private const val API_BASE = "/api/"
    const val API_USER = API_BASE.plus("user")
    const val API_SMS = API_BASE.plus("sms")
    const val API_VERSION = API_BASE.plus("version")
    const val API_CATEGORY = API_BASE.plus("category")
    const val API_TAOBAO = API_BASE.plus("taobao")

    val taobaoClient = DefaultTaobaoClient(
            TAOBAO_SERVER_URL,
            TAOBAO_APPKAY,
            TAOBAO_APPSECRET
    )

    fun getRegistCodeKey(phone: String?): String {
        return "RegistCode-".plus(phone)
    }

    fun getLoginCodeKey(phone: String?): String {
        return "LoginCode-".plus(phone)
    }

    fun getAvatarToken(uid: Long): String {
        val auth = Auth.create(QINIU_ACCESSKEY, QINIU_SECRETKEY)
        return auth.uploadToken(QINIU_BUCKET_ORANGE_PICTURE)
    }


}
