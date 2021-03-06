package com.zhuzichu.orange.repository

import com.zhuzichu.orange.model.Collection
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-09-02
 * Time: 16:36
 */
interface CollectionRepository : JpaRepository<Collection, Long> {
}