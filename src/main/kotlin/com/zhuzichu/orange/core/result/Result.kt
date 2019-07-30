package com.zhuzichu.orange.core.result

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Result(
        var code: Int? = null,
        var msg: String? = null,
        var data: Any? = null
)