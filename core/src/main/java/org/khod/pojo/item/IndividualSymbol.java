package org.khod.pojo.item;

import org.khod.pojo.field.DecimalField;
import org.khod.pojo.field.Field;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.StringField;

/**
 * {
 *   "e": "24hrTicker",  // Event type
 *   "E": 123456789,     // Event time
 *   "s": "BTCUSDT",     // Symbol
 *   "p": "0.0015",      // Price change
 *   "P": "250.00",      // Price change percent
 *   "w": "0.0018",      // Weighted average price
 *   "c": "0.0025",      // Last price
 *   "Q": "10",          // Last quantity
 *   "o": "0.0010",      // Open price
 *   "h": "0.0025",      // High price
 *   "l": "0.0010",      // Low price
 *   "v": "10000",       // Total traded base asset volume
 *   "q": "18",          // Total traded quote asset volume
 *   "O": 0,             // Statistics open time
 *   "C": 86400000,      // Statistics close time
 *   "F": 0,             // First trade ID
 *   "L": 18150,         // Last trade Id
 *   "n": 18151          // Total number of trades
 * }
 */
public class IndividualSymbol extends DefaultPojoItem {
    public IndividualSymbol() {
        super(getFieldDefinition());
    }

    private static Field[] getFieldDefinition() {
        return new Field[] {
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new StringField("s", "Symbol"),
                new DecimalField("p", "Price change"),
                new DecimalField("P", "Price change percent"),
                new DecimalField("w", "Weighted average price"),
                new DecimalField("c", "Last price"),
                new DecimalField("Q", "Last quantity"),
                new DecimalField("o", "Open price"),
                new DecimalField("h", "High price"),
                new DecimalField("l", "Low price"),
                new DecimalField("v", "Total traded base asset volume"),
                new DecimalField("q", "Total traded quote asset volume"),
                new LongField("O", "Statistics open time"),
                new LongField("C", "Statistics close time"),
                new LongField("F", "First trade ID"),
                new LongField("L", "Last trade Id"),
                new LongField("n", "Total number of trades")
        };
    }
}
