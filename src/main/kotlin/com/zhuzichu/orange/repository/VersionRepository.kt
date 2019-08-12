package com.zhuzichu.orange.repository

import com.zhuzichu.orange.model.Version
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-12
 * Time: 12:25
 */
interface VersionRepository : JpaRepository<Version, Long> {

}