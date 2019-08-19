package com.zhuzichu.orange

import org.springframework.test.context.junit4.SpringRunner
import com.taobao.api.request.TbkDgMaterialOptionalRequest
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-19
 * Time: 13:13
 */

@RunWith(SpringRunner::class)
class TaoBaoTests {

    @Test
    fun test() {
        val client = Constants.taobaoClient
        val req = TbkDgMaterialOptionalRequest()
        req.pageSize = 2L
        req.pageNo = 1L
        req.q = "女装"
        req.adzoneId = Constants.TAOBAO_PID
        val rsp = client.execute(req)
        print(rsp.body)
    }
}