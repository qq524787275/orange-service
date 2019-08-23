package com.zhuzichu.orange.service

import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import com.zhuzichu.orange.bean.Orange
import com.zhuzichu.orange.model.Version
import com.zhuzichu.orange.repository.VersionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
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
        val versions = versionRepository.findAll(Example.of(Version().apply {
            platform = orange.platform
        }), Sort(Sort.Direction.DESC, "versionCode"))

        if (versions.isEmpty()) {
            return genFailResult("没有找到该平台")
        }

        val lastVersion = versions[0]
        val taget = orange.versionCode ?: 0
        val src = lastVersion.versionCode ?: 0
        if (taget < src) {
            isUpdate = true
        }
        return genSuccessResult(data = mapOf(
                "isUpdate" to isUpdate,
                "info" to lastVersion
        ))
    }
}