package com.zhuzichu.orange.core.service.redis.impl

import com.alibaba.fastjson.JSON
import com.zhuzichu.orange.core.ext.logi
import com.zhuzichu.orange.core.service.dynProps4Files.DynProps4FilesService
import com.zhuzichu.orange.core.service.redis.RedisResult
import com.zhuzichu.orange.core.service.redis.RedisService
import org.apache.commons.lang3.StringUtils
import org.codehaus.jackson.type.JavaType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.params.SetParams

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.annotation.Resource
import java.io.IOException
import java.util.*

/**
 * Redis 服务接口实现类
 *
 * @author liwei
 * 16/10/30 下午5:28
 */
@Component("redisService")
class RedisServiceImpl : RedisService {
    private val logger = LoggerFactory.getLogger(RedisServiceImpl::class.java)

    @Resource
    private lateinit var dynProps4Files: DynProps4FilesService

    @Resource
    private lateinit var env: Environment

    private lateinit var pool: JedisPool
    /**
     * 从连接池里取连接（用完连接后必须销毁）
     *
     * @return
     */
    private val resource: Jedis
        get() = pool.resource


    /**
     * 初始化操作
     */
    @PostConstruct
    override fun create() {
        val config = JedisPoolConfig()
        config.maxIdle = dynProps4Files!!.getInt("REDIS_MAX_IDLE", JedisPoolConfig.DEFAULT_MAX_IDLE)
        config.maxTotal = dynProps4Files.getInt("REDIS_MAX_TOTAL", JedisPoolConfig.DEFAULT_MAX_TOTAL)
        config.maxWaitMillis = dynProps4Files.getLong("REDIS_MAX_WAIT", JedisPoolConfig.DEFAULT_MAX_WAIT_MILLIS)
        config.testOnBorrow = true
        pool = JedisPool(config, env.getProperty("spring.redis.host"),
                dynProps4Files.getInt("REDIS_PORT", 6379),
                dynProps4Files.getInt("REDIS_MAX_WAIT", 1000))
//                dynProps4Files.getProperty("REDIS_PASSWORD", ""))
    }

    @PreDestroy
    override fun destroy() {
        try {
            pool.destroy()
        } catch (e: Exception) {
            //do nothing
        }

    }

    /**
     * 用完后，销毁连接（必须）
     *
     * @param jedis
     */
    private fun destroyResource(jedis: Jedis?) {
        if (jedis == null) {
            return
        }
        jedis.close()
    }

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    override fun get(key: String): String {

        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!")
            return StringUtils.EMPTY
        }
        val jedis = this.resource
        try {
            return jedis.get(key)
        } finally {
            this.destroyResource(jedis)
        }

    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    override fun <T> get(key: String, clazz: Class<T>): T? {
        val value = get(key)
        var obj: T? = null
        try {
            obj = RedisService.om.readValue(value, clazz)
        } catch (e: IOException) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.name, value)
        }

        return obj
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    override fun set(key: String, obj: Any, expireTime: Int): String {
        var value = RedisService.BLANK_CONTENT
        try {
            value = RedisService.om.writeValueAsString(obj)
        } catch (e: IOException) {
            logger.error("Can not write object to redis:$obj", e)
        }
        return set(key, value, expireTime)
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    override fun set(key: String, value: String, expireTime: Int): String {
        val jedis = this.resource
        try {
            val result = jedis.set(key, value)
            if (expireTime > 0) {
                jedis.expire(key, expireTime)
            }
            return result
        } finally {
            this.destroyResource(jedis)
        }
    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    override fun <T> getResult(key: String, clazz: Class<T>): RedisResult<T> {
        val redisResult = RedisResult<T>()

        val value = get(key)
        if (StringUtils.isBlank(value)) {
            redisResult.isExist = false
            return redisResult
        }
        //到此步，则表明redis中存在key
        redisResult.isExist = true
        if (StringUtils.equalsIgnoreCase(value, RedisService.Companion.BLANK_CONTENT)) {
            return redisResult
        }
        var obj: T?
        try {
            obj = RedisService.om.readValue(value, clazz)
            redisResult.result = obj
        } catch (e: IOException) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.name, value)
            //到此步直接视为无值
            redisResult.isExist = false
        }

        return redisResult
    }

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @return
     */
    override fun <T> getListResult(key: String, elementClazz: Class<T>): RedisResult<T> {
        val redisResult = RedisResult<T>()

        val value = get(key)
        if (StringUtils.isBlank(value)) {
            redisResult.isExist = false
            return redisResult
        }

        //到此步，则表明redis中存在key
        redisResult.isExist = true
        if (StringUtils.equalsIgnoreCase(value, RedisService.BLANK_CONTENT)) {
            return redisResult
        }

        var list: List<Any?>?
        try {
            list = RedisService.om.readValue<List<T>>(value, getCollectionType(List::class.java, elementClazz))
            redisResult.listResult = list
        } catch (e: IOException) {
            logger.error("Can not unserialize list to [{}] with string [{}]", elementClazz.name, value)
            //到此步直接视为无值
            redisResult.isExist = false
        }

        return redisResult
    }

    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    override fun set(key: String, obj: Any): String {
        var value = RedisService.BLANK_CONTENT
        try {
            value = RedisService.om.writeValueAsString(obj)
        } catch (e: IOException) {
            logger.error("Can not write object to redis:$obj", e)
        }
        return set(key, value)
    }


    /**
     * 写入/修改 缓存内容(默认有过期时间 1小时)
     *
     * @param key
     * @param value
     * @return
     */
    override fun set(key: String, value: String): String {
        return this.set(key, value, RedisService.DEFAULT_EXPIRE_TIME)
    }


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @return
     */
    override fun set(key: String, value: String, setParams: SetParams): String {
        val jedis = this.resource
        try {
            return jedis.set(key, value, setParams)
        } finally {
            this.destroyResource(jedis)
        }

    }

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    override fun setnx(key: String, value: String, expiredTime: Int): String {
        val setParams = SetParams()
        setParams.nx().ex(expiredTime)
        return this.set(key, value, setParams)
    }

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    override fun setxx(key: String, value: String, expiredTime: Int): String {
        val setParams = SetParams()
        setParams.xx().ex(expiredTime)
        return this.set(key, value, setParams)
    }

    /**
     * 根据key删除缓存
     *
     * @param keys
     * @return
     */
    override fun delete(vararg keys: String): Long? {
        if (keys.isEmpty()) {
            logger.warn("Params keys is null or 0 length!")
            return -1L
        }
        val jedis = this.resource
        try {
            return jedis.del(*keys)
        } finally {
            this.destroyResource(jedis)
        }
    }

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    override fun exists(key: String): Boolean {
        if (StringUtils.isBlank(key)) {
            //不接受空值
            return false
        }
        val jedis = this.resource
        try {
            return jedis.exists(key)!!
        } finally {
            this.destroyResource(jedis)
        }


    }

    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return
     */
    override fun incrBy(key: String, value: Long): Long {
        val jedis = this.resource
        try {
            return jedis.incrBy(key, value)
        } finally {
            this.destroyResource(jedis)
        }
    }

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    override fun setTTL(key: String, seconds: Int) {
        if (seconds < 0) {
            return
        }
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!")
            return
        }
        val jedis = this.resource
        try {
            jedis.expire(key, seconds)
        } finally {
            this.destroyResource(jedis)
        }
    }

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    override fun keys(pattern: String): Set<String> {

        if (StringUtils.isBlank(pattern)) {
            logger.warn("Params pattern is blank!")
            return emptySet()
        }
        val jedis = this.resource
        try {
            return jedis.keys(pattern)
        } finally {
            this.destroyResource(jedis)
        }
    }


    /**
     * 将对象转为json字符串。若对象为null，则返回 [RedisService.BLANK_CONTENT]
     *
     * @param object
     * @return
     */
    override fun toJsonString(`object`: Any): String {
        if (`object` is Collection<*> && CollectionUtils.isEmpty(`object`)) {
            return RedisService.BLANK_CONTENT
        }
        if (`object` is Map<*, *> && CollectionUtils.isEmpty(`object`)) {
            return RedisService.BLANK_CONTENT
        }
        try {
            return RedisService.om.writeValueAsString(`object`)
        } catch (e: IOException) {
        }
        return RedisService.BLANK_CONTENT
    }

    override fun makeSerializedString(value: Any): String {
        if (value is Collection<*> && value.size == 0) {
            return RedisService.BLANK_CONTENT
        }

        return if (value is Map<*, *> && value.size == 0) {
            RedisService.BLANK_CONTENT
        } else JSON.toJSONString(value)


    }

    private fun <T> getCollectionType(collectionClazz: Class<out Collection<*>>,
                                      elementClazz: Class<T>): JavaType {
        return RedisService.om.typeFactory.constructCollectionType(collectionClazz, elementClazz)
    }

}
