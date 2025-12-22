package org.khod.json;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khod.pojo.field.BooleanField;
import org.khod.pojo.field.DecimalField;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.StringField;
import org.khod.pojo.item.BinanceAggTrade;
import org.khod.pojo.item.BinanceSymbolBook;
import org.khod.utils.JsonParseException;

import java.nio.charset.StandardCharsets;

public class BinanceJsonParserTests {
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

    @Test
    public void parseBinanceAggTradePojo()
            throws JsonParseException {
        BinanceJsonParser<BinanceAggTrade> parser = new BinanceJsonParser<>();
        BinanceAggTrade pojo = new BinanceAggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(aggTradeJson.length());
        buffer.writeCharSequence(aggTradeJson, StandardCharsets.US_ASCII);

        parser.parsePojo(buffer, pojo);

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
        BinanceJsonParser<BinanceSymbolBook> parser = new BinanceJsonParser<>();
        BinanceSymbolBook pojo = new BinanceSymbolBook();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(binanceSymbolBookJson.length());
        buffer.writeCharSequence(binanceSymbolBookJson, StandardCharsets.US_ASCII);

        parser.parsePojo(buffer, pojo);

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

}
