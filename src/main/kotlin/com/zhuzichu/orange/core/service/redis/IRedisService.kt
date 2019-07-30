package com.zhuzichu.orange.core.service.redis

import org.codehaus.jackson.map.ObjectMapper
import redis.clients.jedis.params.SetParams


interface IRedisService {


    fun create()


    fun destroy()

    operator fun get(key: String): String?

    operator fun <T> get(key: String, clazz: Class<T>): T?

    fun <T> getResult(key: String, clazz: Class<T>): RedisResult<T>

    fun <T> getListResult(key: String, elementClazz: Class<T>): RedisResult<T>

    operator fun set(key: String, obj: Any): String

    operator fun set(key: String, value: String): String

    operator fun set(key: String, obj: Any, expireTime: Int): String

    operator fun set(key: String, value: String, expireTime: Int): String

    operator fun set(key: String, value: String, setParams: SetParams): String

    fun setnx(key: String, value: String, expiredTime: Int): String

    fun setxx(key: String, value: String, expiredTime: Int): String

    fun delete(vararg keys: String): Long?

    fun exists(key: String): Boolean

    fun incrBy(key: String, value: Long): Long

    fun setTTL(key: String, seconds: Int)

    fun keys(pattern: String): Set<String>

    fun toJsonString(obj: Any): String

    fun makeSerializedString(value: Any): String

    companion object {

        /**
         * 默认过期时间 ，3600(秒)
         */
        const val DEFAULT_EXPIRE_TIME = 3600
        val om = ObjectMapper()
    }
}