package com.zhuzichu.orange.controller

import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.bean.GoodsInfo
import com.zhuzichu.orange.bean.HomeBean
import com.zhuzichu.orange.core.ext.format2
import com.zhuzichu.orange.core.ext.scheme
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.service.redis.IRedisService
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import com.zhuzichu.orange.service.TaoBaoService
import com.zhuzichu.orange.task.HomeTask
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    lateinit var taoBaoService: TaoBaoService
    @Autowired
    lateinit var redisService: IRedisService

    @PostMapping("/getHomeData")
    fun getHomeData(): String {
        val json = redisService[HomeTask.HOME_DATA] ?: return ProjectJsonUtils.toJson(genFailResult("数据异常"))
        return json
    }


    @PostMapping("/getRecommend")
    @Encrypt
    fun getRecommend(@RequestBody recommendParam: RecommendParam): Result {
        val recommendGoods = taoBaoService.getRecommendGoods(13366L, recommendParam.itemId)
                ?: return genFailResult("获取数据失败")
        return genSuccessResult(data = recommendGoods.tbk_dg_optimus_material_response.result_list.map_data
                .map {
                    GoodsInfo().apply {
                        itemid = it.item_id.toString()
                        itempic = it.pict_url.scheme()
                        itemshorttitle = it.title
                        itemprice = it.zk_final_price
                        itemendprice = (it.zk_final_price.toDouble() - it.coupon_amount.toDouble()).format2()
                        itemsale = it.volume.toString()
                        itemtitle = it.title
                        couponmoney = it.coupon_amount.toString()
                        shoptype = if (it.user_type == 1) "B" else "C"
                        smallimages = it.small_images.string.map { url ->
                            url.scheme()
                        }
                        couponurl = it.coupon_share_url.scheme()
                    }
                }
        )
    }

    @PostMapping("/search")
    @Encrypt
    fun search(@RequestBody searchParam: SearchParam): Result {
        val sort: String?
        when (searchParam.sort) {
            1 -> {
                sort = "price".plus("_asc")
            }
            2 -> {
                sort = "price".plus("_des")
            }
            4 -> {
                sort = "total_sales".plus("_des")
            }
            else -> {
                sort = null
            }
        }
        val goods = taoBaoService.getGoods(searchParam, sort)
                ?: return genFailResult("获取数据失败")
        return genSuccessResult(data = goods.tbk_dg_material_optional_response.result_list.map_data.map {
            GoodsInfo().apply {
                itemid = it.item_id.toString()
                itempic = it.pict_url
                itemshorttitle = it.short_title
                itemprice = it.zk_final_price
                itemendprice = (it.zk_final_price.toDouble() - it.coupon_amount?.toDouble()).format2()
                itemsale = it.volume.toString()
                itemtitle = it.title
                couponmoney = it.coupon_amount
                shoptype = if (it.user_type == 1) "B" else "C"
                smallimages = it.small_images.string
                couponurl = it.coupon_share_url.scheme()
            }
        })
    }

    //---------------------------------------参数分界线---------------------------------------

    data class RecommendParam(
            @field:NotBlank(message = "itemId 不能为空")
            val itemId: Long
    )

    data class SearchParam(
            @field:NotBlank(message = "pageSize 不能为空")
            val pageSize: Long,
            @field:NotBlank(message = "pageNo 不能为空")
            val pageNo: Long,
            @field:NotBlank(message = "keyWord 不能为空")
            val keyword: String,
            val sort: Int? = null
    )
}