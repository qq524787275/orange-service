package com.zhuzichu.orange

/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:12:21
 *@Desciption:
 **/

object Constants {
    private const val API_BASE = "/api/"
    const val API_USER = API_BASE.plus("user")
    const val API_SMS = API_BASE.plus("sms")

    fun getRegistCodeKey(phone: String?): String {
        return "RegistCode-".plus(phone)
    }
}
