package com.zhuzichu.orange.netty

import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 13:57
 */

class SimpleChatClientInitializer : ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        pipeline.addLast("framer", DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter()))
        pipeline.addLast("decoder", StringDecoder())
        pipeline.addLast("encoder", StringEncoder())
        pipeline.addLast("handler", SimpleChatClientHandler())
    }
}