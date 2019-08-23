package com.zhuzichu.orange.service

import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.controller.UserController
import com.zhuzichu.orange.core.ext.logi
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import com.zhuzichu.orange.core.utils.ProjectTokenUtils
import com.zhuzichu.orange.bean.Orange
import com.zhuzichu.orange.model.User
import com.zhuzichu.orange.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
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
        val isExistsByPhone = userRepository.exists(Example.of(User(phone = user.phone)))
        if (isExistsByPhone) {
            return genFailResult("该手机号已经被绑定")
        }
        val data = userRepository.save(user.apply {
            avatarUrl = Constants.AVATAR_DEFAULT
            password = ProjectPolicyUtils.md5(user.password)
            if (nickname == null) {
                nickname = username
            }
        }).apply {
            avatarUrl = Constants.AVATAR_DEFAULT_URL.plus(avatarUrl)
        }
        return genSuccessResult(data = mapOf(
                "token" to ProjectTokenUtils.createJWTToken(data.id, data.username),
                "userInfo" to data.apply {
                    password = null
                }
        ))
    }

    fun login(user: User, orange: Orange, ip: String?): Result {
        ProjectJsonUtils.toJson(user).logi()
        val data = userRepository.findOne(Example.of(user))
        if (!data.isPresent)
            return genFailResult("用户名或密码错误")
        userRepository.save(data.get().apply {
            loginLastTime = System.currentTimeMillis()
            loginLastPlatform = orange.platform
            loginLastVersionCode = orange.versionCode
            loginLastVersionName = orange.versionName
            loginLastDevice = orange.device
            loginLastIp = ip
        })
        return genSuccessResult(data = mapOf(
                "token" to ProjectTokenUtils.createJWTToken(data.get().id, data.get().username),
                "userInfo" to data.get().apply {
                    avatarUrl = Constants.AVATAR_DEFAULT_URL.plus(avatarUrl)
                    password = null
                }
        ), msg = "登录成功")
    }

    fun loginByPhone(user: User, orange: Orange, ip: String?): Result {
        val data = userRepository.findOne(Example.of(user))
        if (!data.isPresent)
            return genFailResult("该手机未绑定任何账号")
        userRepository.save(data.get().apply {
            loginLastTime = System.currentTimeMillis()
            loginLastPlatform = orange.platform
            loginLastVersionCode = orange.versionCode
            loginLastVersionName = orange.versionName
            loginLastDevice = orange.device
            loginLastIp = ip
        })
        return genSuccessResult(data = mapOf(
                "token" to ProjectTokenUtils.createJWTToken(data.get().id, data.get().username),
                "userInfo" to data.get().apply {
                    password = null
                    avatarUrl = Constants.AVATAR_DEFAULT_URL.plus(avatarUrl)
                }
        ), msg = "登录成功")
    }

    fun getUserInfo(user: User): Result {
        val data = userRepository.findOne(Example.of(user))
        if (!data.isPresent)
            return genFailResult("没有找到该用户信息")
        return genSuccessResult(data = data.get().apply {
            password = null
            avatarUrl = Constants.AVATAR_DEFAULT_URL.plus(avatarUrl)
        })
    }

    fun updateUserInfo(uid: Long, type: Int, value: Any): Result {
        val data = userRepository.findById(uid)
        if (!data.isPresent)
            return genFailResult("没有找到该用户信息")
        val user = data.get()
        when (type) {
            UserController.UpdateParam.TYPE_NICKNAME -> {
                user.nickname = value.toString()
            }
            UserController.UpdateParam.TYPE_EMAIL -> {
                user.email = value.toString()
            }
            UserController.UpdateParam.TYPE_LOCATION -> {
                user.location = value.toString()
            }
            UserController.UpdateParam.TYPE_SEX -> {
                user.sex = value.toString().toInt()
                value.toString().logi()
            }
            UserController.UpdateParam.TYPE_SUMMARY -> {
                user.summary = value.toString()
            }
            UserController.UpdateParam.TYPE_AVATAR -> {
                user.avatarUrl = value.toString()
                value.toString().logi()
            }
            else -> {
            }
        }
        return genSuccessResult(data = userRepository.save(user).apply {
            password = null
            avatarUrl = Constants.AVATAR_DEFAULT_URL.plus(avatarUrl)
        })
    }
}