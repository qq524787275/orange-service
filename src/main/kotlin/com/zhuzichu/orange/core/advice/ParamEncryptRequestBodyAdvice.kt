package com.zhuzichu.orange.core.advice

import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.core.ext.logi
import com.zhuzichu.orange.core.utils.ProjectPolicyUtils
import org.apache.commons.io.IOUtils
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.lang.reflect.Type
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:21:25
 *@Desciption:
 **/

@RestControllerAdvice
class ParamEncryptRequestBodyAdvice : RequestBodyAdvice {

    override fun supports(methodParameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return methodParameter.hasMethodAnnotation(Encrypt::class.java) && methodParameter.hasParameterAnnotation(RequestBody::class.java)
    }

    override fun handleEmptyBody(body: Any?, inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Any? {
        return body
    }

    override fun beforeBodyRead(inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): HttpInputMessage {
        return object : HttpInputMessage {
            override fun getBody(): InputStream {
                return ByteArrayInputStream(
                        URLDecoder.decode(ProjectPolicyUtils.decryptPolicy(IOUtils.toString(inputMessage.body, StandardCharsets.UTF_8)), "UTF-8")
                                .toByteArray(StandardCharsets.UTF_8)
                )
            }

            override fun getHeaders(): HttpHeaders {
                return inputMessage.headers
            }
        }
    }

    override fun afterBodyRead(body: Any, inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Any {
        return body
    }

}