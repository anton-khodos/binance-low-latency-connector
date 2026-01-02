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

    private final static String depthUpdateJson = """
            {
              "e": "depthUpdate",
              "E": 123456789,
              "s": "BTCUSDT",
              "U": 157,
              "u": 160,
              "b": [
                [
                  "0.0024",
                  "10"
                ]
              ],
              "a": [
                [
                  "0.0026",
                  "100"
                ]
              ]
            }""";

    public static final String forcedOrderJson = """
            {
              "e": "forceOrder",
              "E": 1623499200000,
              "o": {
                "s": "BTCUSDT",
                "S": "SELL",
                "o": "MARKET",
                "f": "IOC",
                "q": "0.001",
                "p": "0",
                "ap": "35000.00",
                "X": "FILLED",
                "l": "0.001",
                "z": "0.001",
                "T": 1623499200000
              }
            }""";

    public static final String individialSymbolTickerJson = """
            {
              "e": "24hrTicker",
              "E": 123456789,
              "s": "BTCUSDT",
              "p": "500.00",
              "P": "5.00",
              "w": "10000.00",
              "c": "10500.00",
              "Q": "0.001",
              "o": "10000.00",
              "h": "11000.00",
              "l": "9000.00",
              "v": "1500.00",
              "q": "15000000.00",
              "O": 123456000,
              "C": 123456999,
              "F": 100,
              "L": 200,
              "n": 1000
            }""";

    private static final String individialSymbolMiniTickerJson = """
            {
              "e": "24hrMiniTicker",
              "E": 123456789,
              "s": "BTCUSDT",
              "c": "10500.00",
              "o": "10000.00",
              "h": "11000.00",
              "l": "9000.00",
              "v": "1500.00",
              "q": "15000000.00"
            }""";

    private static final String continuousKlineJson = """
            {
              "e":"continuous_kline",
              "E":1591261542539,
              "ps":"BTCUSD",
              "ct":"NEXT_QUARTER",
              "k":{
                "t":1591261500000,
                "T":1591261559999,
                "i":"1m",
                "f":606400,
                "L":606430,
                "o":"9638.9",
                "c":"9639.8",
                "h":"9639.8",
                "l":"9638.6",
                "v":"156",
                "n":31,
                "x":false,
                "q":"1.61836886",
                "V":"73",
                "Q":"0.75731156",
                "B":"0"
              }
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

    @Test
    public void parseBinanceAggTradePojo()
            throws JsonParseException {
        AggTrade pojo = new AggTrade();
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
        SymbolBook pojo = new SymbolBook();
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
    public void parseDepthUpdateJsonShouldNotThrow()
            throws JsonParseException {
        DepthUpdate pojo = new DepthUpdate(5);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(depthUpdateJson.length());
        buffer.writeCharSequence(depthUpdateJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("depthUpdate", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(123456789, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals(157, ((LongField) pojo.getFieldMap().get("U")).getValue());
        Assertions.assertEquals(160, ((LongField) pojo.getFieldMap().get("u")).getValue());

        ArrayField bids = (ArrayField) pojo.getFieldMap().get("b");
        Assertions.assertEquals(1, bids.getSize());

        ArrayField bidEntry = (ArrayField)bids.getValue()[0];
        Assertions.assertEquals(2, bidEntry.getSize());
        Assertions.assertEquals("0.0024", ((DecimalField) bidEntry.getValue()[0]).getValue().toString());
        Assertions.assertEquals("10", ((DecimalField) bidEntry.getValue()[1]).getValue().toString());

        ArrayField asks = (ArrayField) pojo.getFieldMap().get("a");
        Assertions.assertEquals(1, asks.getSize());
        ArrayField askEntry = (ArrayField)asks.getValue()[0];
        Assertions.assertEquals(2, askEntry.getSize());
        Assertions.assertEquals("0.0026", ((DecimalField) askEntry.getValue()[0]).getValue().toString());
        Assertions.assertEquals("100", ((DecimalField) askEntry.getValue()[1]).getValue().toString());
    }

    @Test
    public void parseForcedOrderJsonShouldNotThrow()
            throws JsonParseException {
        ForceOrder pojo = new ForceOrder();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(forcedOrderJson.length());
        buffer.writeCharSequence(forcedOrderJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("forceOrder", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(1623499200000L, ((LongField) pojo.getFieldMap().get("E")).getValue());

        DefaultPojoItem order = ((ObjectField) pojo.getFieldMap().get("o")).getValue();
        Assertions.assertEquals("BTCUSDT", ((StringField) order.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals("SELL", ((StringField) order.getFieldMap().get("S")).getValue().toString());
        Assertions.assertEquals("MARKET", ((StringField) order.getFieldMap().get("o")).getValue().toString());
        Assertions.assertEquals("IOC", ((StringField) order.getFieldMap().get("f")).getValue().toString());
        Assertions.assertEquals("0.001", ((DecimalField) order.getFieldMap().get("q")).getValue().toString());
        Assertions.assertEquals("0", ((DecimalField) order.getFieldMap().get("p")).getValue().toString());
        Assertions.assertEquals("35000", ((DecimalField) order.getFieldMap().get("ap")).getValue().toString());
        Assertions.assertEquals("FILLED", ((StringField) order.getFieldMap().get("X")).getValue().toString());
        Assertions.assertEquals("0.001", ((DecimalField) order.getFieldMap().get("l")).getValue().toString());
        Assertions.assertEquals("0.001", ((DecimalField) order.getFieldMap().get("z")).getValue().toString());
        Assertions.assertEquals(1623499200000L, ((LongField) order.getFieldMap().get("T")).getValue());
    }

    @Test
    public void parseIndividialSymbolTickerJsonShouldNotThrow()
            throws JsonParseException {
        IndividualSymbol pojo = new IndividualSymbol();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(individialSymbolTickerJson.length());
        buffer.writeCharSequence(individialSymbolTickerJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("24hrTicker", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(123456789, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals("500", ((DecimalField) pojo.getFieldMap().get("p")).getValue().toString());
        Assertions.assertEquals("5", ((DecimalField) pojo.getFieldMap().get("P")).getValue().toString());
        Assertions.assertEquals("10000", ((DecimalField) pojo.getFieldMap().get("w")).getValue().toString());
        Assertions.assertEquals("10500", ((DecimalField) pojo.getFieldMap().get("c")).getValue().toString());
        Assertions.assertEquals("0.001", ((DecimalField) pojo.getFieldMap().get("Q")).getValue().toString());
        Assertions.assertEquals("10000", ((DecimalField) pojo.getFieldMap().get("o")).getValue().toString());
        Assertions.assertEquals("11000", ((DecimalField) pojo.getFieldMap().get("h")).getValue().toString());
        Assertions.assertEquals("9000", ((DecimalField) pojo.getFieldMap().get("l")).getValue().toString());
        Assertions.assertEquals("1500", ((DecimalField) pojo.getFieldMap().get("v")).getValue().toString());
        Assertions.assertEquals("15000000", ((DecimalField) pojo.getFieldMap().get("q")).getValue().toString());
        Assertions.assertEquals(123456000L, ((LongField) pojo.getFieldMap().get("O")).getValue());
        Assertions.assertEquals(123456999L, ((LongField) pojo.getFieldMap().get("C")).getValue());
        Assertions.assertEquals(100, ((LongField) pojo.getFieldMap().get("F")).getValue());
        Assertions.assertEquals(200, ((LongField) pojo.getFieldMap().get("L")).getValue());
        Assertions.assertEquals(1000, ((LongField) pojo.getFieldMap().get("n")).getValue());
    }

    @Test
    public void parseIndividialSymbolMiniTickerJsonShouldNotThrow()
            throws JsonParseException {
        IndividualSymbolMini pojo = new IndividualSymbolMini();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(individialSymbolMiniTickerJson.length());
        buffer.writeCharSequence(individialSymbolMiniTickerJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("24hrMiniTicker", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(123456789, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField) pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals("10500", ((DecimalField) pojo.getFieldMap().get("c")).getValue().toString());
        Assertions.assertEquals("10000", ((DecimalField) pojo.getFieldMap().get("o")).getValue().toString());
        Assertions.assertEquals("11000", ((DecimalField) pojo.getFieldMap().get("h")).getValue().toString());
        Assertions.assertEquals("9000", ((DecimalField) pojo.getFieldMap().get("l")).getValue().toString());
        Assertions.assertEquals("1500", ((DecimalField) pojo.getFieldMap().get("v")).getValue().toString());
        Assertions.assertEquals("15000000", ((DecimalField) pojo.getFieldMap().get("q")).getValue().toString());
    }

    @Test
    public void parseContinuousKlineJsonShouldNotThrow()
            throws JsonParseException {
        ContinuousKline pojo = new ContinuousKline();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(continuousKlineJson.length());
        buffer.writeCharSequence(continuousKlineJson, StandardCharsets.US_ASCII);

        BinanceJsonParser.parsePojo(buffer, pojo);

        Assertions.assertEquals("continuous_kline", ((StringField) pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(1591261542539L, ((LongField) pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSD", ((StringField) pojo.getFieldMap().get("ps")).getValue().toString());
        Assertions.assertEquals("NEXT_QUARTER", ((StringField) pojo.getFieldMap().get("ct")).getValue().toString());

        DefaultPojoItem internalKline = ((ObjectField)pojo.getFieldMap().get("k")).getValue();
        Assertions.assertEquals(1591261500000L, ((LongField) internalKline.getFieldMap().get("t")).getValue());
        Assertions.assertEquals(1591261559999L, ((LongField) internalKline.getFieldMap().get("T")).getValue());
        Assertions.assertEquals("1m", ((StringField) internalKline.getFieldMap().get("i")).getValue().toString());
        Assertions.assertEquals(606400, ((LongField) internalKline.getFieldMap().get("f")).getValue());
        Assertions.assertEquals(606430, ((LongField) internalKline.getFieldMap().get("L")).getValue());
        Assertions.assertEquals("9638.9", ((DecimalField) internalKline.getFieldMap().get("o")).getValue().toString());
        Assertions.assertEquals("9639.8", ((DecimalField) internalKline.getFieldMap().get("c")).getValue().toString());
        Assertions.assertEquals("9639.8", ((DecimalField) internalKline.getFieldMap().get("h")).getValue().toString());
        Assertions.assertEquals("9638.6", ((DecimalField) internalKline.getFieldMap().get("l")).getValue().toString());
        Assertions.assertEquals("156", ((DecimalField) internalKline.getFieldMap().get("v")).getValue().toString());
        Assertions.assertEquals(31, ((LongField) internalKline.getFieldMap().get("n")).getValue());
        Assertions.assertFalse(((BooleanField) internalKline.getFieldMap().get("x")).getValue());
        Assertions.assertEquals("1.61836886", ((DecimalField) internalKline.getFieldMap().get("q")).getValue().toString());
        Assertions.assertEquals("73", ((DecimalField) internalKline.getFieldMap().get("V")).getValue().toString());
        Assertions.assertEquals("0.75731156", ((DecimalField) internalKline.getFieldMap().get("Q")).getValue().toString());
        Assertions.assertEquals("0", ((StringField) internalKline.getFieldMap().get("B")).getValue().toString());
    }

    @Test
    public void parseInvalidJsonShouldThrow() {
        AggTrade pojo = new AggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(INVALID_JSON.length());
        buffer.writeCharSequence(INVALID_JSON, StandardCharsets.US_ASCII);

        Assertions.assertThrows(JsonParseException.class, () -> BinanceJsonParser.parsePojo(buffer, pojo));
    }

    @Test
    public void parseIncompleteJsonShouldThrow() {
        AggTrade pojo = new AggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(INCOMPLETE_JSON.length());
        buffer.writeCharSequence(INCOMPLETE_JSON, StandardCharsets.US_ASCII); // Missing closing brace

        Assertions.assertThrows(JsonParseException.class, () -> BinanceJsonParser.parsePojo(buffer, pojo));
    }
}
