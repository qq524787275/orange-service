package com.zhuzichu.orange

import com.taobao.api.request.TbkDgMaterialOptionalRequest
import org.junit.Test
import com.taobao.api.request.TbkItemInfoGetRequest
import com.taobao.api.request.TbkShopRecommendGetRequest


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
        print(rsp.body)
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
    fun shop(){
        val client = Constants.taobaoClient
        val req = TbkShopRecommendGetRequest()
        req.userId = 3029542506L
        req.fields = "user_id,shop_title,shop_type,seller_nick,pict_url,shop_url";
        val rsp = client.execute(req)
        println(rsp.body)
    }
}