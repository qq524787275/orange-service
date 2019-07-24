package com.zhuzichu.orange.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24 0024
 *@Time:23:54
 *@Desciption:
 **/
@Entity
@Table(name = "orange_user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @field:NotBlank(message = "账号不能为空")
        var username: String? = null,
        @field:NotBlank(message = "密码不能为空")
        var password: String? = null
)