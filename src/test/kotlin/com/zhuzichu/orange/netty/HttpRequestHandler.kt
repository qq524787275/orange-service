package com.zhuzichu.orange.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.ChannelFutureListener
import io.netty.handler.stream.ChunkedNioFile
import io.netty.channel.DefaultFileRegion
import io.netty.handler.ssl.SslHandler
import io.netty.handler.codec.http.*
import java.io.File
import java.io.RandomAccessFile
import java.net.URISyntaxException



/**
 * Created by IntelliJ IDEA.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-22
 * Time: 14:24
 */
class HttpRequestHandler(//1
        private val wsUri: String) : SimpleChannelInboundHandler<FullHttpRequest>() {

    @Throws(Exception::class)
    public override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        if (wsUri.equals(request.uri, ignoreCase = true)) {
            ctx.fireChannelRead(request.retain())                  //2
        } else {
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(ctx)                               //3
            }

            val file = RandomAccessFile(INDEX, "r")//4

            val response = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.OK)
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8")

            val keepAlive = HttpHeaders.isKeepAlive(request)

            if (keepAlive) {                                        //5
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length())
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE)
            }
            ctx.write(response)                    //6

            if (ctx.pipeline().get(SslHandler::class.java) == null) {     //7
                ctx.write(DefaultFileRegion(file.getChannel(), 0, file.length()))
            } else {
                ctx.write(ChunkedNioFile(file.getChannel()))
            }
            val future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)           //8
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE)        //9
            }

            file.close()
        }
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
        private val INDEX: File

        init {
            val location = HttpRequestHandler::class.java.protectionDomain.codeSource.location
            try {
                var path = location.toURI().toString() + "WebsocketChatClient.html"
                path = if (!path.contains("file:")) path else path.substring(5)
                INDEX = File(path)
            } catch (e: URISyntaxException) {
                throw IllegalStateException("Unable to locate WebsocketChatClient.html", e)
            }

        }

        private fun send100Continue(ctx: ChannelHandlerContext) {
            val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE)
            ctx.writeAndFlush(response)
        }
    }
}
