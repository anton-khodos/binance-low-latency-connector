package org.khod.pojo.item;

import org.khod.pojo.field.*;

/**
 * {
 *   "e": "aggTrade",  // Event type
 *   "E": 123456789,   // Event time
 *   "s": "BTCUSDT",    // Symbol
 *   "a": 5933014,		// Aggregate trade ID
 *   "p": "0.001",     // Price
 *   "q": "100",       // Quantity
 *   "f": 100,         // First trade ID
 *   "l": 105,         // Last trade ID
 *   "T": 123456785,   // Trade time
 *   "m": true,        // Is the buyer the market maker?
 * }
 */

public class BinanceAggTrade extends DefaultPojoItem {
    @Override
    Field[] getFieldDefinition() {
        return new Field[] {
            new StringField("e", "Event type"),
            new LongField("E", "Event time"),
            new StringField("s", "Symbol"),
            new LongField("a", "Aggregate trade ID"),
            new DecimalField("p", "Price"),
            new DecimalField("q", "Quantity"),
            new LongField("f", "First trade ID"),
            new LongField("l", "Last trade ID"),
            new LongField("T", "Trade time"),
            new BooleanField("m", "Is the buyer the market maker?")
        };
    }
}
