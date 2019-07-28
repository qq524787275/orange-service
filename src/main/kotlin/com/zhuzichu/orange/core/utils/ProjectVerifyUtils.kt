package com.zhuzichu.orange.core.utils

import com.zhuzichu.orange.core.exception.ServiceException
import com.zhuzichu.orange.core.result.ResultCode
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 *@Auther:zhuzichu
 *@Date:2019/7/28 0028
 *@Time:18:54
 *@Desciption:
 **/

object ProjectVerifyUtils{

    /**
     * 正则表达式 数字+英文字母
     */
    val REGX_ALPHABETA_NUM = "^[a-zA-Z0-9]$"

    /**
     * 中国大陆手机号
     */
    val REGX_CN_MOBILE = "^(0086|[+]86)?1\\d{10}"

    /**
     * 邮编
     */
    val REGEX_POSTCODE = "^[0-9]{6}$"

    /**
     * 用于校验 0, 1
     */
    val PEGEX_YES_OR_NO = "^[0-1]{1}$"


    @Throws(ServiceException::class)
    fun verifyPhoneNo(phoneNo: String, throwException: Boolean, fieldName: String): Boolean {
        notNull(phoneNo, fieldName, true)
        if (phoneNo.matches("^1[0-9]{10}".toRegex())) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, fieldName)
        }
        return false
    }


    @Throws(ServiceException::class)
    fun notBlank(value: String, throwException: Boolean): Boolean {
        return notBlank(value, "", throwException)
    }

    /**
     * 非空白判断
     *
     * @param value
     * @param paramName
     * @param throwException
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun notBlank(value: String, paramName: String, throwException: Boolean): Boolean {
        val result = StringUtils.isNotBlank(value)
        if (result) {
            return true
        }
        return if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, paramName)
        } else {
            false
        }
    }

    @Throws(ServiceException::class)
    fun greaterThan(value: Int, compareValue: Int, paramName: String, throwException: Boolean): Boolean {
        val result = value > compareValue
        if (result) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, "$paramName=$value")
        }
        return false
    }

    @Throws(ServiceException::class)
    fun greaterThan(value: Long, compareValue: Long, paramName: String, throwException: Boolean): Boolean {
        val result = value > compareValue
        if (result) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, "$paramName=$value")
        }
        return false
    }

    @Throws(ServiceException::class)
    fun greaterThan(value: Double, compareValue: Double, paramName: String, throwException: Boolean): Boolean {
        val result = value > compareValue
        if (result) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, "$paramName=$value")
        }
        return false
    }

    @Throws(ServiceException::class)
    fun maxStringLength(value: String, maxLength: Int, paramName: String, allowBlank: Boolean,
                        throwException: Boolean): Boolean {
        //非空校验
        val isNotBlank = notBlank(value, false)
        if (!isNotBlank) {
            if (allowBlank) {
                return true
            }
            if (throwException) {
                throw ServiceException("$paramName=$value")
            }
            return false
        }
        //字符串长度校验
        if (value.length > maxLength) {
            if (throwException) {
                throw ServiceException("$paramName=$value")
            }
            return false
        }
        return true
    }

    /**
     * 检查数据最小字符串长度
     *
     * @param value
     * @param minLength
     * @param paramName
     * @param allowBlank
     * @param throwException
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun minStringLength(value: String, minLength: Int, paramName: String, allowBlank: Boolean,
                        throwException: Boolean): Boolean {
        //非空校验
        if (!notBlank(value, false)) {
            if (allowBlank) {
                return true
            }
            if (throwException) {
                throw ServiceException("$paramName=$value")
            }
            return false
        }

        //字符串长度校验
        if (value.length < minLength) {
            if (throwException) {
                throw ServiceException("$paramName=$value")
            }
            return false
        }
        return true
    }


    @Throws(ServiceException::class)
    fun contains(value: Int, container: IntArray?, paramName: String, throwException: Boolean): Boolean {
        if (container == null) {
            throw ServiceException("container")
        }
        val result = ArrayUtils.contains(container, value)
        if (result) {
            return true
        }
        if (throwException) {
            throw ServiceException("$paramName=$value")
        }
        return false
    }

    /**
     * 非空判断
     *
     * @param obj
     * @param throwException
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun notNull(obj: Any?, paramName: String, throwException: Boolean): Boolean {
        if (obj == null) {
            if (throwException) {
                throw ServiceException("$paramName is null")
            }
            return false
        }
        return true
    }


    /**
     * 非空白判断
     *
     * @param field
     * @param throwException
     * @param fieldName
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun notBlank(field: String, throwException: Boolean, fieldName: String): Boolean {
        if (StringUtils.isNotBlank(field)) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, "$fieldName 不能为空白")
        }
        return false
    }

    /**
     * 根据正则表达式判断参数合法性
     *
     * @param field
     * @param regex
     * @param throwException
     * @param fieldName
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun verifyByRegex(field: String, regex: String, throwException: Boolean, fieldName: String): Boolean {
        notNull(field, fieldName, true)
        if (field.matches(regex.toRegex())) {
            return true
        }
        if (throwException) {
            throw ServiceException("$fieldName=$field")
        }
        return false
    }

    /**
     * 日期字符串校验
     *
     * @param value
     * @param format
     * @param fieldName
     * @param throwException
     * @return
     * @throws ServiceException
     */
    @Throws(ServiceException::class)
    fun verifyDate(value: String, format: String, fieldName: String, throwException: Boolean): Boolean {
        val result = notNull(value, fieldName, throwException)
        if (!result) {
            return result
        }
        val sdf = SimpleDateFormat(format)
        var date: Date?
        try {
            date = sdf.parse(value)
        } catch (e: ParseException) {
            return if (throwException) {
                throw ServiceException(ResultCode.INVALID_PARAM, "$fieldName=$value")
            } else {
                false
            }
        }
        val actValue = sdf.format(date)
        if (StringUtils.equals(value, actValue)) {
            return true
        }
        if (throwException) {
            throw ServiceException(ResultCode.INVALID_PARAM, "$fieldName=$value")
        }
        return false
    }

    fun getVerifyCode4(): Int {
        return ((Math.random() * 9 + 1) * 1000).toInt()
    }

    fun getVerifyCode5(): Int {
        return ((Math.random() * 9 + 1) * 10000).toInt()
    }

    fun getVerifyCode6(): Int {
        return ((Math.random() * 9 + 1) * 100000).toInt()
    }
}