package org.khod.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Queue;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

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
        logger.atInfo().log("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                logger.atInfo().log("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                logger.atInfo().log("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if(msg instanceof WebSocketFrame) {
            ByteBuf buf = ((WebSocketFrame)msg).content();
            if(buf.capacity() != 0) {
                // Keep buffer alive beyond handler lifetime
                buf.retain();
                if (!queue.offer(buf)) {
                    logger.atWarn().log("Failed to push message into the queue. Consumer doesn't keep up with the "
                                        + "producer!");
                }
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