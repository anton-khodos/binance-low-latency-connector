package org.khod.websocket.binance;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;
import org.khod.pojo.item.AggTrade;
import org.khod.pojo.item.SymbolBook;
import org.khod.utils.URLBuilder;
import org.khod.websocket.IWebSocketCallback;
import org.khod.websocket.WebSocketClient;
import org.khod.websocket.WebSocketConnection;


public class BinanceWebSocketClient {
    private int connectionIndex = 0;
    private final WebSocketConnection[] connections = new WebSocketConnection[64];
    private final boolean useTestURL;

    public BinanceWebSocketClient(final boolean useTestURL) {
        this.useTestURL = useTestURL;
    }


    public int aggTradeStream(String symbol, IWebSocketCallback<AggTrade> onMessageCallback) {
        final SpscArrayQueue<ByteBuf> queue = new SpscArrayQueue<>(1024);
        final AggTrade flyweightItem = new AggTrade();
        final String url = useTestURL ?
                URLBuilder.buildBinanceTestURL(symbol, "aggTrade") :
                URLBuilder.buildBinanceProdURL(symbol, "aggTrade");

        WebSocketClient client = new WebSocketClient(queue, url);
        BinanceJsonQueueConsumer<AggTrade> aggTradeJsonConsumer = new BinanceJsonQueueConsumer<>(queue,
                                                                                                 onMessageCallback,
                                                                                                 flyweightItem
        );
        WebSocketConnection connection = new WebSocketConnection(client, aggTradeJsonConsumer);
        connection.start();
        connections[connectionIndex] = connection;

        return connectionIndex++;
    }

    public int individualSymbolBookStream(String symbol, IWebSocketCallback<SymbolBook> onMessageCallback) {
        final SpscArrayQueue<ByteBuf> queue = new SpscArrayQueue<>(1024);
        final SymbolBook flyweightItem = new SymbolBook();
        final String url = useTestURL ?
                URLBuilder.buildBinanceTestURL(symbol, "bookTicker") :
                URLBuilder.buildBinanceProdURL(symbol, "bookTicker");

        WebSocketClient client = new WebSocketClient(queue, url);
        BinanceJsonQueueConsumer<SymbolBook> aggTradeJsonConsumer = new BinanceJsonQueueConsumer<>(queue,
                                                                                                   onMessageCallback,
                                                                                                   flyweightItem
        );
        WebSocketConnection connection = new WebSocketConnection(client, aggTradeJsonConsumer);
        connection.start();
        connections[connectionIndex] = connection;

        return connectionIndex++;
    }

    public void closeConnection(int connectionId) {
        connections[connectionId].stop();
    }
}
