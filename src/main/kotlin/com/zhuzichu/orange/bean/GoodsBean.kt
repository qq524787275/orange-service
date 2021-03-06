package com.zhuzichu.orange.bean

import java.io.Serializable

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-23
 * Time: 18:22
 */
data class GoodsBean(
        var activity_type: String = "",
        var activityid: String = "",
        var coupon_condition: String = "",
        var couponendtime: String = "",
        var couponexplain: String = "",
        var couponmoney: String = "",
        var couponnum: String = "",
        var couponreceive: String = "",
        var couponreceive2: String = "",
        var couponstarttime: String = "",
        var couponsurplus: String = "",
        var couponurl: String = "",
        var cuntao: String = "",
        var discount: String = "",
        var down_type: String = "",
        var end_time: String = "",
        var fqcat: String = "",
        var general_index: String = "",
        var guide_article: String = "",
        var is_brand: String = "",
        var is_explosion: String = "",
        var is_live: String = "",
        var itemdesc: String = "",
        var itemendprice: String = "",
        var itemid: String = "",
        var itempic: String = "",
        var itempic_copy: String = "",
        var itemprice: String = "",
        var itemsale: String = "",
        var itemsale2: String = "",
        var itemshorttitle: String = "",
        var itemtitle: String = "",
        var online_users: String = "",
        var original_article: String = "",
        var original_img: String = "",
        var product_id: String = "",
        var report_status: String = "",
        var seller_id: String = "",
        var seller_name: String = "",
        var sellernick: String = "",
        var shopid: String = "",
        var shopname: String = "",
        var shoptype: String = "",
        var son_category: String = "",
        var start_time: String = "",
        var starttime: String = "",
        var taobao_image: String = "",
        var tkmoney: String = "",
        var tkrates: String = "",
        var tktype: String = "",
        var todaycouponreceive: String = "",
        var todaysale: String = "",
        var userid: String = "",
        var videoid: String = "",
        var smallimages: List<String> = listOf()
):Serializable