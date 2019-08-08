package com.zhuzichu.orange.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-08
 * Time: 15:52
 */
@Table(name = "orange_category")
@Entity
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @field:NotBlank(message = "数据不能为空")
        var name: String? = null,
        var image: String? = null,
        var pid: Long? = null
)