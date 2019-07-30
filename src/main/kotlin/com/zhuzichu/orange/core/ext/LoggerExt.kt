package com.zhuzichu.orange.core.ext

import org.slf4j.LoggerFactory

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24
 *@Time:22:02
 *@Desciption:
 **/

private const val tag = "Orange"

fun Any?.logi(vararg objects: Any?) {
    LoggerFactory.getLogger(tag).info(this.toString(), objects)
}

fun Any?.logw(vararg objects: Any?) {
    LoggerFactory.getLogger(tag).warn(this.toString(), objects)
}

fun Any?.loge(vararg objects: Any?) {
    LoggerFactory.getLogger(tag).error(this.toString(), objects)
}