package com.zhuzichu.orange.bean

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-26
 * Time: 18:08
 */
class RecommendGoods(
        var tbk_item_recommend_get_response: TbkItemRecommendGetResponse = TbkItemRecommendGetResponse()
) {
    data class TbkItemRecommendGetResponse(
            var request_id: String = "",
            var results: Results = Results()
    ) {
        data class Results(
                var n_tbk_item: List<NTbkItem> = listOf()
        ) {
            data class NTbkItem(
                    var item_url: String = "",
                    var num_iid: Long = 0,
                    var pict_url: String = "",
                    var provcity: String = "",
                    var reserve_price: String = "",
                    var small_images: SmallImages = SmallImages(),
                    var title: String = "",
                    var user_type: Int = 0,
                    var zk_final_price: String = ""
            ) {
                data class SmallImages(
                        var string: List<String> = listOf()
                )
            }
        }
    }
}