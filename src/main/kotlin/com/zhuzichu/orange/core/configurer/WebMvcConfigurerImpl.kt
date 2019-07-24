package com.zhuzichu.orange.core.configurer

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.alibaba.fastjson.support.config.FastJsonConfig
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
import com.zhuzichu.orange.annotations.Access
import com.zhuzichu.orange.core.exception.ServiceException
import com.zhuzichu.orange.core.ext.logw
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.ResultCode
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.utils.ProjectTokenUtils
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.io.IOException
import java.nio.charset.Charset
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24
 *@Time:21:50
 *@Desciption:
 **/

@Configuration
class WebMvcConfigurerImpl : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val converter = FastJsonHttpMessageConverter()
        val config = FastJsonConfig().apply {
            setSerializerFeatures()
        }
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, //保留空的字段
                SerializerFeature.WriteNullStringAsEmpty, //String null -> ""
                SerializerFeature.WriteNullNumberAsZero)//Number null -> 0
        converter.fastJsonConfig = config
        converter.defaultCharset = Charset.forName("UTF-8")
        converters.add(converter)
    }

    override fun configureHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
        resolvers.add(HandlerExceptionResolver { request, response, handler, e ->
            val result = Result()
            when (e) {
                is ServiceException -> {//业务失败的异常，如“账号或密码错误”
                    result.code = e.code
                    result.message = e.message
                }
                is NoHandlerFoundException -> result.code = ResultCode.NOT_FOUND.code
                is ServletException -> {
                    result.code = ResultCode.FAIL.code
                    result.message = e.message
                }
                is ConstraintViolationException -> {
                    result.code = ResultCode.FAIL.code
                    result.message = e.constraintViolations.toList()[0].message
                }
                else -> {
                    //系统内部异常,不返回给客户端,内部记录错误日志
                    result.code = ResultCode.INTERNAL_SERVER_ERROR.code
                    var message = "未知错误"
                    if (handler is HandlerMethod) {
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.requestURI,
                                handler.bean.javaClass.name,
                                handler.method.name,
                                e.message)
                    } else {
                        e.message?.let {
                            message = it
                        }
                    }
                }
            }
            responseResult(response, result)
            ModelAndView()
        })
    }

    private fun responseResult(response: HttpServletResponse, result: Result) {
        response.characterEncoding = "UTF-8"
        response.setHeader("Content-type", "application/json;charset=UTF-8")
        response.status = 200
        try {
            response.writer.write(JSON.toJSONString(result))
        } catch (ex: IOException) {
        }
    }

    // 解决跨域问题
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
    }


    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptorAdapter() {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                response.setHeader("Access-Control-Allow-Origin", "*")
                val pass = validateSign(request)
                if (pass) {
                    val handlerMethod = handler as HandlerMethod
                    val method = handlerMethod.method
                    val access = method.getAnnotation(Access::class.java) ?: return true
                    if (access.authorities.isNotEmpty()) {
                        val authSet = access.authorities.toList()
                        val username = request.getAttribute("username")
                        if (authSet.contains(username)) {
                            return true
                        }
                    }
                    responseResult(response, genFailResult("该用户没有权限"))
                    return false
                } else {
                    "签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}".logw(
                            this,
                            request.requestURI,
                            getIpAddress(request),
                            JSON.toJSONString(request.parameterMap))
                }
                return super.preHandle(request, response, handler)
            }
        })
    }

    private fun getIpAddress(request: HttpServletRequest): String? {
        var ip: String? = request.getHeader("x-forwarded-for")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim { it <= ' ' }
        }
        return ip
    }

    private fun validateSign(request: HttpServletRequest): Boolean {
        val requestSign = request.getParameter("token")//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        try {
            val map = ProjectTokenUtils.verifyJWTToken(requestSign)
            request.setAttribute("uid", map["uid"]?.asInt())
            request.setAttribute("username", map["username"]?.asString())
            return true
        } catch (e: Exception) {
        }
        return false//比较
    }
}