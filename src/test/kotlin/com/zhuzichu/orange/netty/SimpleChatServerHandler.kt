package com.zhuzichu.orange.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.concurrent.GlobalEventExecutor
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.group.ChannelGroup


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 13:50
 */
class SimpleChatServerHandler : SimpleChannelInboundHandler<String>() {

    var channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        for (channel in channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n")
        }
        channels.add(ctx.channel())
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        for (channel in channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n")
        }
        channels.remove(ctx.channel())
    }

    override fun channelRead0(ctx: ChannelHandlerContext, s: String) {
        val incoming = ctx.channel()
        for (channel in channels) {
            if (channel !== incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n")
            } else {
                channel.writeAndFlush("[you]$s\n")
            }
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        println("SimpleChatClient:" + incoming.remoteAddress() + "在线")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val incoming = ctx.channel()
        println("SimpleChatClient:" + incoming.remoteAddress() + "掉线")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val incoming = ctx.channel()
        println("SimpleChatClient:" + incoming.remoteAddress() + "异常")
        // 当出现异常就关闭连接
        cause.printStackTrace()
        ctx.close()
    }
}