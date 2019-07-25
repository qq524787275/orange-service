package com.zhuzichu.orange

import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import org.junit.Test

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:23:26
 *@Desciption:
 **/

object  Tests{
    @JvmStatic
    fun main(args: Array<String>) {
        val encryptPolicy = ProjectPolicyUtils.encryptPolicy("""{"username":"asdfasdf","password":"qaioasd520"}""")
        println(encryptPolicy)
        val decryptPolicy = ProjectPolicyUtils.decryptPolicy(encryptPolicy)
        println(decryptPolicy)
    }
}

