package com.zhuzichu.orange.controller


import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.model.User
import com.zhuzichu.orange.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/regist")
    @Encrypt
    fun regist(@RequestBody user: User): Result {
        return userService.regist(user)
    }
}