package com.zhuzichu.orange.core.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.zhuzichu.orange.Constants
import java.util.*

/**
 *@Auther:zhuzichu
 *@Date:2019/7/24
 *@Time:22:20
 *@Desciption:
 **/
object ProjectTokenUtils {

    private const val SECRET = "WETHIS123456"

    /**
     * 生成token
     *
     * @param uid      用户id
     * @param username 用户姓名
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun createJWTToken(uid: Long?, username: String?): String {
        return JWT.create()
                .withHeader(mapOf(
                        "alg" to "HS256",
                        "typ" to "JWT"
                ))
                .withClaim(Constants.KEY_USER_ID, uid)
                .withClaim(Constants.KEY_USER_USERNAME, username)
                .withExpiresAt(Calendar.getInstance().apply {
                    add(Calendar.HOUR, 24)
                }.time)
                .withIssuedAt(Date())
                .sign(Algorithm.HMAC256(SECRET))
    }

    @Throws(Exception::class)
    fun verifyJWTToken(token: String?): Map<String, Claim> {
        val jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build()
        try {
            return jwtVerifier.verify(token).claims
        } catch (e: Exception) {
            throw RuntimeException("登录凭证已经过期，请重新登录")
        }
    }
}