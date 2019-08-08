package com.zhuzichu.orange.repository

import com.zhuzichu.orange.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category,Long>
