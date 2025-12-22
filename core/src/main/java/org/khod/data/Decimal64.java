package org.khod.data;

import java.math.BigDecimal;

public final class Decimal64 implements Comparable<Decimal64> {
    private static final long[] POW10 = {
            1L,
            10L,
            100L,
            1_000L,
            10_000L,
            100_000L,
            1_000_000L,
            10_000_000L,
            100_000_000L,
            1_000_000_000L,
            10_000_000_000L,
            100_000_000_000L,
            1_000_000_000_000L,
            10_000_000_000_000L,
            100_000_000_000_000L,
            1_000_000_000_000_000L,
            10_000_000_000_000_000L,
            100_000_000_000_000_000L
    };

    private static final double[] DOUBLE_POW10 = {
            1e-18, 1e-17, 1e-16, 1e-15, 1e-14, 1e-13,
            1e-12, 1e-11, 1e-10, 1e-9, 1e-8, 1e-7,
            1e-6, 1e-5, 1e-4, 1e-3, 1e-2, 1e-1,
            1e0,
            1e1, 1e2, 1e3, 1e4, 1e5, 1e6,
            1e7, 1e8, 1e9, 1e10, 1e11, 1e12,
            1e13, 1e14, 1e15, 1e16, 1e17, 1e18
    };

    private final StringBuilder sb;

    private long mantissa;
    private int exponent;

    public Decimal64(long value, int scale) {
        this.mantissa = value;
        this.exponent = scale;
        normalize();
        sb = new StringBuilder(32);
    }

    public Decimal64() {
        sb = new StringBuilder(32);
    }

    public long getMantissa() {
        return mantissa;
    }

    public void setMantissa(final long mantissa) {
        this.mantissa = mantissa;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(final int exponent) {
        this.exponent = exponent;
    }

    public void copyTo(Decimal64 other) {
        other.setMantissa(mantissa);
        other.setExponent(exponent);
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(mantissa)
                         .scaleByPowerOfTen(exponent);
    }

    public double toDouble() {
        return mantissa * pow10Double(exponent);
    }

    private static double pow10Double(int exp) {
        if (exp >= -18 && exp <= 18) {
            return DOUBLE_POW10[exp + 18];
        }
        return Math.pow(10.0, exp);
    }

    public void normalize() {
        if (mantissa == 0) {
            exponent = 0;
            return;
        }

        while ((mantissa % 10) == 0) {
            mantissa /= 10;
            exponent++;
        }
    }

    @Override
    public String toString() {
        if (mantissa == 0) return "0";
        sb.setLength(0);

        String s = Long.toString(Math.abs(mantissa));
        int point = s.length() + exponent;

        if (mantissa < 0) sb.append('-');

        if (point <= 0) {
            sb.append("0.");
            for (int i = 0; i < -point; i++) sb.append('0');
            sb.append(s);
        } else if (point >= s.length()) {
            sb.append(s);
            for (int i = 0; i < point - s.length(); i++) sb.append('0');
        } else {
            sb.append(s, 0, point);
            sb.append('.');
            sb.append(s, point, s.length());
        }

        return sb.toString();
    }

    @Override
    public int compareTo(Decimal64 o) {
        if (this.mantissa == 0 && o.mantissa == 0) return 0;

        if (this.exponent == o.exponent) {
            return Long.compare(this.mantissa, o.mantissa);
        }

        int diff = this.exponent - o.exponent;

        if (diff > 0) {
            return Long.compare(
                    this.mantissa,
                    o.mantissa * POW10[diff]
            );
        } else {
            return Long.compare(
                    this.mantissa * POW10[-diff],
                    o.mantissa
            );
        }
    }
}
