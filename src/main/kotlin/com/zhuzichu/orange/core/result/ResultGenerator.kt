package com.zhuzichu.orange.core.result

private const val DEFAULT_SUCCESS_MESSAGE = "SUCCESS"

fun genSuccessResult(): Result {
    return Result().apply {
        code = ResultCode.SUCCESS.code
        data = null
        message = DEFAULT_SUCCESS_MESSAGE
    }
}

fun genSuccessResult(data: Any): Result {
    return Result().apply {
        code = ResultCode.SUCCESS.code
        this.data = data
        message = DEFAULT_SUCCESS_MESSAGE
    }
}

fun genFailResult(message: String): Result {
    return Result().apply {
        code = ResultCode.FAIL.code
        data = null
        this.message = message
    }
}
