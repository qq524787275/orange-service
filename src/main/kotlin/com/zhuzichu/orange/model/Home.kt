package com.zhuzichu.orange.model

import com.zhuzichu.orange.bean.GoodsBean
import javax.persistence.*

/**
 *@Auther:zhuzichu
 *@Date:2019/8/28 0028
 *@Time:11:51
 *@Desciption:
 **/
@Entity
@Table(name = "orange_home")
class Home(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var materialId: Long? = null,

        var name: String? = null,

        var pageSize: Long? = null,

        var showType: Int? = null,

        @Transient
        var list: List<GoodsBean> = listOf(),

        @Transient
        var extension: Any? = null
)