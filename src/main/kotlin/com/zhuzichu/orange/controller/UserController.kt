package com.zhuzichu.orange.controller

import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.model.User
import com.zhuzichu.orange.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:0:03
 *@Desciption:
 **/

@RestController
@RequestMapping("/api/user")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @PostMapping("/regist")
    fun regist(@Valid user: User, bindingResult: BindingResult): Result {
        return userService.regist(user)
    }
}