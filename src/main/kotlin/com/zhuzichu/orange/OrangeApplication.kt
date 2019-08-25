package com.zhuzichu.orange

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class OrangeApplication

fun main(args: Array<String>) {
	runApplication<OrangeApplication>(*args)
}
