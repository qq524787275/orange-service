package com.zhuzichu.orange.core.exception

import com.zhuzichu.orange.core.result.ResultCode
import java.lang.Exception

class ServiceException : Exception {
    var code: Int = 0
    lateinit var extraMessage: String

    constructor(resultCode: ResultCode) : super(resultCode.message) {
        this.code = resultCode.code
    }

    constructor(code: Int, message: String, extraMessage: String, cause: Throwable?) : super(message, cause) {
        this.code = code
        this.extraMessage = message
    }

    constructor(resultCode: ResultCode, extraMessage: String) : this(resultCode.code, resultCode.message, extraMessage, null)
}