package org.khod.json;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khod.pojo.field.*;
import org.khod.pojo.item.*;
import org.khod.utils.JsonParseException;

import java.nio.charset.StandardCharsets;

public class BinanceJsonParserTests {
    public static final String INVALID_JSON = """
            {
              "e": "aggTrade",
              "E": 123456789,
              "s": "BTCUSDT",
              "a": 5933014,
              "p": "0.001",
              "q": "100",
              "f": 100,
              "l": 105,
              "T": 123456785,
              "m": true,   // Trailing comma makes this invalid JSON
            }""";
    /**
     * Example from:
     * <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Aggregate-Trade-Streams">Aggregate-Trade-Streams</a>
     */
    private final static String aggTradeJson = """
            {
              "e": "aggTrade",
              "E": 123456789,
              "s": "BTCUSDT",
              "a": 5933014,
              "p": "0.001",
              "q": "100",
              "f": 100,
              "l": 105,
              "T": 123456785,
              "m": true,
            }""";

    private final static String markPriceJson = """
            {
              "e": "markPriceUpdate",
              "E": 1562305380000,
              "s": "BTCUSDT",
              "p": "11794.15000000",
              "i": "11784.62659091",
              "P": "11784.25641265",
              "r": "0.00038167",
              "T": 1562306400000
            }""";

    private final static String klineJson = """
            {
              "e": "kline",
              "E": 1638747660000,
              "s": "BTCUSDT",
              "k": {
                "t": 1638747660000,
                "T": 1638747719999,
                "s": "BTCUSDT",
                "i": "1m",
                "f": 100,
                "L": 200,
                "o": "0.0010",
                "c": "0.0020",
                "h": "0.0025",
                "l": "0.0015",
                "v": "1000",
                "n": 100,
                "x": false,
                "q": "1.0000",
                "V": "500",
                "Q": "0.500",
                "B": "123456"
              }
            }""";

    private final static String binanceSymbolBookJson = """
             {
               "e":"bookTicker",
               "u":400900217,
               "E": 1568014460893,
               "T": 1568014460891,
               "s":"BNBUSDT",
               "b":"25.35190000",
               "B":"31.21000000",
               "a":"25.36520000",
               "A":"40.66000000"
             }""";

    public static final String INCOMPLETE_JSON = """
            {
              "e": "aggTrade",
              "E": 123456789,
              "s": "BTCUSDT",
              "a": 5933014,
              "p": "0.001",
              "q": "100",
              "f": 100,
              "l": 105,
              "T": 123456785
            """;
    public static final String EMPTY_JSON = "{}";

    @Test
    public void parseBinanceAggTradePojo()
            throws JsonParseException {
        BinanceAggTrade pojo = new BinanceAggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(aggTradeJson.length());
        buffer.writeCharSequence(aggTradeJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("aggTrade", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(123456789, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals(5933014, ((LongField) pojo.getFieldMap().get("a")).getValue());

        Assertions.assertEquals("0.001", ((DecimalField) pojo.getFieldMap().get("p")).getValue().toString());
        Assertions.assertEquals("100", ((DecimalField) pojo.getFieldMap().get("q")).getValue().toString());

        Assertions.assertEquals(100, ((LongField) pojo.getFieldMap().get("f")).getValue());
        Assertions.assertEquals(105, ((LongField) pojo.getFieldMap().get("l")).getValue());
        Assertions.assertEquals(123456785, ((LongField) pojo.getFieldMap().get("T")).getValue());
        Assertions.assertTrue(((BooleanField) pojo.getFieldMap().get("m")).getValue());
    }

    @Test
    public void parseBinanceBinanceSymbolBookPojo()
            throws JsonParseException {
        BinanceSymbolBook pojo = new BinanceSymbolBook();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(binanceSymbolBookJson.length());
        buffer.writeCharSequence(binanceSymbolBookJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("bookTicker", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(400900217, ((LongField) pojo.getFieldMap().get("u")).getValue());
        Assertions.assertEquals(1568014460893L, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals(1568014460891L, ((LongField) pojo.getFieldMap().get("T")).getValue());
        Assertions.assertEquals("BNBUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());

        Assertions.assertEquals("25.3519", ((DecimalField) pojo.getFieldMap().get("b")).getValue().toString());
        Assertions.assertEquals("31.21", ((DecimalField) pojo.getFieldMap().get("B")).getValue().toString());
        Assertions.assertEquals("25.3652", ((DecimalField) pojo.getFieldMap().get("a")).getValue().toString());
        Assertions.assertEquals("40.66", ((DecimalField) pojo.getFieldMap().get("A")).getValue().toString());
    }

    @Test
    public void parseMarkPriceJsonShouldNotThrow()
            throws JsonParseException {
        MarkPrice pojo = new MarkPrice();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(markPriceJson.length());
        buffer.writeCharSequence(markPriceJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);
        Assertions.assertEquals("markPriceUpdate", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(1562305380000L, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals("11794.15", ((DecimalField) pojo.getFieldMap().get("p")).getValue().toString());
        Assertions.assertEquals("11784.62659091", ((DecimalField) pojo.getFieldMap().get("i")).getValue().toString());
        Assertions.assertEquals("11784.25641265", ((DecimalField) pojo.getFieldMap().get("P")).getValue().toString());
        Assertions.assertEquals("0.00038167", ((DecimalField) pojo.getFieldMap().get("r")).getValue().toString());
        Assertions.assertEquals(1562306400000L, ((LongField) pojo.getFieldMap().get("T")).getValue());
    }

    @Test
    public void parseKlineJsonShouldNotThrow()
            throws JsonParseException {
        Kline pojo = new Kline();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(klineJson.length());
        buffer.writeCharSequence(klineJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("kline", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(1638747660000L, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());

        DefaultPojoItem internalKline = ((ObjectField)pojo.getFieldMap().get("k")).getValue();
        Assertions.assertEquals(1638747660000L, ((LongField) internalKline.getFieldMap().get("t")).getValue());
        Assertions.assertEquals(1638747719999L, ((LongField) internalKline.getFieldMap().get("T")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) internalKline.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals("1m", ((StringField) internalKline.getFieldMap().get("i")).getValue().toString());
        Assertions.assertEquals(100, ((LongField) internalKline.getFieldMap().get("f")).getValue());
        Assertions.assertEquals(200, ((LongField) internalKline.getFieldMap().get("L")).getValue());
        Assertions.assertEquals("0.001", ((DecimalField) internalKline.getFieldMap().get("o")).getValue().toString());
        Assertions.assertEquals("0.002", ((DecimalField) internalKline.getFieldMap().get("c")).getValue().toString());
        Assertions.assertEquals("0.0025", ((DecimalField) internalKline.getFieldMap().get("h")).getValue().toString());
        Assertions.assertEquals("0.0015", ((DecimalField) internalKline.getFieldMap().get("l")).getValue().toString());
        Assertions.assertEquals("1000", ((DecimalField) internalKline.getFieldMap().get("v")).getValue().toString());
        Assertions.assertEquals(100, ((LongField) internalKline.getFieldMap().get("n")).getValue());
        Assertions.assertFalse(((BooleanField) internalKline.getFieldMap().get("x")).getValue());
        Assertions.assertEquals("1", ((DecimalField) internalKline.getFieldMap().get("q")).getValue().toString());
        Assertions.assertEquals("500", ((DecimalField) internalKline.getFieldMap().get("V")).getValue().toString());
        Assertions.assertEquals("0.5", ((DecimalField) internalKline.getFieldMap().get("Q")).getValue().toString());
        Assertions.assertEquals("123456", ((StringField) internalKline.getFieldMap().get("B")).getValue().toString());
    }

    @Test
    public void parseInvalidJsonShouldThrow() {
        BinanceAggTrade pojo = new BinanceAggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(INVALID_JSON.length());
        buffer.writeCharSequence(INVALID_JSON, StandardCharsets.US_ASCII);

        Assertions.assertThrows(JsonParseException.class, () -> {
            BinanceJsonParser.parsePojo(buffer, pojo);
        });
    }

    @Test
    public void parseIncompleteJsonShouldThrow() {
        BinanceAggTrade pojo = new BinanceAggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(INCOMPLETE_JSON.length());
        buffer.writeCharSequence(INCOMPLETE_JSON, StandardCharsets.US_ASCII); // Missing closing brace

        Assertions.assertThrows(JsonParseException.class, () -> {
            BinanceJsonParser.parsePojo(buffer, pojo);
        });
    }
}
