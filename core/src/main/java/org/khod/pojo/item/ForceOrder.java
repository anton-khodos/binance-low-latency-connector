package org.khod.pojo.item;

import org.khod.pojo.field.*;

/**
 * {
 * 	"e":"forceOrder",// Event Type
 * 	"E":1568014460893,// Event Time
 * 	"o":{
 * 		"s":"BTCUSDT",// Symbol
 * 		"S":"SELL",// Side
 * 		"o":"LIMIT",// Order Type
 * 		"f":"IOC",// Time in Force
 * 		"q":"0.014",// Original Quantity
 * 		"p":"9910",// Price
 * 		"ap":"9910",// Average Price
 * 		"X":"FILLED",// Order Status
 * 		"l":"0.014",// Order Last Filled Quantity
 * 		"z":"0.014",// Order Filled Accumulated Quantity
 * 		"T":1568014460893,// Order Trade Time
 *        }
 * }
 */
public class ForceOrder extends DefaultPojoItem {
    public ForceOrder() {
        super(getFieldDefinition());
    }

    private static Field[] getFieldDefinition() {
        return new Field[]{
                new StringField("e", "Event Type"),
                new LongField("E", "Event Time"),
                new ObjectField("o", "Order Data", new Field[]{
                        new StringField("s", "Symbol"),
                        new StringField("S", "Side"),
                        new StringField("o", "Order Type"),
                        new StringField("f", "Time in Force"),
                        new DecimalField("q", "Original Quantity"),
                        new DecimalField("p", "Price"),
                        new DecimalField("ap", "Average Price"),
                        new StringField("X", "Order Status"),
                        new DecimalField("l", "Order Last Filled Quantity"),
                        new DecimalField("z", "Order Filled Accumulated Quantity"),
                        new LongField("T", "Order Trade Time"),
                })
        };
    }
}
