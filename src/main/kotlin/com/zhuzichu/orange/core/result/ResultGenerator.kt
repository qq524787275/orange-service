package com.zhuzichu.orange.core.result

private const val DEFAULT_SUCCESS_MESSAGE = "SUCCESS"

fun genSuccessResult(): Result {
    return Result().apply {
        code = ResultCode.SUCCESS.code
        data = null
        msg = DEFAULT_SUCCESS_MESSAGE
    }
}

fun genSuccessResult(data: Any? = null, msg: String = DEFAULT_SUCCESS_MESSAGE): Result {
    return Result().apply {
        code = ResultCode.SUCCESS.code
        this.data = data
        this.msg = msg
    }
}

fun genFailResult(message: String): Result {
    return Result().apply {
        code = ResultCode.FAIL.code
        data = null
        this.msg = message
    }
}
