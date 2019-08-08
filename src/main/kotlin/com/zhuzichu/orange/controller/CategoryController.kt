package com.zhuzichu.orange.controller

import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotBlank

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-08
 * Time: 15:53
 */

@RestController
@RequestMapping(Constants.API_CATEGORY)
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @PostMapping("/getCategory")
    fun getCategory(@RequestBody categoryParam: CategoryParam): Result {
        return categoryService.getCategory(categoryParam.pid)
    }


    data class CategoryParam(
            @field:NotBlank(message = "pid不能为空")
            val pid: Long
    )
}