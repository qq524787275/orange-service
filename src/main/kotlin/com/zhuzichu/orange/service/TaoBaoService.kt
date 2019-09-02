package com.zhuzichu.orange.service

import com.taobao.api.request.*
import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.controller.TaoBaoController
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import org.springframework.stereotype.Service
import com.taobao.api.response.TbkDgOptimusMaterialResponse
import com.zhuzichu.orange.bean.*
import com.zhuzichu.orange.core.ext.logi


/**
 *@Auther:zhuzichu
 *@Date:2019/8/24 0024
 *@Time:16:00
 *@Desciption:
 **/
@Service
class TaoBaoService {
    val client = Constants.taobaoClient

    fun getGoods(searchParam: TaoBaoController.SearchParam, sort: String?): Goods? {
        val rsp = client.execute(TbkDgMaterialOptionalRequest().also {
            it.pageSize = searchParam.pageSize
            it.pageNo = searchParam.pageNo
            it.adzoneId = Constants.TAOBAO_PID
            it.q = searchParam.keyword
            it.sort = sort
//            it.hasCoupon = true
//            it.startTkRate = 1000
            it.endPrice = 10000
        })
        if (rsp.isSuccess)
            return ProjectJsonUtils.fromJson(rsp.body, Goods::class.java)
        return null
    }

    fun getRecommend(materialId: Long, itemId: Long? = null, pageSize: Long? = 20): Recommend? {
        val req = TbkDgOptimusMaterialRequest()
        req.pageSize = pageSize
        req.adzoneId = Constants.TAOBAO_PID
        req.pageNo = 1L
        req.materialId = materialId
        req.itemId = itemId
        val rsp = client.execute(req)
        if (rsp.isSuccess)
            return ProjectJsonUtils.fromJson(rsp.body, Recommend::class.java)
        return null
    }

}