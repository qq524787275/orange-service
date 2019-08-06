package com.zhuzichu.orange.controller


import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.core.ext.logi
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.service.redis.IRedisService
import com.zhuzichu.orange.core.utils.ProjectIpAddrUtils
import com.zhuzichu.orange.model.Orange
import com.zhuzichu.orange.model.User
import com.zhuzichu.orange.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.NotBlank

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:0:03
 *@Desciption:
 **/

@RestController
@RequestMapping(Constants.API_USER)
class UserController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var redisService: IRedisService

    @PostMapping("/regist")
    @Encrypt
    fun regist(@RequestBody user: User, httpRequest: HttpServletRequest): Result {
        val code = redisService[Constants.getRegistCodeKey(user.phone)]
        if (code.isNullOrBlank() || code != user.code) {
            return genFailResult("验证码错误")
        }
        val orange = httpRequest.getAttribute(Constants.KEY_ORANGE) as Orange
        user.loginLastTime = System.currentTimeMillis()
        user.loginLastPlatform = orange.platform
        user.loginLastVersionCode = orange.versionCode
        user.loginLastVersionName = orange.versionName
        user.loginLastDevice = orange.device
        user.registTime = System.currentTimeMillis()
        user.loginLastIp = ProjectIpAddrUtils.getIpAddr(httpRequest)
        httpRequest.remoteAddr.logi()
        return userService.regist(user)
    }

    @PostMapping("/login")
    @Encrypt
    fun login(@RequestBody loginParam: LoginParam, httpRequest: HttpServletRequest): Result {
        val orange = httpRequest.getAttribute(Constants.KEY_ORANGE) as Orange
        val ipAddr = ProjectIpAddrUtils.getIpAddr(httpRequest)
        return userService.login(User(username = loginParam.username, password = loginParam.password), orange, ipAddr)
    }

    @PostMapping("/loginByPhone")
    @Encrypt
    fun loginByPhone(@RequestBody loginByPhoneParam: LoginByPhoneParam, httpRequest: HttpServletRequest): Result {
        val code = redisService[Constants.getRegistCodeKey(loginByPhoneParam.phone)]
        if (code.isNullOrBlank() || code != loginByPhoneParam.code) {
            return genFailResult("验证码错误")
        }
        val ipAddr = ProjectIpAddrUtils.getIpAddr(httpRequest)
        val orange = httpRequest.getAttribute(Constants.KEY_ORANGE) as Orange
        return userService.loginByPhone(User(phone = loginByPhoneParam.phone), orange, ipAddr)
    }

    @PostMapping("/getUserInfo")
    fun getUserInfo(request: HttpServletRequest): Result {
        val uid = request.getAttribute(Constants.KEY_USER_ID) as Long
        return userService.getUserInfo(User(id = uid))
    }

    @PostMapping("/updateUserInfo")
    @Encrypt
    fun updateUserInfo(@RequestBody updateParam: UpdateParam, request: HttpServletRequest): Result {
        val uid = request.getAttribute(Constants.KEY_USER_ID) as Long
        "执行了:".plus(uid).logi()
        return userService.updateUserInfo(uid, updateParam.type, updateParam.value)
    }


    @PostMapping("/getAvatarToken")
    fun getAvatarToken(request: HttpServletRequest): Result {
        val uid = request.getAttribute(Constants.KEY_USER_ID) as Long
        return genSuccessResult(data = Constants.getAvatarToken(uid))
    }

    //---------------------------------------参数分界线---------------------------------------
    data class UpdateParam(
            @field:NotBlank(message = "类型不能为空")
            val type: Int,
            @field:NotBlank(message = "更新的数据不能为空")
            val value: Any
    ) {
        companion object {
            const val TYPE_NICKNAME = 0
            const val TYPE_SEX = 1
            const val TYPE_EMAIL = 2
            const val TYPE_LOCATION = 3
            const val TYPE_SUMMARY = 4
            const val TYPE_AVATAR = 5
        }
    }

    data class LoginParam(
            @field:NotBlank(message = "账号不能为空")
            val username: String,
            @field:NotBlank(message = "密码不能为空")
            val password: String
    )

    data class LoginByPhoneParam(
            @field:NotBlank(message = "手机号不能为空")
            val phone: String,
            @field:NotBlank(message = "验证码不能为空")
            val code: String
    )
}