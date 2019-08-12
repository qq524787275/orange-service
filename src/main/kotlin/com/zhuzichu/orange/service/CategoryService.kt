package com.zhuzichu.orange.service

import com.zhuzichu.orange.core.ext.logi
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.model.Category
import com.zhuzichu.orange.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-08
 * Time: 15:53
 */
@Service
class CategoryService {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    fun getCategory(pid: Long): Result {
        if (pid == -1L) {
            return genSuccessResult(data = categoryRepository.findAll())
        }
        return genSuccessResult(data = categoryRepository.findAll(Example.of(Category().apply {
            this.pid = pid
        })))
    }

}