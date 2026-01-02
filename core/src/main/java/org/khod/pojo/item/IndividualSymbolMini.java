package org.khod.pojo.item;

import org.khod.pojo.field.DecimalField;
import org.khod.pojo.field.Field;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.StringField;

/**
 *   {
 *     "e": "24hrMiniTicker",  // Event type
 *     "E": 123456789,         // Event time
 *     "s": "BTCUSDT",         // Symbol
 *     "c": "0.0025",          // Close price
 *     "o": "0.0010",          // Open price
 *     "h": "0.0025",          // High price
 *     "l": "0.0010",          // Low price
 *     "v": "10000",           // Total traded base asset volume
 *     "q": "18"               // Total traded quote asset volume
 *   }
 */
public class IndividualSymbolMini extends DefaultPojoItem {
    public IndividualSymbolMini() {
        super(getFieldDefinition());
    }

    private static Field[] getFieldDefinition() {
        return new Field[] {
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new StringField("s", "Symbol"),
                new DecimalField("c", "Close price"),
                new DecimalField("o", "Open price"),
                new DecimalField("h", "High price"),
                new DecimalField("l", "Low price"),
                new DecimalField("v", "Total traded base asset volume"),
                new DecimalField("q", "Total traded quote asset volume")
        };
    }
}
