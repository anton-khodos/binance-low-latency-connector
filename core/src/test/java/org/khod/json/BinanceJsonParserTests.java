package org.khod.json;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khod.pojo.field.BooleanField;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.StringField;
import org.khod.pojo.item.BinanceAggTradePojo;
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
        BinanceAggTradePojo pojo = new BinanceAggTradePojo();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(aggTradeJson.length());
        buffer.writeCharSequence(aggTradeJson, StandardCharsets.US_ASCII);

        parser.parsePojo(buffer, pojo);

        Assertions.assertEquals(((StringField)pojo.getFieldMap().get("e")).getValue().toString(), "aggTrade");
        Assertions.assertEquals(((LongField)pojo.getFieldMap().get("E")).getValue(), 123456789);
        Assertions.assertEquals(((StringField)pojo.getFieldMap().get("s")).getValue().toString(), "BTCUSDT");
        Assertions.assertEquals(((LongField)pojo.getFieldMap().get("a")).getValue(), 5933014);

        //TODO: fix decimal64 parsing.
//        Assertions.assertEquals(((DecimalField)pojo.getFieldMap().get("p")).getValue().toString(), "0.001");
//        Assertions.assertEquals(((DecimalField)pojo.getFieldMap().get("q")).getValue().toString(), "100");

        Assertions.assertEquals(((LongField)pojo.getFieldMap().get("f")).getValue(), 100);
        Assertions.assertEquals(((LongField)pojo.getFieldMap().get("l")).getValue(), 105);
        Assertions.assertEquals(((LongField)pojo.getFieldMap().get("T")).getValue(), 123456785);
        Assertions.assertTrue(((BooleanField) pojo.getFieldMap().get("m")).getValue());

    }
}
