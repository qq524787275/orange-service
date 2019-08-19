package com.zhuzichu.orange.controller

import com.alibaba.fastjson.JSONObject
import com.taobao.api.request.TbkDgMaterialOptionalRequest
import com.zhuzichu.orange.Constants
import com.zhuzichu.orange.annotations.Encrypt
import com.zhuzichu.orange.core.result.Result
import com.zhuzichu.orange.core.result.genFailResult
import com.zhuzichu.orange.core.result.genSuccessResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotBlank

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-19
 * Time: 13:57
 */
@RestController
@RequestMapping(Constants.API_TAOBAO)
class TaoBaoController {

    @PostMapping("/seach")
    @Encrypt
    fun search(@RequestBody searchParam: SearchParam): Result {
        val client = Constants.taobaoClient
        val rsp = client.execute(TbkDgMaterialOptionalRequest().apply {
            pageSize = searchParam.pageSize
            pageNo = searchParam.pageNo
            adzoneId = Constants.TAOBAO_PID
            q = searchParam.keyWord
            sort = searchParam.sort
        })
        if (rsp.isSuccess) {
            return genSuccessResult(data = JSONObject.parse(rsp.body))
        }
        return genFailResult(rsp.subMsg)
    }

    //---------------------------------------参数分界线---------------------------------------
    data class SearchParam(
            @field:NotBlank(message = "pageSize 不能为空")
            val pageSize: Long,
            @field:NotBlank(message = "pageNo 不能为空")
            val pageNo: Long,
            @field:NotBlank(message = "keyWord 不能为空")
            val keyWord: String,
            val sort: String? = null
    )
}