package com.zhuzichu.orange.service

import com.taobao.api.request.TbkDgMaterialOptionalRequest
import com.taobao.api.request.TbkUatmFavoritesGetRequest
import com.taobao.api.request.TbkUatmFavoritesItemGetRequest
import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.bean.Favorites
import com.zhuzichu.orange.bean.FavoritesItem
import com.zhuzichu.orange.bean.Goods
import com.zhuzichu.orange.controller.TaoBaoController
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import org.springframework.stereotype.Service
import com.taobao.api.response.TbkDgOptimusMaterialResponse
import com.taobao.api.request.TbkDgOptimusMaterialRequest
import com.zhuzichu.orange.bean.Recommend
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

    fun getFavorites(pageNo: Long, pageSize: Long): Favorites? {
        val req = TbkUatmFavoritesGetRequest()
        req.pageNo = pageNo
        req.pageSize = pageSize
        req.fields = "favorites_title,favorites_id,type"
        req.type = 1L
        val rsp = client.execute(req)
        if (rsp.isSuccess)
            return ProjectJsonUtils.fromJson(rsp.body, Favorites::class.java)
        return null
    }

    fun getFavoritesItem(favoritesId: Int): FavoritesItem? {
        val req = TbkUatmFavoritesItemGetRequest()
        req.adzoneId = Constants.TAOBAO_PID
        req.favoritesId = favoritesId.toLong()
        req.fields = "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type"
        val rsp = client.execute(req)
        if (rsp.isSuccess)
            return ProjectJsonUtils.fromJson(rsp.body, FavoritesItem::class.java)
        return null
    }

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

    fun getRecommendGoods(materialId: Long, itemId: Long? = null, pageSize: Long? = 20): Recommend? {
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