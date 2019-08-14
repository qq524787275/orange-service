package com.zhuzichu.orange.core.ext

import com.zhuzichu.orange.Constants
import org.springframework.web.servlet.config.annotation.InterceptorRegistration

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-07-31
 * Time: 11:43
 */

fun InterceptorRegistration.addDefaultPath() {
    this.addPathPatterns("/**")
            .excludePathPatterns(Constants.API_USER.plus("/regist"))
            .excludePathPatterns(Constants.API_USER.plus("/login"))
            .excludePathPatterns(Constants.API_USER.plus("/loginByPhone"))
            .excludePathPatterns(Constants.API_SMS.plus("/**"))
            .excludePathPatterns(Constants.API_CATEGORY.plus("/**"))
            .excludePathPatterns(Constants.API_VERSION .plus("/**"))
}