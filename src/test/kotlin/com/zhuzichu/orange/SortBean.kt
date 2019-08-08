package com.zhuzichu.orange

data class SortBean(
        var `data`: List<Data> = listOf(),
        var cid: Int = 0,
        var main_name: String = ""
) {
    data class Data(
            var info: List<Info> = listOf(),
            var next_name: String = ""
    ) {
        data class Info(
                var imgurl: String = "",
                var son_name: String = ""
        )
    }
}
