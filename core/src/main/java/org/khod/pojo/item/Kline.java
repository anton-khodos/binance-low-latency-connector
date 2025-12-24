package org.khod.pojo.item;

import org.khod.pojo.field.*;

/**
 * {
 *   "e": "kline",     // Event type
 *   "E": 1638747660000,   // Event time
 *   "s": "BTCUSDT",    // Symbol
 *   "k": {
 *     "t": 1638747660000, // Kline start time
 *     "T": 1638747719999, // Kline close time
 *     "s": "BTCUSDT",  // Symbol
 *     "i": "1m",      // Interval
 *     "f": 100,       // First trade ID
 *     "L": 200,       // Last trade ID
 *     "o": "0.0010",  // Open price
 *     "c": "0.0020",  // Close price
 *     "h": "0.0025",  // High price
 *     "l": "0.0015",  // Low price
 *     "v": "1000",    // Base asset volume
 *     "n": 100,       // Number of trades
 *     "x": false,     // Is this kline closed?
 *     "q": "1.0000",  // Quote asset volume
 *     "V": "500",     // Taker buy base asset volume
 *     "Q": "0.500",   // Taker buy quote asset volume
 *     "B": "123456"   // Ignore
 *   }
 * }
 */

public class Kline extends DefaultPojoItem {
    public Kline() {
        super(getFieldDefinition());
    }

    private static Field[] getFieldDefinition() {
        return new Field[] {
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new StringField("s", "Symbol"),
                new ObjectField("k", "Kline data", new Field[] {
                        new LongField("t", "Kline start time"),
                        new LongField("T", "Kline close time"),
                        new StringField("s", "Symbol"),
                        new StringField("i", "Interval"),
                        new LongField("f", "First trade ID"),
                        new LongField("L", "Last trade ID"),
                        new DecimalField("o", "Open price"),
                        new DecimalField("c", "Close price"),
                        new DecimalField("h", "High price"),
                        new DecimalField("l", "Low price"),
                        new DecimalField("v", "Base asset volume"),
                        new LongField("n", "Number of trades"),
                        new BooleanField("x", "Is this kline closed?"),
                        new DecimalField("q", "Quote asset volume"),
                        new DecimalField("V", "Taker buy base asset volume"),
                        new DecimalField("Q", "Taker buy quote asset volume"),
                        new StringField("B", "Ignore")
                })
        };
    }
}
