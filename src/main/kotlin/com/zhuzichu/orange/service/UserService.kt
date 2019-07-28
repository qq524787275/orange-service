package com.zhuzichu.orange.service

import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import com.zhuzichu.orange.core.utils.ProjectTokenUtils
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
        val isExistsByUsername = userRepository.exists(Example.of(User(username = user.username)))
        if (isExistsByUsername) {
            return genFailResult("该账号已经被注册")
        }
        val isExistsByPhone = userRepository.exists(Example.of(User(username = user.phone)))
        if (isExistsByPhone) {
            return genFailResult("该手机号已经被绑定")
        }
        val data = userRepository.save(user.apply {
            password = ProjectPolicyUtils.md5(user.password)
        })
        return genSuccessResult(mapOf(
                "token" to ProjectTokenUtils.createJWTToken(data.id, data.username)
        ))
    }
}