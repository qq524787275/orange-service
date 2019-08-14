package com.zhuzichu.orange.service

import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.model.Orange
import com.zhuzichu.orange.model.Version
import com.zhuzichu.orange.repository.VersionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 12:25
 */
@Service
class VersionService {

    @Autowired
    lateinit var versionRepository: VersionRepository

    fun checkUpdate(orange: Orange): Result {
        var isUpdate = false
        val version = versionRepository.findOne(Example.of(Version().apply {
            platform = orange.platform
        }))

        if (!version.isPresent) {
            return genFailResult("没有找到该平台")
        }

        val taget = orange.versionCode ?: 0
        val src = version.get().versionCode ?: 0
        if (taget < src) {
            isUpdate = true
        }
        return genSuccessResult(data = mapOf(
                "isUpdate" to isUpdate,
                "info" to version
        ))
    }
}