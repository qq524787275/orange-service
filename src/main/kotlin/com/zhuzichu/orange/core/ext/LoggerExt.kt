package com.zhuzichu.orange.core.ext

import org.slf4j.LoggerFactory

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24
 *@Time:22:02
 *@Desciption:
 **/

fun String?.logi(any: Any) {
    LoggerFactory.getLogger(any.javaClass).info(this)
}

fun String?.logw(any: Any, vararg objects: Any?) {
    LoggerFactory.getLogger(any.javaClass).warn(this, objects)
}

fun String?.loge(any: Any, vararg objects: Any?) {
    LoggerFactory.getLogger(any.javaClass).error(this, objects)
}