package com.zhuzichu.orange

import com.zhuzichu.orange.bean.HomeBean
import com.zhuzichu.orange.model.Home
import com.zhuzichu.orange.repository.HomeRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 *@Auther:zhuzichu
 *@Date:2019/8/28 0028
 *@Time:14:02
 *@Desciption:
 **/
@RunWith(SpringRunner::class)
@SpringBootTest
class HomeTests {
    @Autowired
    lateinit var homeRepository: HomeRepository

    @Test
    fun saveHome() {
        homeRepository.saveAll(listOf(
                Home(null,13366L, "轮播图", 5, -1),
                Home(null,13367L, "女装", 20, 1),
                Home(null,13372L, "男装", 20, 2),
                Home(null,13370L, " 鞋包配饰", 20, 3),
                Home(null,13376L, "运动户外", 20, 1)
        ))
    }
}