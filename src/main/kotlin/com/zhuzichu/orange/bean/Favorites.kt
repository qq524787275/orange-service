package com.zhuzichu.orange.bean

/**
 *@Auther:zhuzichu
 *@Date:2019/8/24 0024
 *@Time:15:44
 *@Desciption:
 **/
data class Favorites(
        var tbk_uatm_favorites_get_response: TbkUatmFavoritesGetResponse = TbkUatmFavoritesGetResponse()
) {
    data class TbkUatmFavoritesGetResponse(
            var request_id: String = "",
            var results: Results = Results(),
            var total_results: Int = 0
    ) {
        data class Results(
                var tbk_favorites: List<TbkFavorite> = listOf()
        ) {
            data class TbkFavorite(
                    var favorites_id: Int = 0,
                    var favorites_title: String = "",
                    var type: Int = 0
            )
        }
    }
}