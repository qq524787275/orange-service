package com.zhuzichu.orange.core.service.redis

/**
 * redis 中取得的结果
 * Created by liwei on 2017/2/7.
 */
class RedisResult<T> {

    /**
     * redis中是否存在
     */
    var isExist = false

    /**
     * redis中取得的数据
     */
    var result: T? = null

    /**
     * redis中取得的List数据
     */
    var listResult: List<T>? = null
    /**
     * redis中的key是否存在。true:表示redis中存在Key,但对应的值为空值标记
     */
    var isKeyExists = false
    /**
     * redis中key 对应在对象值
     */
    var resultObj: T? = null
}
