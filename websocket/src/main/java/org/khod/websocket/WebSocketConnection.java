package org.khod.websocket;

import org.khod.data.ILifetimeObject;

import javax.net.ssl.SSLException;

public class WebSocketConnection implements ILifetimeObject {

    private final WebSocketClient client;
    private final ILifetimeObject aggTradeJsonConsumer;

    public WebSocketConnection(
            final WebSocketClient client,
            final ILifetimeObject aggTradeJsonConsumer) {
        this.client = client;
        this.aggTradeJsonConsumer = aggTradeJsonConsumer;
    }


    @Override
    public void start() {
        try {
            client.connect();
            aggTradeJsonConsumer.start();
        } catch (SSLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        client.close();
        aggTradeJsonConsumer.stop();
    }
}
