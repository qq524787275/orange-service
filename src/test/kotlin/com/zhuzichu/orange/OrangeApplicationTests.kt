package com.zhuzichu.orange

import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class OrangeApplicationTests {

    @Test
    fun contextLoads() {
        val encryptPolicy = ProjectPolicyUtils.encryptPolicy("""{"username":"asdfasdf","password":"qaioasd520"}""")
        print(encryptPolicy)
        val decryptPolicy = ProjectPolicyUtils.decryptPolicy(encryptPolicy)
        print(decryptPolicy)
    }
}
