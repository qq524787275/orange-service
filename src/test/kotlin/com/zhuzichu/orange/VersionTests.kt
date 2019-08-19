package com.zhuzichu.orange

import com.zhuzichu.orange.model.Version
import com.zhuzichu.orange.repository.VersionRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Example
import org.springframework.test.context.junit4.SpringRunner


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 11:33
 */

@RunWith(SpringRunner::class)
@SpringBootTest
class VersionTests {
    @Autowired
    lateinit var versionRepository: VersionRepository

    val info = """<p><b>更新内容</b></p><p><i>1.修复若干bug</i></p><p><i>2.添加若干功能</i></p><p><i>3.优化若干布局</i></p>"""

    @Test
    fun addAndroidVersion() {
        versionRepository.save(Version().apply {
            platform = "android"
            versionCode = 1
            versionName = "1.0.0"
            url = "http://zhuzichu.com/app-release.apk"
            content = info
        })
    }

    @Test
    fun updateAndroidVersion() {
        val version = versionRepository.findOne(Example.of(Version().apply {
            platform = "android"
        }))

        if (version.isPresent) {
            versionRepository.save(version.get().apply {
                versionCode = 2
                versionName = "1.0.1"
            })
        }
    }
}