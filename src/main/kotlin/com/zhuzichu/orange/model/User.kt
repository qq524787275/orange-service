package com.zhuzichu.orange.model

import com.fasterxml.jackson.annotation.JsonInclude
import javax.persistence.*
import javax.validation.constraints.NotBlank
import kotlin.jvm.Transient

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

        @Column(columnDefinition = "varchar(100) COMMENT '密码'")
        @field:NotBlank(message = "密码不能为空")
        var password: String? = null,

        @Column(columnDefinition = "varchar(100) COMMENT '电话号'")
        @field:NotBlank(message = "手机号不能为空")
        var phone: String? = null,

        @Column(name = "sex", columnDefinition = "tinyint(4) COMMENT '性别 0未设置 1男 2女'")
        var sex: Int? = null,

        @Column(name = "avatar_url", columnDefinition = "varchar(255) COMMENT '头像url'")
        var avatarUrl: String? = null,

        @Column(columnDefinition = "varchar(100) COMMENT '邮箱'")
        var email: String? = null,

        @Column(columnDefinition = "varchar(20) COMMENT '昵称'")
        var nickname: String? = null,

        @Column(columnDefinition = "varchar(100) COMMENT '所在地'")
        var location: String? = null,

        @Column(columnDefinition = "varchar(255) COMMENT '个人简介'")
        var summary: String? = null,

        @Column(name = "regist_time", columnDefinition = "varchar(100) COMMENT '用户注册时间'")
        var registTime: Long? = null,

        @Column(name = "login_last_time", columnDefinition = "varchar(100) COMMENT '最后一次登录时间'")
        var loginLastTime: Long? = null,

        @Column(name = "login_last_platform", columnDefinition = "varchar(100) COMMENT '最后一次登录的手机平台'")
        var loginLastPlatform: String? = null,

        @Column(name = "login_last_version_code", columnDefinition = "int(10) COMMENT '最后一次登录的App 的版本code'")
        var loginLastVersionCode: Int? = null,

        @Column(name = "login_last_version_name", columnDefinition = "varchar(100) COMMENT '最后一次登录的App 的版本name'")
        var loginLastVersionName: String? = null,

        @Column(name = "login_last_device", columnDefinition = "varchar(100) COMMENT '最后一次登录的设备信息'")
        var loginLastDevice: String? = null,

        @Column(name = "login_last_ip", columnDefinition = "varchar(100) COMMENT '最后一次登录的ip地址'")
        var loginLastIp: String? = null,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var foots: List<Foot>? = null,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var collections: List<Collection>? = null,

        @Transient
        var code: String? = null
)