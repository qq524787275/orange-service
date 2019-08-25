package com.zhuzichu.orange

import com.taobao.api.request.*
import org.junit.Test
import com.taobao.api.response.TbkContentGetResponse
import com.taobao.api.response.TbkDgNewuserOrderSumResponse
import com.taobao.api.response.TbkDgOptimusMaterialResponse
import com.zhuzichu.orange.bean.Goods
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import com.taobao.api.response.TbkUatmFavoritesGetResponse
import com.taobao.api.response.TbkUatmFavoritesItemGetResponse
import com.taobao.api.request.TbkDgOptimusMaterialRequest


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-19
 * Time: 13:13
 */

class TaoBaoTests {

    @Test
    fun search() {
        val client = Constants.taobaoClient
        val req = TbkDgMaterialOptionalRequest()
        req.pageSize = 2L
        req.pageNo = 1L
        req.q = "女装"
        req.adzoneId = Constants.TAOBAO_PID
        val rsp = client.execute(req)
        if (rsp.isSuccess) {
            val goods = ProjectJsonUtils.fromJson(rsp.body, Goods::class.java)
            println(goods.tbk_dg_material_optional_response.result_list.map_data[0].title)
        }
        print(rsp.body)
    }

    @Test
    fun search1() {
        val client = Constants.taobaoClient
        val req = TbkDgOptimusMaterialRequest()
        req.pageSize = 1L
        req.adzoneId = Constants.TAOBAO_PID
        req.pageNo = 1L
        req.itemId = 544092135549L
        val rsp = client.execute(req)
        println(rsp.body)
    }

    @Test
    fun shopDetail() {
        val client = Constants.taobaoClient
        val req = TbkItemInfoGetRequest()
        req.numIids = "564471772318"
        val rsp = client.execute(req)
        print(rsp.body)
    }

    @Test
    fun shop() {
        val client = Constants.taobaoClient
        val req = TbkShopRecommendGetRequest()
        req.userId = 3029542506L
        req.fields = "user_id,shop_title,shop_type,seller_nick,pict_url,shop_url";
        val rsp = client.execute(req)
        println(rsp.body)
    }

    @Test
    fun content() {
        val client = Constants.taobaoClient
        val req = TbkDgNewuserOrderSumRequest()
        req.pageSize = 20L
        req.adzoneId = Constants.TAOBAO_PID
        req.pageNo = 1L
        val rsp = client.execute(req)
        println(rsp.body)
    }

    @Test
    fun favorites() {
        val client = Constants.taobaoClient
        val req = TbkUatmFavoritesGetRequest()
        req.pageNo = 1L
        req.pageSize = 20L
        req.fields = "favorites_title,favorites_id,type"
        req.type = 1L
        val rsp = client.execute(req)
        println(rsp.body)
    }

    @Test
    fun favoritesItem() {
        val client = Constants.taobaoClient
        val req = TbkUatmFavoritesItemGetRequest()
        req.adzoneId = Constants.TAOBAO_PID
        req.favoritesId = 19735860L
        req.fields = "coupon_click_url,num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type"
        val rsp = client.execute(req)
        println(rsp.body)
    }

    @Test
    fun getItems() {
        val client = Constants.taobaoClient
        val req = TbkDgOptimusMaterialRequest()
        req.pageSize = 20L
        req.adzoneId = Constants.TAOBAO_PID
        req.pageNo = 1L
        req.materialId = 3756L
//        req.itemId = 544092135549L
        val rsp = client.execute(req)
        println(rsp.body)
    }
}