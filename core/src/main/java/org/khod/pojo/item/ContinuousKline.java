package org.khod.pojo.item;

import org.khod.pojo.field.Field;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.ObjectField;
import org.khod.pojo.field.StringField;

/**
 * {
 *   "e":"continuous_kline",	// Event type
 *   "E":1591261542539,		// Event time
 *   "ps":"BTCUSD",			// Pair
 *   "ct":"NEXT_QUARTER"		// Contract type
 *   "k":{
 *     "t":1591261500000,		// Kline start time
 *     "T":1591261559999,		// Kline close time
 *     "i":"1m",				// Interval
 *     "f":606400,				// First update ID
 *     "L":606430,				// Last update ID
 *     "o":"9638.9",			// Open price
 *     "c":"9639.8",			// Close price
 *     "h":"9639.8",			// High price
 *     "l":"9638.6",			// Low price
 *     "v":"156",				// volume
 *     "n":31,					// Number of trades
 *     "x":false,				// Is this kline closed?
 *     "q":"1.61836886",		// Base asset volume
 *     "V":"73",				// Taker buy volume
 *     "Q":"0.75731156",		// Taker buy base asset volume
 *     "B":"0"					// Ignore
 *   }
 * }
 */
public class ContinuousKline extends DefaultPojoItem {

    public ContinuousKline() {
        super(getFieldDefinition());
    }

    private static Field[] getFieldDefinition() {
        return new Field[] {
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new StringField("ps", "Pair"),
                new StringField("ct", "Contract type"),
                new ObjectField("k", "Kline data", new Field[] {
                        new LongField("t", "Kline start time"),
                        new LongField("T", "Kline close time"),
                        new StringField("i", "Interval"),
                        new LongField("f", "First update ID"),
                        new LongField("L", "Last update ID"),
                        new org.khod.pojo.field.DecimalField("o", "Open price"),
                        new org.khod.pojo.field.DecimalField("c", "Close price"),
                        new org.khod.pojo.field.DecimalField("h", "High price"),
                        new org.khod.pojo.field.DecimalField("l", "Low price"),
                        new org.khod.pojo.field.DecimalField("v", "Volume"),
                        new LongField("n", "Number of trades"),
                        new org.khod.pojo.field.BooleanField("x", "Is this kline closed?"),
                        new org.khod.pojo.field.DecimalField("q", "Base asset volume"),
                        new org.khod.pojo.field.DecimalField("V", "Taker buy volume"),
                        new org.khod.pojo.field.DecimalField("Q", "Taker buy base asset volume"),
                        new StringField("B", "Ignore")
                })
        };
    }
}
