package com.zhuzichu.orange.bean

/**
 *@Auther:zhuzichu
 *@Date:2019/8/24 0024
 *@Time:15:45
 *@Desciption:
 **/

data class FavoritesItem(
        var tbk_uatm_favorites_item_get_response: TbkUatmFavoritesItemGetResponse = TbkUatmFavoritesItemGetResponse()
) {
    data class TbkUatmFavoritesItemGetResponse(
            var request_id: String = "",
            var results: Results = Results(),
            var total_results: Int = 0
    ) {
        data class Results(
                var uatm_tbk_item: List<UatmTbkItem> = listOf()
        ) {
            data class UatmTbkItem(
                    var event_end_time: String = "",
                    var event_start_time: String = "",
                    var item_url: String = "",
                    var nick: String = "",
                    var num_iid: Long = 0,
                    var pict_url: String = "",
                    var provcity: String = "",
                    var reserve_price: String = "",
                    var seller_id: Int = 0,
                    var shop_title: String = "",
                    var small_images: SmallImages = SmallImages(),
                    var status: Int = 0,
                    var title: String = "",
                    var tk_rate: String = "",
                    var type: Int = 0,
                    var user_type: Int = 0,
                    var volume: Int = 0,
                    var zk_final_price: String = "",
                    var zk_final_price_wap: String = ""
            ) {
                data class SmallImages(
                        var string: List<String> = listOf()
                )
            }
        }
    }
}