package com.zhuzichu.orange.bean

/**
 *@Auther:zhuzichu
 *@Date:2019/8/24 0024
 *@Time:15:53
 *@Desciption:
 **/
data class HomeBean(
        val id: Long,
        val name: String,
        val pageSize: Long,
        val showType: Int,
        var list: List<GoodsInfo> = listOf()
)