package org.khod.pojo;

import org.khod.utils.Decimal64;

public class BinanceAggTrade {
    private long eventTime;      // E
    private long tradeId;        // a
    private long firstTradeId;   // f
    private long lastTradeId;    // l
    private long tradeTime;      // T
    private boolean isMaker;     // m

    private final Decimal64 price;     // p
    private final Decimal64 quantity;  // q
    private final String symbol; // s

    public BinanceAggTrade(final String symbol) {
        this.symbol = symbol;
        this.price = new Decimal64();
        this.quantity = new Decimal64();
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(final long eventTime) {
        this.eventTime = eventTime;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(final long tradeId) {
        this.tradeId = tradeId;
    }

    public Decimal64 getPrice() {
        return price;
    }

    public void setPrice(final Decimal64 price) {
        price.copyTo(this.price);
    }

    public Decimal64 getQuantity() {
        return quantity;
    }

    public void setQuantity(final Decimal64 quantity) {
        quantity.copyTo(this.quantity);
    }

    public long getFirstTradeId() {
        return firstTradeId;
    }

    public void setFirstTradeId(final long firstTradeId) {
        this.firstTradeId = firstTradeId;
    }

    public long getLastTradeId() {
        return lastTradeId;
    }

    public void setLastTradeId(final long lastTradeId) {
        this.lastTradeId = lastTradeId;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(final long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public boolean isMaker() {
        return isMaker;
    }

    public void setMaker(final boolean maker) {
        isMaker = maker;
    }

    public String getSymbol() {
        return symbol;
    }
}
