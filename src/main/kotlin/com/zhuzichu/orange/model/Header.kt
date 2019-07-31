package com.zhuzichu.orange.model

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-07-31
 * Time: 18:23
 */

data class Header(
        var token: String,
        var platform: String,
        var device: String,
        var version_code: Int,
        var version_name: String
)