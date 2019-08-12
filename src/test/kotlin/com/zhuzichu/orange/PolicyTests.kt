package com.zhuzichu.orange

import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 11:28
 */

@RunWith(SpringRunner::class)
@SpringBootTest
class PolicyTests {
    @Test
    fun encryptPolicy() {
        val encryptPolicy = ProjectPolicyUtils.encryptPolicy("""{"username":"asdfasdf","password":"qaioasd520"}""")
        print(encryptPolicy)
        val decryptPolicy = ProjectPolicyUtils.decryptPolicy(encryptPolicy)
        print(decryptPolicy)
    }
}