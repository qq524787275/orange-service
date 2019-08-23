package com.zhuzichu.orange.controller

import com.alibaba.fastjson.JSONObject
import com.taobao.api.request.TbkDgMaterialOptionalRequest
import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.bean.Goods
import com.zhuzichu.orange.bean.GoodsInfo
import com.zhuzichu.orange.core.ext.format2
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotBlank

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-19
 * Time: 13:57
 */
@RestController
@RequestMapping(Constants.API_TAOBAO)
class TaoBaoController {

    @PostMapping("/seach")
    @Encrypt
    fun search(@RequestBody searchParam: SearchParam): Result {
        val client = Constants.taobaoClient
        val rsp = client.execute(TbkDgMaterialOptionalRequest().apply {
            pageSize = searchParam.pageSize
            pageNo = searchParam.pageNo
            adzoneId = Constants.TAOBAO_PID
            q = searchParam.keyWord
            sort = searchParam.sort
            hasCoupon = true
            startTkRate = 1000
            endPrice = 10000
        })
        if (rsp.isSuccess) {
            val goods = ProjectJsonUtils.fromJson(rsp.body, Goods::class.java)
            return genSuccessResult(data = goods.tbk_dg_material_optional_response.result_list.map_data.map {
                GoodsInfo().apply {
                    itemid = it.item_id.toString()
                    itempic = it.pict_url
                    itemshorttitle = it.short_title
                    itemprice = it.zk_final_price
                    itemendprice = (it.zk_final_price.toDouble() - it.coupon_amount.toDouble()).format2()
                    itemsale = it.tk_total_sales
                    couponmoney = it.coupon_amount
                    smallimages = it.small_images.string
                }
            })
        }
        return genFailResult(rsp.subMsg)
    }

    //---------------------------------------参数分界线---------------------------------------
    data class SearchParam(
            @field:NotBlank(message = "pageSize 不能为空")
            val pageSize: Long,
            @field:NotBlank(message = "pageNo 不能为空")
            val pageNo: Long,
            @field:NotBlank(message = "keyWord 不能为空")
            val keyWord: String,
            val sort: String? = null
    )
}