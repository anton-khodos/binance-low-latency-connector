package org.khod.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.khod.data.Decimal64;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class Decimal64Tests {

    private static Stream<Arguments> decimalConversionTestProvider() {
        return Stream.of(
                Arguments.of(new Decimal64(1, -3), BigDecimal.valueOf(1, 3), 0.001, "0.001" ),
                Arguments.of(new Decimal64(0, 0), BigDecimal.valueOf(0, 0), 0, "0" ),
                Arguments.of(new Decimal64(123, -1), BigDecimal.valueOf(123, 1), 12.3, "12.3" ),
                Arguments.of(new Decimal64(-1, -3), BigDecimal.valueOf(-1, 3), -0.001, "-0.001" ),
                Arguments.of(new Decimal64(1, -50), BigDecimal.valueOf(1, 50), 1.0E-50, "0"
                                                                                        +
                        ".00000000000000000000000000000000000000000000000001"),
                Arguments.of(new Decimal64(123456789012345678L, -8), BigDecimal.valueOf(123456789012345678L, 8),
                        1234567890.1234567, "1234567890.12345678"),
                Arguments.of(new Decimal64(-123456789012345678L, -8), BigDecimal.valueOf(-123456789012345678L, 8),
                        -1234567890.1234567, "-1234567890.12345678")
        );
    }

    @ParameterizedTest
    @MethodSource("decimalConversionTestProvider")
    public void parameterizedConversionDecimalTest(Decimal64 decimal64, BigDecimal bigDecimal, double decimal,
                                                   String decimalString) {
        Assertions.assertEquals(bigDecimal, decimal64.toBigDecimal());
        Assertions.assertEquals(decimal, decimal64.toDouble());
        Assertions.assertEquals(decimalString, decimal64.toString());
    }

    @Test
    public void testDecimalNormalization() {
        Decimal64 decimal = new Decimal64(1000, -3);
        decimal.normalize();
        Assertions.assertEquals(1, decimal.getMantissa());
        Assertions.assertEquals(0, decimal.getExponent());

        decimal = new Decimal64(25000, -5);
        decimal.normalize();
        Assertions.assertEquals(25, decimal.getMantissa());
        Assertions.assertEquals(-2, decimal.getExponent());

        decimal = new Decimal64(0, 10);
        decimal.normalize();
        Assertions.assertEquals(0, decimal.getMantissa());
        Assertions.assertEquals(0, decimal.getExponent());
    }

    @Test
    public void testDecimalCopy() {
        Decimal64 original = new Decimal64(12345, -2);
        Decimal64 copy = new Decimal64();
        original.copyTo(copy);
        Assertions.assertEquals(original.getMantissa(), copy.getMantissa());
        Assertions.assertEquals(original.getExponent(), copy.getExponent());
    }

    @Test
    public void testDecimalToString() {
        Decimal64 decimal = new Decimal64(123456, -3);
        Assertions.assertEquals("123.456", decimal.toString());

        decimal = new Decimal64(0, 0);
        Assertions.assertEquals("0", decimal.toString());

        decimal = new Decimal64(-7890, -2);
        Assertions.assertEquals("-78.9", decimal.toString());
    }
}
