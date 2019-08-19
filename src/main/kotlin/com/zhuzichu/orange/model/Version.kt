package com.zhuzichu.orange.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 09:41
 */

@Entity
@Table(name = "orange_version")
data class Version(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @field:NotBlank(message = "版本号不能为空")
        @Column(name = "platform", columnDefinition = "varchar(255) COMMENT '平台'")
        var platform: String? = null,

        var versionCode: Int? = null,

        var versionName: String? = null,

        @Column(name = "url", columnDefinition = "varchar(255) COMMENT '下载链接'")
        var url: String? = null,

        var content: String? = null
)