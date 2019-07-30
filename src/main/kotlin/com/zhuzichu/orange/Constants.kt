package com.zhuzichu.orange

/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:12:21
 *@Desciption:
 **/

object Constants {
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
}
