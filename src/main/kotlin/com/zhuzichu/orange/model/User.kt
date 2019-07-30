package com.zhuzichu.orange.model

import com.fasterxml.jackson.annotation.JsonInclude
import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24 0024
 *@Time:23:54
 *@Desciption:
 **/
@Entity
@Table(name = "orange_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @field:NotBlank(message = "账号不能为空")
        @Column(name = "username", columnDefinition = "varchar(100) COMMENT '用户名'")
        var username: String? = null,

        @field:NotBlank(message = "密码不能为空")
        var password: String? = null,

        @field:NotBlank(message = "手机号不能为空")
        var phone: String? = null,

        @Column(name = "sex", columnDefinition = "tinyint(4) COMMENT '性别 0未设置 1男 2女'")
        var sex: Int? = 0,

        var email: String? = null,

        var nickname: String? = null,

        @field:NotBlank(message = "验证码不能为空")
        @Transient
        var code: String? = null
)