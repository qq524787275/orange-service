package com.zhuzichu.orange.annotations

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:21:24
 *@Desciption:
 **/

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Encrypt