package com.zhuzichu.orange.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 12:37
 */
class NettyServer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val boss = NioEventLoopGroup()
            val woker = NioEventLoopGroup()
            val server = ServerBootstrap()

            try {
                server.apply {
                    group(boss, woker)
                    channel(NioServerSocketChannel::class.java)
                    option(ChannelOption.SO_BACKLOG, 1024)
                    childOption(ChannelOption.SO_KEEPALIVE, true)
                    childOption(ChannelOption.TCP_NODELAY, true)
//                    childHandler(SimpleChatServerInitializer())
                    childHandler(WebsocketChatServerInitializer())
                }
                val future = server.bind(9999).sync()
                future.channel().closeFuture().sync()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                boss.shutdownGracefully()
                woker.shutdownGracefully()
            }
        }
    }

}