package org.khod.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jctools.queues.SpscArrayQueue;

import javax.net.ssl.SSLException;
import java.net.URI;

public final class WebSocketClient {

    private static final int MAX_CONTENT_LENGTH = 8192;
    private final SpscArrayQueue<ByteBuf> queue;

    private final URI uri;
    private final EventLoopGroup group;
    private Channel channel;

    public WebSocketClient(final SpscArrayQueue<ByteBuf> queue, String url) {
        this.queue = queue;
        try {
            this.uri = new URI(url);
            this.group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        } catch (Exception e) {
            throw new RuntimeException("failed to create URI for %s".formatted(url), e);
        }
    }

    public void connect()
            throws SSLException, InterruptedException {
        String scheme = uri.getScheme() == null? "ws" : uri.getScheme();
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            throw new RuntimeException("Only WS(S) is supported.");
        }

        final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
        final int port = getPort(scheme);
        final SslContext sslCtx = getSslContext(scheme);

        final WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                                                                    WebSocketVersion.V13,
                                                                                                    null,
                                                                                                    true,
                                                                                                    new DefaultHttpHeaders()
        );
        final WebSocketClientHandler handler =
                new WebSocketClientHandler(handshaker, queue);
        Bootstrap b = new Bootstrap();

        b.group(group)
         .channel(NioSocketChannel.class)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) {
                 ChannelPipeline p = ch.pipeline();
                 if (sslCtx != null) {
                     p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                 }
                 p.addLast(
                         new HttpClientCodec(),
                         new HttpObjectAggregator(MAX_CONTENT_LENGTH),
                         new WebSocketClientCompressionHandler(MAX_CONTENT_LENGTH),
                         handler);
             }
         });
        channel = b.connect(host, port).sync().channel();
        handler.handshakeFuture().sync();
    }

    private SslContext getSslContext(final String scheme)
            throws SSLException {
        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                                      .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }
        return sslCtx;
    }

    private int getPort(final String scheme) {
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }
        return port;
    }

    public void send(String text) {
        channel.writeAndFlush(new TextWebSocketFrame(text));
    }

    public void close() {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.close();
        group.shutdownGracefully();
    }
}