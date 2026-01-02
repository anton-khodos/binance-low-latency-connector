package org.khod.websocket.binance;

import org.khod.pojo.item.*;
import org.khod.websocket.IWebSocketCallback;

public interface IBinanceWebSocketClient {
    int aggTradeStream(String symbol, IWebSocketCallback<AggTrade> onMessageCallback);
    int bookTicker(String symbol, IWebSocketCallback<SymbolBook> onMessageCallback);
    int symbolTicker(String symbol, IWebSocketCallback<IndividualSymbol> onMessageCallback);
    int klineStream(String symbol, String interval, IWebSocketCallback<Kline> onMessageCallback);
    int miniTickerStream(String symbol, IWebSocketCallback<IndividualSymbolMini> onMessageCallback);
    int allMiniTickerStream(IWebSocketCallback<IndividualSymbolMini> onMessageCallback);
    int allTickerStream(IWebSocketCallback<IndividualSymbol> onMessageCallback);
    int allBookTickerStream(IWebSocketCallback<SymbolBook> onMessageCallback);
    int partialDepthStream(String symbol, int levels, int speed, IWebSocketCallback<DepthUpdate> onMessageCallback);
    int diffDepthStream(String symbol, int speed, IWebSocketCallback<DepthUpdate> onMessageCallback);
    int markPriceStream(String symbol, int speed, IWebSocketCallback<MarkPrice> onMessageCallback);
    int continuousKlineStream(String pair, String interval, String contractType, IWebSocketCallback<ContinuousKline> onMessageCallback);
    int forceOrderStream(String symbol, IWebSocketCallback<ForceOrder> onMessageCallback);
    int allForceOrderStream(IWebSocketCallback<ForceOrder> onMessageCallback);
    void closeConnection(int streamId);
    void closeAllConnections();
}
