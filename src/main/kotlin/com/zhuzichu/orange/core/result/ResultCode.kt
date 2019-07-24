package com.zhuzichu.orange.core.result

enum class ResultCode(val code: Int, val message: String) {
    SUCCESS(1, "SUCCESS"), //成功
    FAIL(-1, "访问失败"), //失败
    UNAUTHORIZED(401, "签名错误"), //未认证（签名错误）
    NOT_FOUND(404, "此接口不存在"), //接口不存在
    INTERNAL_SERVER_ERROR(500, "系统繁忙,请稍后再试"), //服务器内部错误
    INVALID_PARAM(10000, "参数错误")
}
