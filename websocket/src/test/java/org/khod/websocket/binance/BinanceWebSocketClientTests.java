package org.khod.websocket.binance;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BinanceWebSocketClientTests {
    private static final Logger logger = LoggerFactory.getLogger(BinanceWebSocketClientTests.class);

    @Test
    public void connectivityTest() {
        boolean isTestClient = true;
        BinanceWebSocketClient client = new BinanceWebSocketClient(isTestClient);

        AtomicInteger messageReceived = new AtomicInteger(0);
        int id = client.aggTradeStream("btcusdt", x -> {
            logger.atInfo().log(x.toString());
            messageReceived.incrementAndGet();
        });
        Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> messageReceived.get() > 10);

        client.closeConnection(id);
        //TODO: proper test
    }
}
