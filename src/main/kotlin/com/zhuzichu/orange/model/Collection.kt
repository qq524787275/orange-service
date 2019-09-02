package com.zhuzichu.orange.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-09-02
 * Time: 14:45
 */
@Table(name = "orange_collection")
@Entity
data class Collection(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        var user: User? = null,
        @field:NotEmpty(message = "id不能为空")
        var itemId: Long? = null,
        @field:NotBlank(message = "title不能为空")
        var title: String? = null,
        @field:NotBlank(message = "pictUrl不能为空")
        var pictUrl: String? = null,
        var lastTime: Long? = null
)