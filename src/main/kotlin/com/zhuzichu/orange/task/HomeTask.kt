package com.zhuzichu.orange.task

import com.zhuzichu.orange.bean.GoodsBean
import com.zhuzichu.orange.core.ext.format2
import com.zhuzichu.orange.core.ext.scheme
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.core.service.redis.IRedisService
import com.zhuzichu.orange.core.utils.ProjectJsonUtils
import com.zhuzichu.orange.model.Home
import com.zhuzichu.orange.service.TaoBaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 *@Auther:zhuzichu
 *@Date:2019/8/25 0025
 *@Time:14:16
 *@Desciption:
 **/
@Component
class HomeTask {

    companion object {
        const val HOME_DATA = "HOME_DATA"
    }

    @Autowired
    lateinit var taoBaoService: TaoBaoService
    @Autowired
    lateinit var redisService: IRedisService


    private fun getHomeList(): List<Home> {
        return taoBaoService.getHomeList()
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    fun updateHomeData() {
        val homeResult = getHomeResult()
        redisService[HOME_DATA, ProjectJsonUtils.toJson(homeResult)] = 60 * 61
    }

    fun getHomeResult(): Result {
        val homeList = getHomeList()
        homeList.map {
            val recommendGoods = taoBaoService.getRecommend(it.materialId, pageSize = it.pageSize)
            recommendGoods ?: return genFailResult("获取数据失败")
            val list = recommendGoods.tbk_dg_optimus_material_response.result_list.map_data.map {
                GoodsBean().apply {
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
            it.list = it.list.plus(list)
            it
        }
        return genSuccessResult(data = homeList)
    }
}