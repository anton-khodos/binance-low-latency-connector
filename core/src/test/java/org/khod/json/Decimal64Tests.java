package org.khod.json;

import org.junit.jupiter.api.Assertions;
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
                                                                                        + ".00000000000000000000000000000000000000000000000001")
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


}
