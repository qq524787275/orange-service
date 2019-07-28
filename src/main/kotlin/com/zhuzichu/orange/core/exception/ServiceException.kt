package com.zhuzichu.orange.core.exception

import com.zhuzichu.orange.core.result.ResultCode
import java.lang.Exception

class ServiceException : Exception {
    var code: Int = 0
    lateinit var extraMessage: String

    constructor (extraMessage: String) : this(ResultCode.INVALID_PARAM, extraMessage)

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        this.code = resultCode.code
    }

    constructor(code: Int, message: String, extraMessage: String, cause: Throwable?) : super(message, cause) {
        this.code = code
        this.extraMessage = extraMessage
    }

    constructor(resultCode: ResultCode, extraMessage: String) : this(resultCode.code, resultCode.msg, extraMessage, null)
}