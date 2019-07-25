package com.zhuzichu.orange.controller

import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.TaobaoClient
import com.taobao.api.request.TimeGetRequest
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genSuccessResult
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
//        val client: TaobaoClient = DefaultTaobaoClient(
//                "http://gw.api.taobao.com/router/rest",
//                "27560769",
//                "dfbc7bae26a24e137a73f16298c60a5"
//        )
//        val req = TimeGetRequest()
//        val response = client.execute(req)
//        if(response.)
        return userService.regist(user)
    }
}