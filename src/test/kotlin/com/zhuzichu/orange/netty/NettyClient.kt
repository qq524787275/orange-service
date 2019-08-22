package com.zhuzichu.orange.netty

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.io.InputStreamReader
import java.io.BufferedReader


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 13:43
 */
class NettyClient {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val woker = NioEventLoopGroup()
            val clinet = Bootstrap()
            try {
                clinet.apply {
                    group(woker)
                    channel(NioSocketChannel::class.java)
                    option(ChannelOption.SO_KEEPALIVE, true)
                    handler(SimpleChatClientInitializer())
                }
                val channel = clinet.connect("127.0.0.1", 9999).sync().channel()
                val isr = BufferedReader(InputStreamReader(System.`in`))
                channel.writeAndFlush(isr.readLine() + "\r\n")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                woker.shutdownGracefully()
            }
        }
    }
}