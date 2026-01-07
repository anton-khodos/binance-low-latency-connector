package org.khod.websocket.binance;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khod.pojo.item.AggTrade;
import org.khod.websocket.IWebSocketCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BinanceWebSocketClientTests {
    private static final Logger logger = LoggerFactory.getLogger(BinanceWebSocketClientTests.class);

    private static BinanceWebSocketClient client;
    private static AtomicInteger messageReceived;

    @BeforeAll
    public static void setup() {
        boolean isTestClient = true;
        client = new BinanceWebSocketClient(isTestClient);
        messageReceived = new AtomicInteger(0);
    }

    @BeforeEach
    public void beforeEach() {
        messageReceived.set(0);
    }

    @Test
    public void connectivityTest() {
        int id = client.aggTradeStream("btcusdt", x -> {
            logger.atInfo().log(x.toString());
            messageReceived.incrementAndGet();
        });
        Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> messageReceived.get() > 10);
        client.closeConnection(id);
    }

    @Test
    public void klineStreamTest() {
        int id = client.klineStream("ethusdt", "1m", x -> {
            logger.atInfo().log(x.toString());
            messageReceived.incrementAndGet();
        });
        Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> messageReceived.get() > 5);
        client.closeConnection(id);
    }

    @Test
    public void partialBookDepthStreamTest() {
        int id = client.partialDepthStream("bnbusdt", 5, 100, x -> {
            logger.atInfo().log(x.toString());
            messageReceived.incrementAndGet();
        });
        Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> messageReceived.get() > 5);
        client.closeConnection(id);
    }
}
