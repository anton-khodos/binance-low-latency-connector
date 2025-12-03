package org.khod.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestWebSocketServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    public TestWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.atInfo().log("Connection opened.");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.atInfo().log("Connection closed. Code = {}, Reason = {}.", reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.atInfo().log("OnMessage = {}", message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.atError().setCause(ex).log("On error.");
    }

    @Override
    public void onStart() {
        logger.atInfo().log("Websocket server started.");
        isStarted.set(true);
    }

    public boolean isStarted() {
        return isStarted.get();
    }
}