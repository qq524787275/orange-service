package com.zhuzichu.orange.core.service.redis

import com.alibaba.fastjson.JSON
import com.zhuzichu.orange.core.service.redis.IRedisService.Companion.DEFAULT_EXPIRE_TIME
import org.apache.commons.lang3.StringUtils
import org.codehaus.jackson.type.JavaType
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.params.SetParams
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-07-30
 * Time: 15:17
 */

@Component("redisService")
class RedisService : IRedisService {
    private lateinit var pool: JedisPool

    private fun obtain(): Jedis = pool.resource

    @PostConstruct
    override fun create() {
        val config = JedisPoolConfig()
        config.maxIdle = 500
        config.maxTotal = 8
        config.maxWaitMillis = -1L
        config.testOnBorrow = true
        pool = JedisPool(config, "0.0.0.0", 6379, 1000)
    }

    @PreDestroy
    override fun destroy() {
        pool.destroy()
    }

    override fun get(key: String): String? {
        return obtain().use {
            it.get(key)
        }
    }

    override fun <T> get(key: String, clazz: Class<T>): T? {
        val value = get(key)
        return try {
            IRedisService.om.readValue(value, clazz)
        } catch (e: Exception) {
            null
        }
    }

    override fun <T> getResult(key: String, clazz: Class<T>): RedisResult<T> {
        val redisResult = RedisResult<T>()
        val value = get(key)
        if (StringUtils.isBlank(value)) {
            redisResult.isExist = false
            return redisResult
        }
        try {
            redisResult.isExist = true
            redisResult.result = IRedisService.om.readValue(value, clazz)
        } catch (e: Exception) {
            redisResult.isExist = false
        } finally {
            return redisResult
        }
    }

    override fun <T> getListResult(key: String, elementClazz: Class<T>): RedisResult<T> {
        val redisResult = RedisResult<T>()
        val value = get(key)
        if (StringUtils.isBlank(value)) {
            redisResult.isExist = false
            return redisResult
        }
        try {
            redisResult.isExist = true
            redisResult.listResult = IRedisService.om.readValue<List<T>>(value, getCollectionType(List::class.java, elementClazz))
        } catch (e: Exception) {
            redisResult.isExist = false
        } finally {
            return redisResult
        }
    }

    override fun set(key: String, obj: Any): String {
        return set(key, obj, DEFAULT_EXPIRE_TIME)
    }

    override fun set(key: String, value: String): String {
        return set(key, value, DEFAULT_EXPIRE_TIME)
    }

    override fun set(key: String, obj: Any, expireTime: Int): String {
        return set(key, IRedisService.om.writeValueAsString(obj), expireTime)
    }

    override fun set(key: String, value: String, expireTime: Int): String {
        return obtain().use {
            it.set(key, value).apply {
                it.expire(key, expireTime)
            }
        }
    }

    override fun set(key: String, value: String, setParams: SetParams): String {
        return obtain().use {
            return it.set(key, value, setParams)
        }
    }

    override fun setnx(key: String, value: String, expiredTime: Int): String {
        val setParams = SetParams()
        setParams.nx().ex(expiredTime)
        return set(key, value, setParams)
    }

    override fun setxx(key: String, value: String, expiredTime: Int): String {
        val setParams = SetParams()
        setParams.xx().ex(expiredTime)
        return this.set(key, value, setParams)
    }

    override fun delete(vararg keys: String): Long? {
        return obtain().use {
            it.del(*keys)
        }
    }

    override fun exists(key: String): Boolean {
        return obtain().use {
            it.exists(key)
        }
    }

    override fun incrBy(key: String, value: Long): Long {
        return obtain().use {
            it.incrBy(key, value)
        }
    }

    override fun setTTL(key: String, seconds: Int) {
        return obtain().use {
            it.expire(key, seconds)
        }
    }

    override fun keys(pattern: String): Set<String> {
        return obtain().use {
            it.keys(pattern)
        }
    }

    override fun toJsonString(obj: Any): String {
        return IRedisService.om.writeValueAsString(obj)
    }

    override fun makeSerializedString(value: Any): String {
        return JSON.toJSONString(value)
    }

    private fun <T> getCollectionType(collectionClazz: Class<out Collection<*>>,
                                      elementClazz: Class<T>): JavaType {
        return IRedisService.om.typeFactory.constructCollectionType(collectionClazz, elementClazz)
    }
}