package com.zhuzichu.orange.controller


import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.service.redis.IRedisService
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
    fun regist(@RequestBody user: User): Result {
        val code = redisService[Constants.getRegistCodeKey(user.phone)]
        if (code.isNullOrBlank() || code != user.code) {
            return genFailResult("验证码错误")
        }
        return userService.regist(user)
    }

    @PostMapping("/login")
    @Encrypt
    fun login(@RequestBody loginParam: LoginParam): Result {
        return userService.login(User(username = loginParam.username, password = loginParam.password))
    }

    @PostMapping("/loginByPhone")
    @Encrypt
    fun loginByPhone(@RequestBody loginByPhoneParam: LoginByPhoneParam): Result {
        val code = redisService[Constants.getRegistCodeKey(loginByPhoneParam.phone)]
        if (code.isNullOrBlank() || code != loginByPhoneParam.code) {
            return genFailResult("验证码错误")
        }
        return userService.loginByPhone(User(phone = loginByPhoneParam.phone))
    }

    @PostMapping("/getUserInfo")
    fun getUserInfo(request: HttpServletRequest): Result {
        val uid = request.getAttribute(Constants.KEY_USER_ID) as Long
        return userService.getUserInfo(User(id = uid))
    }

    //---------------------------------------参数分界线---------------------------------------

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