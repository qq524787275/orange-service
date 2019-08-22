package com.zhuzichu.orange.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.util.concurrent.GlobalEventExecutor
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.group.ChannelGroup
import io.netty.channel.SimpleChannelInboundHandler



/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 14:41
 */
class TextWebSocketFrameHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext,
                              msg: TextWebSocketFrame) { // (1)
        val incoming = ctx.channel()
        for (channel in channels) {
            if (channel !== incoming) {
                channel.writeAndFlush(TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()))
            } else {
                channel.writeAndFlush(TextWebSocketFrame("[you]" + msg.text()))
            }
        }
    }

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext) {  // (2)
        val incoming = ctx.channel()
        for (channel in channels) {
            channel.writeAndFlush(TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"))
        }
        channels.add(ctx.channel())
        System.out.println("Client:" + incoming.remoteAddress() + "加入")
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {  // (3)
        val incoming = ctx.channel()
        for (channel in channels) {
            channel.writeAndFlush(TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"))
        }
        System.out.println("Client:" + incoming.remoteAddress() + "离开")
        channels.remove(ctx.channel())
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) { // (5)
        val incoming = ctx.channel()
        System.out.println("Client:" + incoming.remoteAddress() + "在线")
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) { // (6)
        val incoming = ctx.channel()
        System.out.println("Client:" + incoming.remoteAddress() + "掉线")
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val incoming = ctx.channel()
        System.out.println("Client:" + incoming.remoteAddress() + "异常")
        // 当出现异常就关闭连接
        cause.printStackTrace()
        ctx.close()
    }

    companion object {

        var channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }

}