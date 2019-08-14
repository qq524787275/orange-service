package com.zhuzichu.orange.controller

import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.model.Orange
import com.zhuzichu.orange.service.VersionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 12:24
 */

@RestController
@RequestMapping(Constants.API_VERSION)
class VersionController {

    @Autowired
    lateinit var versionService: VersionService

    @PostMapping("/checkUpdate")
    fun checkUpdate(httpRequest: HttpServletRequest): Result {
        val orange = httpRequest.getAttribute(Constants.KEY_ORANGE) as Orange
        return versionService.checkUpdate(orange)
    }
}