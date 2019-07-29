package com.zhuzichu.orange.controller

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.service.redis.RedisService
import com.zhuzichu.orange.core.utils.ProjectVerifyUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.zhuzichu.orange.core.utils.ProjectSmsUtils
import com.zhuzichu.orange.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.Resource


/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:19:01
 *@Desciption:
 **/

@RestController
@RequestMapping(Constants.API_SMS)
class SmsController {
    @Autowired
    lateinit var redisService: RedisService

    @PostMapping("/getRegistCode")
    fun getRegistCode(
            @RequestParam phone: String
    ): Result {
        val code = ProjectVerifyUtils.getVerifyCode6()
        val smsRequest = ProjectSmsUtils.getSmsRequest(phone, code)
        return try {
            val sendSmsResponse = ProjectSmsUtils.client.getCommonResponse(smsRequest)
            sendSmsResponse.data
            redisService[Constants.getRegistCodeKey(phone), code.toString()] = 60 * 5
            genSuccessResult("短信发送成功")
        } catch (e: Exception) {
            e.printStackTrace()
            if (e !is ClientException) {
                genSuccessResult()
            }
            genFailResult("短信发送失败")

        }
    }
}