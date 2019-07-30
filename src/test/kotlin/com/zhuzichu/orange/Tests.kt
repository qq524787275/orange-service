package com.zhuzichu.orange

import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import org.junit.Test

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:23:26
 *@Desciption:
 **/

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        val encryptPolicy = ProjectPolicyUtils.encryptPolicy("""{"username":"18229858146","password":"18229858146","phone":"18229858146","code":"623324"}""")
//        val encryptPolicy = ProjectPolicyUtils.encryptPolicy("""{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjEsImV4cCI6MTU2NDU1MzA5MywiaWF0IjoxNTY0NDY2NjkzLCJ1c2VybmFtZSI6IjE4MjI5ODU4MTQ2In0.DT38TanZgUz1palePvd7iux94Hty-WJUW53uPSpwLpI"}""")

        println(encryptPolicy)
        val decryptPolicy = ProjectPolicyUtils.decryptPolicy(encryptPolicy)
        println(decryptPolicy)
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val code = 50
//        val info = "getRegistCode$code"
//    }
    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjEsImV4cCI6MTU2NDU1MzA5MywiaWF0IjoxNTY0NDY2NjkzLCJ1c2VybmFtZSI6IjE4MjI5ODU4MTQ2In0.DT38TanZgUz1palePvd7iux94Hty-WJUW53uPSpwLpI
}

