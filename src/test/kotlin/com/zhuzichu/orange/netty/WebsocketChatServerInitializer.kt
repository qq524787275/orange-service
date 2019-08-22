package com.zhuzichu.orange.netty

import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel


/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 14:41
 */
class WebsocketChatServerInitializer : ChannelInitializer<SocketChannel>() { //1

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {//2
        val pipeline = ch.pipeline()
        pipeline.addLast(HttpServerCodec())
        pipeline.addLast(HttpObjectAggregator(64 * 1024))
        pipeline.addLast(ChunkedWriteHandler())

        pipeline.addLast(HttpRequestHandler("/ws"))
        pipeline.addLast(WebSocketServerProtocolHandler("/ws"))
        pipeline.addLast(TextWebSocketFrameHandler())
    }
}