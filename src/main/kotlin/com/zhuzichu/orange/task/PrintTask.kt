package com.zhuzichu.orange.task

import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import org.springframework.scheduling.annotation.Scheduled
import java.util.*


/**
 *@Auther:zhuzichu
 *@Date:2019/8/25 0025
 *@Time:14:13
 *@Desciption:
 **/
@Component
class PrintTask {
    companion object {
        private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    @Scheduled(fixedRate = 1000 * 3)
    fun reportCurrentTime() {
        println("NOW：" + sdf.format(Date()))
    }
}