package com.zhuzichu.orange.bean

data class Recommend(
        var tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse = TbkDgOptimusMaterialResponse()
) {
    data class TbkDgOptimusMaterialResponse(
            var is_default: String = "",
            var request_id: String = "",
            var result_list: ResultList = ResultList()
    ) {
        data class ResultList(
                var map_data: List<MapData> = listOf()
        ) {
            data class MapData(
                    var category_id: Int = 0,
                    var category_name: String = "",
                    var click_url: String = "",
                    var commission_rate: String = "",
                    var coupon_amount: Int = 0,
                    var coupon_click_url: String = "",
                    var coupon_end_time: String = "",
                    var coupon_info: String = "",
                    var coupon_remain_count: Int = 0,
                    var coupon_share_url: String = "",
                    var coupon_start_fee: String = "",
                    var coupon_start_time: String = "",
                    var coupon_total_count: Int = 0,
                    var item_description: String = "",
                    var item_id: Long = 0,
                    var level_one_category_id: Int = 0,
                    var level_one_category_name: String = "",
                    var nick: String = "",
                    var pict_url: String = "",
                    var seller_id: Long = 0,
                    var shop_title: String = "",
                    var small_images: SmallImages = SmallImages(),
                    var title: String = "",
                    var user_type: Int = 0,
                    var volume: Int = 0,
                    var zk_final_price: String = ""
            ) {
                data class SmallImages(
                        var string: List<String> = listOf()
                )
            }
        }
    }
}

