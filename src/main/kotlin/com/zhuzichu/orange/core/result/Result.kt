package com.zhuzichu.orange.core.result

data class Result(
        var code: Int? = null,
        var message: String? = null,
        var data: Any? = null
)