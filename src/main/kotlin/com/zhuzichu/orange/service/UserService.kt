package com.zhuzichu.orange.service

import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.model.User
import com.zhuzichu.orange.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:0:09
 *@Desciption:
 **/

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun regist(user: User): Result {
        val isExists = userRepository.exists(Example.of(User().apply { username = user.username }))
        if (isExists) {
            return genFailResult("该账号已经被注册")
        }
        val data = userRepository.save(user)
        return genSuccessResult(data)
    }
}