package org.khod.pojo.item;

import org.khod.pojo.field.*;

/**
 * {
 *   "e":"bookTicker",			// event type
 *   "u":400900217,     		// order book updateId
 *   "E": 1568014460893,  		// event time
 *   "T": 1568014460891,  		// transaction time
 *   "s":"BNBUSDT",     		// symbol
 *   "b":"25.35190000", 		// best bid price
 *   "B":"31.21000000", 		// best bid qty
 *   "a":"25.36520000", 		// best ask price
 *   "A":"40.66000000"  		// best ask qty
 * }
 */

public class BinanceSymbolBook extends DefaultPojoItem {
    @Override
    Field[] getFieldDefinition() {
        return new Field[] {
                new StringField("e", "Event type"),
                new LongField("u", "order book updateId"),
                new LongField("E", "Event time"),
                new LongField("T", "transaction time"),
                new StringField("s", "Symbol"),
                new DecimalField("b", "best bid price"),
                new DecimalField("B", "best bid qty"),
                new DecimalField("a", "best ask price"),
                new DecimalField("A", "best ask qty")
        };
    }
}
