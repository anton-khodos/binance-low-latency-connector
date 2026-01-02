package org.khod.websocket.binance;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;
import org.khod.pojo.item.*;
import org.khod.utils.URLBuilder;
import org.khod.websocket.IWebSocketCallback;
import org.khod.websocket.WebSocketClient;
import org.khod.websocket.WebSocketConnection;


public class BinanceWebSocketClient implements IBinanceWebSocketClient{
    private int connectionIndex = 0;
    private final WebSocketConnection[] connections = new WebSocketConnection[64];
    private final boolean useTestURL;

    public BinanceWebSocketClient(final boolean useTestURL) {
        this.useTestURL = useTestURL;
    }


    @Override
    public int aggTradeStream(String symbol, IWebSocketCallback<AggTrade> onMessageCallback) {
        final AggTrade flyweightItem = new AggTrade();
        return createGenericStream(symbol, "aggTrade", flyweightItem, onMessageCallback);
    }

    @Override
    public int bookTicker(String symbol, IWebSocketCallback<SymbolBook> onMessageCallback) {
        final SymbolBook flyweightItem = new SymbolBook();
        return createGenericStream(symbol, "bookTicker", flyweightItem, onMessageCallback);
    }

    @Override
    public int symbolTicker(String symbol, IWebSocketCallback<IndividualSymbol> onMessageCallback) {
        final IndividualSymbol flyweightItem = new IndividualSymbol();
        return createGenericStream(symbol, "ticker", flyweightItem, onMessageCallback);
    }

    @Override
    public int klineStream(String symbol, String interval, IWebSocketCallback<Kline> onMessageCallback) {
        final Kline flyweightItem = new Kline();
        return createGenericStream(symbol, String.format("kline_%s", interval), flyweightItem, onMessageCallback);
    }

    @Override
    public int miniTickerStream(String symbol, IWebSocketCallback<IndividualSymbolMini> onMessageCallback) {
        final IndividualSymbolMini flyweightItem = new IndividualSymbolMini();
        return createGenericStream(symbol, "miniTicker", flyweightItem, onMessageCallback);
    }

    @Override
    public int allMiniTickerStream(IWebSocketCallback<IndividualSymbolMini> onMessageCallback) {
        final IndividualSymbolMini flyweightItem = new IndividualSymbolMini();
        String streamType = "!miniTicker@arr";
        return createGenericStream("", streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int allTickerStream(IWebSocketCallback<IndividualSymbol> onMessageCallback) {
        final IndividualSymbol flyweightItem = new IndividualSymbol();
        String streamType = "!ticker@arr";
        return createGenericStream("", streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int allBookTickerStream(IWebSocketCallback<SymbolBook> onMessageCallback) {
        final SymbolBook flyweightItem = new SymbolBook();
        String streamType = "!bookTicker";
        return createGenericStream("", streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int partialDepthStream(String symbol, int levels, int speed, IWebSocketCallback<DepthUpdate> onMessageCallback) {
        final DepthUpdate flyweightItem = new DepthUpdate(50);
        String streamType = String.format("depth%d@%dms", levels, speed);
        return createGenericStream(symbol, streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int diffDepthStream(String symbol, int speed, IWebSocketCallback<DepthUpdate> onMessageCallback) {
        final DepthUpdate flyweightItem = new DepthUpdate(50);
        String streamType = String.format("depth@%dms", speed);
        return createGenericStream(symbol, streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int markPriceStream(String symbol, int speed, IWebSocketCallback<MarkPrice> onMessageCallback) {
        final MarkPrice flyweightItem = new MarkPrice();
        String streamType = String.format("markPrice@%dms", speed);
        return createGenericStream(symbol, streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int continuousKlineStream(String pair, String interval, String contractType, IWebSocketCallback<ContinuousKline> onMessageCallback) {
        final ContinuousKline flyweightItem = new ContinuousKline();
        String streamType = String.format("continuousKline_%s_%s", interval, contractType);
        return createGenericStream(pair, streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int forceOrderStream(String symbol, IWebSocketCallback<ForceOrder> onMessageCallback) {
        final ForceOrder flyweightItem = new ForceOrder();
        String streamType = "forceOrder";
        return createGenericStream(symbol, streamType, flyweightItem, onMessageCallback);
    }

    @Override
    public int allForceOrderStream(IWebSocketCallback<ForceOrder> onMessageCallback) {
        final ForceOrder flyweightItem = new ForceOrder();
        String streamType = "!forceOrder@arr";
        return createGenericStream("", streamType, flyweightItem, onMessageCallback);
    }

    private <T extends DefaultPojoItem> int createGenericStream(String symbol,
                                                                String streamType,
                                                                T flyweightItem,
                                                                IWebSocketCallback<T> onMessageCallback) {
        final SpscArrayQueue<ByteBuf> queue = new SpscArrayQueue<>(1024);
        final String url = useTestURL ?
                URLBuilder.buildBinanceTestURL(symbol, streamType) :
                URLBuilder.buildBinanceProdURL(symbol, streamType);

        WebSocketClient client = new WebSocketClient(queue, url);
        BinanceJsonQueueConsumer<T> jsonConsumer = new BinanceJsonQueueConsumer<>(queue,
                onMessageCallback,
                flyweightItem
        );
        WebSocketConnection connection = new WebSocketConnection(client, jsonConsumer);
        connection.start();
        connections[connectionIndex] = connection;

        return connectionIndex++;
    }

    public void closeConnection(int connectionId) {
        connections[connectionId].stop();
    }

    @Override
    public void closeAllConnections() {
        for (int i = 0; i < connectionIndex; i++) {
            connections[i].stop();
        }
        connectionIndex = 0;
    }
}
