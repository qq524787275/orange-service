package com.zhuzichu.orange

import com.zhuzichu.orange.core.service.redis.IRedisService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 11:30
 */

@RunWith(SpringRunner::class)
@SpringBootTest
class RedisTests {
    @Test
    fun put() {
        redisService[Constants.getRegistCodeKey("18229858146"), 456123.toString()] = 60
    }

    @Autowired
    lateinit var redisService: IRedisService

}