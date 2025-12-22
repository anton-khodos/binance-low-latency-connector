package org.khod.pojo.item;

/**
 * {
 *   "e": "depthUpdate", // Event type
 *   "E": 123456789,     // Event time
 *   "T": 123456788,     // Transaction time
 *   "s": "BTCUSDT",     // Symbol
 *   "U": 157,           // First update ID in event
 *   "u": 160,           // Final update ID in event
 *   "pu": 149,          // Final update Id in last stream(ie `u` in last stream)
 *   "b": [              // Bids to be updated
 *     [
 *       "0.0024",       // Price level to be updated
 *       "10"            // Quantity
 *     ]
 *   ],
 *   "a": [              // Asks to be updated
 *     [
 *       "0.0026",       // Price level to be updated
 *       "100"          // Quantity
 *     ]
 *   ]
 * }
 */

//TODO: work nested arrays and nested object fields in arrays.
public class DepthUpdate extends DefaultPojoItem {
    @Override
    protected org.khod.pojo.field.Field[] getFieldDefinition() {
        return new org.khod.pojo.field.Field[] {
            new org.khod.pojo.field.StringField("e", "Event type"),
            new org.khod.pojo.field.LongField("E", "Event time"),
            new org.khod.pojo.field.LongField("T", "Transaction time"),
            new org.khod.pojo.field.StringField("s", "Symbol"),
            new org.khod.pojo.field.LongField("U", "First update ID in event"),
            new org.khod.pojo.field.LongField("u", "Final update ID in event"),
            new org.khod.pojo.field.LongField("pu", "Final update Id in last stream"),
            new org.khod.pojo.field.ArrayField("b", "Bids to be updated", org.khod.pojo.field.FieldType.ARRAY, new org.khod.pojo.field.Field[] {
                new org.khod.pojo.field.StringField("", "Price level to be updated"),
                new org.khod.pojo.field.StringField("", "Quantity")
            }),
            new org.khod.pojo.field.ArrayField("a", "Asks to be updated", org.khod.pojo.field.FieldType.ARRAY, new org.khod.pojo.field.Field[] {
                new org.khod.pojo.field.StringField("", "Price level to be updated"),
                new org.khod.pojo.field.StringField("", "Quantity")
            })
        };
    }

}
