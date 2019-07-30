package com.zhuzichu.orange.core.service.redis

class RedisResult<T> {
    var isExist = false
    var result: T? = null
    var listResult: List<T>? = null
}
