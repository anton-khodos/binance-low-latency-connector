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

    @Test
    public void parseBinanceAggTradePojo()
            throws JsonParseException {
        BinanceJsonParser parser = new BinanceJsonParser();
        BinanceAggTrade pojo = new BinanceAggTrade();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(aggTradeJson.length());
        buffer.writeCharSequence(aggTradeJson, StandardCharsets.US_ASCII);

        parser.parsePojo(buffer, pojo);

        Assertions.assertEquals("aggTrade", ((StringField)pojo.getFieldMap().get("e")).getValue().toString());
        Assertions.assertEquals(123456789, ((LongField)pojo.getFieldMap().get("E")).getValue());
        Assertions.assertEquals("BTCUSDT", ((StringField)pojo.getFieldMap().get("s")).getValue().toString());
        Assertions.assertEquals(5933014, ((LongField)pojo.getFieldMap().get("a")).getValue());

        Assertions.assertEquals("0.001", ((DecimalField)pojo.getFieldMap().get("p")).getValue().toString());
        Assertions.assertEquals("100", ((DecimalField)pojo.getFieldMap().get("q")).getValue().toString());

        Assertions.assertEquals(100, ((LongField)pojo.getFieldMap().get("f")).getValue());
        Assertions.assertEquals(105, ((LongField)pojo.getFieldMap().get("l")).getValue());
        Assertions.assertEquals(123456785, ((LongField)pojo.getFieldMap().get("T")).getValue());
        Assertions.assertTrue(((BooleanField) pojo.getFieldMap().get("m")).getValue());

    }
}
