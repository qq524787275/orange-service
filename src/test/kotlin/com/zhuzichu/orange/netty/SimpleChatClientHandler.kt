package com.zhuzichu.orange.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler



/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 13:57
 */
class SimpleChatClientHandler : SimpleChannelInboundHandler<String>() {
    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, s: String) {
        println(s)
    }
}