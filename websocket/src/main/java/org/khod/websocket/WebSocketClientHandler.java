package org.khod.websocket;

import com.sun.jdi.ObjectReference;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;

import java.net.URI;
import java.util.Queue;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    private final Queue<ByteBuf> queue;

    public WebSocketClientHandler(URI uri, final Queue<ByteBuf> queue) {
        this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                                         WebSocketVersion.V13,
                                                                         null,
                                                                         true,
                                                                         new DefaultHttpHeaders()
        );
        this.queue = queue;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if(msg instanceof WebSocketFrame) {
            ByteBuf buf = ((WebSocketFrame)msg).content();
            if(buf.capacity() != 0) {
                // Keep buffer alive beyond handler lifetime
                buf.retain();
                queue.offer(buf);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}