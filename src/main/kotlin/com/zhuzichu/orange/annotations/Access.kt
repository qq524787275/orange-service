package com.zhuzichu.orange.annotations


/**
 *@Auther:zhuzichu
 *@Date:2019/7/24 0024
 *@Time:23:28
 *@Desciption:
 **/
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Access(val authorities: Array<String> = [])
