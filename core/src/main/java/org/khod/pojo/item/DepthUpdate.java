package org.khod.pojo.item;

import org.khod.pojo.field.*;

/**
 * {
 * "e": "depthUpdate", // Event type
 * "E": 123456789,     // Event time
 * "T": 123456788,     // Transaction time
 * "s": "BTCUSDT",     // Symbol
 * "U": 157,           // First update ID in event
 * "u": 160,           // Final update ID in event
 * "pu": 149,          // Final update Id in last stream(ie `u` in last stream)
 * "b": [              // Bids to be updated
 * [
 * "0.0024",       // Price level to be updated
 * "10"            // Quantity
 * ]
 * ],
 * "a": [              // Asks to be updated
 * [
 * "0.0026",       // Price level to be updated
 * "100"          // Quantity
 * ]
 * ]
 * }
 */

public class DepthUpdate extends DefaultPojoItem {

    public DepthUpdate(int maxSize) {
        super(getFieldDefinition(maxSize));
    }

    private static Field[] getFieldDefinition(int maxSize) {
        return new Field[]{
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new LongField("T", "Transaction time"),
                new StringField("s", "Symbol"),
                new LongField("U", "First update ID in event"),
                new LongField("u", "Final update ID in event"),
                new LongField("pu", "Final update Id in last stream"),
                generateArrayField("b", "Bids to be updated", maxSize),
                generateArrayField("a", "Asks to be updated", maxSize),
        };
    }

    private static ArrayField generateArrayField(final String name, final String description, int maxSize) {
        ArrayField[] fields = new ArrayField[maxSize];
        for (int i = 0; i < maxSize; i++) {
            ArrayField internalArrayField = new ArrayField("", "Internal Pair",
                    new org.khod.pojo.field.Field[]{
                            new DecimalField("", "Price level to be updated"),
                            new DecimalField("", "Quantity")
                    });
            fields[i] = internalArrayField;
        }
        return new ArrayField(name, description, fields);
    }
}
