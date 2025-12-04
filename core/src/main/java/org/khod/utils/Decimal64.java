package org.khod.utils;

public final class Decimal64 {
    private long mantisa;
    private int exponent;

    public Decimal64(long value, int scale) {
        this.mantisa = value;
        this.exponent = scale;
    }

    public Decimal64() {
    }

    public long getMantisa() {
        return mantisa;
    }

    public void setMantisa(final long mantisa) {
        this.mantisa = mantisa;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(final int exponent) {
        this.exponent = exponent;
    }

    public void copyTo(Decimal64 other) {
        other.setMantisa(mantisa);
        other.setExponent(exponent);
    }
}
