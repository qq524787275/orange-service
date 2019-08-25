package com.zhuzichu.orange.core.ext

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-23
 * Time: 18:28
 */
fun Double.format2(): String {
    return String.format("%.2f", this)
}

fun String.scheme(): String {
    return "http:".plus(this)
}