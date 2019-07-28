package com.zhuzichu.orange.core.service.redis

import org.codehaus.jackson.map.ObjectMapper
import redis.clients.jedis.params.SetParams

/**
 * Redis 服务接口
 * Jimersy Lee
 * 2017-09-18 14:58:21
 */
interface RedisService {

    /**
     * 初始化操作
     */
    fun create()


    fun destroy()

    //    /**
    //     * 从连接池里取连接（用完连接后必须销毁）
    //     *
    //     * @return
    //     */
    //     Jedis getResource();

    //    /**
    //     * 用完后，销毁连接（必须）
    //     *
    //     * @param jedis
    //     */
    //    void destroyResource(Jedis jedis);

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    operator fun get(key: String): String


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    operator fun <T> get(key: String, clazz: Class<T>): T?


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T> getResult(key: String, clazz: Class<T>): RedisResult<T>

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @param <T>
     * @return
    </T> */
    fun <T> getListResult(key: String, elementClazz: Class<T>): RedisResult<T>


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    operator fun set(key: String, obj: Any): String


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @return
     */
    operator fun set(key: String, value: String): String


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    operator fun set(key: String, obj: Any, expireTime: Int): String


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    operator fun set(key: String, value: String, expireTime: Int): String

    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @return
     */
    operator fun set(key: String, value: String, setParams: SetParams): String

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    fun setnx(key: String, value: String, expiredTime: Int): String

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    fun setxx(key: String, value: String, expiredTime: Int): String

    /**
     * 根据key删除缓存,
     *
     * @param keys
     * @return
     */
    fun delete(vararg keys: String): Long?

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    fun exists(key: String): Boolean


    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return 运算结果
     */
    fun incrBy(key: String, value: Long): Long

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    fun setTTL(key: String, seconds: Int)

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    fun keys(pattern: String): Set<String>

    /**
     * 将对象转为json字符串。若对象为null，则返回 [RedisService.BLANK_CONTENT]
     *
     * @param object
     * @return
     */
    fun toJsonString(`object`: Any): String

    /**
     * json序列化对象。
     *
     * @param value
     * @return 返回序列化后的字符串。若value为null，则返回 [RedisService.BLANK_CONTENT]
     */
    fun makeSerializedString(value: Any): String

    companion object {

        /**
         * 默认过期时间 ，3600(秒)
         */
        val DEFAULT_EXPIRE_TIME = 3600

        val om = ObjectMapper()


        /**
         * 空白占位符
         */
        val BLANK_CONTENT = "__BLANK__"
    }


}
