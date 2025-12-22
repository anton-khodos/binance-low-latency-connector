package org.khod.pojo.item;

import org.khod.pojo.field.DecimalField;
import org.khod.pojo.field.Field;
import org.khod.pojo.field.LongField;
import org.khod.pojo.field.StringField;

/**
 *   {
 *     "e": "markPriceUpdate",  	// Event type
 *     "E": 1562305380000,      	// Event time
 *     "s": "BTCUSDT",          	// Symbol
 *     "p": "11794.15000000",   	// Mark price
 *     "i": "11784.62659091",		// Index price
 *     "P": "11784.25641265",		// Estimated Settle Price, only useful in the last hour before the settlement starts
 *     "r": "0.00038167",       	// Funding rate
 *     "T": 1562306400000       	// Next funding time
 *   }
 */

public class MarkPrice extends DefaultPojoItem {
    @Override
    protected Field[] getFieldDefinition() {
        return new Field[]{
                new StringField("e", "Event type"),
                new LongField("E", "Event time"),
                new StringField("s", "Symbol"),
                new DecimalField("p", "Mark price"),
                new DecimalField("i", "Index price"),
                new DecimalField("P", "Estimated Settle Price"),
                new DecimalField("r", "Funding rate"),
                new LongField("T", "Next funding time")
        };
    }
}