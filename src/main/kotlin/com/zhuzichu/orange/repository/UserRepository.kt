package com.zhuzichu.orange.repository

import com.zhuzichu.orange.model.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 *@Auther:zhuzichu
 *@Date:2019/7/25 0025
 *@Time:0:05
 *@Desciption:
 **/
interface UserRepository :JpaRepository<User,Long>{
}