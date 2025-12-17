package org.khod.websocket;

public interface IWebSocketCallback<T> {
    void onMessage(T message);
}
