package com.zhuzichu.orange.model

import com.fasterxml.jackson.annotation.JsonIgnore
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

        @Column(columnDefinition = "COMMENT '密码")
        @field:NotBlank(message = "密码不能为空")
        var password: String? = null,

        @Column(columnDefinition = "COMMENT '电话号")
        @field:NotBlank(message = "手机号不能为空")
        var phone: String? = null,

        @Column(name = "sex", columnDefinition = "tinyint(4) COMMENT '性别 0未设置 1男 2女'")
        var sex: Int? = 0,

        @Column(columnDefinition = "COMMENT '邮箱")
        var email: String? = null,

        @Column(columnDefinition = "COMMENT '昵称")
        var nickname: String? = null,

        @Column(columnDefinition = "COMMENT '所在地")
        var location: String? = null,

        @Column(columnDefinition = "COMMENT '个人简介")
        val summary: String? = null,

        @Column(name = "regist_time", columnDefinition = "varchar(100) COMMENT '用户注册时间'")
        val registTime: Long? = null,

        @Column(name = "login_last_time", columnDefinition = "varchar(100) COMMENT '最后一次登录时间'")
        val loginLastTime: Long? = null,

        @Column(name = "login_last_platform", columnDefinition = "varchar(100) COMMENT '最后一次登录的手机平台'")
        val loginLastPlatform: String? = null,

        @Column(name = "login_last_version_code", columnDefinition = "COMMENT '最后一次登录的App 的版本code")
        val loginLastVersionCode: Int? = null,

        @Column(name = "login_last_version_name", columnDefinition = "varchar(100) COMMENT '最后一次登录的App 的版本name'")
        val loginLastVersionName: String? = null,

        @field:NotBlank(message = "验证码不能为空")
        @Transient
        var code: String? = null
)