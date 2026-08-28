package com.visa.rewards.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private final RewardCalculator calculator = new RewardCalculator();

    @Test
    void shouldReturnZeroForAmountAtOrBelowFifty() {
        assertEquals(0, calculator.calculate(new BigDecimal("50.00")));
        assertEquals(0, calculator.calculate(new BigDecimal("49.99")));
    }

    @Test
    void shouldCalculateOnePointPerDollarBetweenFiftyAndOneHundred() {
        assertEquals(10, calculator.calculate(new BigDecimal("60.00")));
        assertEquals(50, calculator.calculate(new BigDecimal("100.00")));
    }

    @Test
    void shouldCalculateTwoPointsPerDollarAboveOneHundred() {
        assertEquals(90, calculator.calculate(new BigDecimal("120.00")));
        assertEquals(250, calculator.calculate(new BigDecimal("200.00")));
    }

    @Test
    void shouldIgnoreFractionalDollars() {
        assertEquals(10, calculator.calculate(new BigDecimal("60.99")));
        assertEquals(90, calculator.calculate(new BigDecimal("120.99")));
    }

    @Test
    void shouldReturnZeroForNullOrNonPositiveAmount() {
        assertEquals(0, calculator.calculate(null));
        assertEquals(0, calculator.calculate(BigDecimal.ZERO));
        assertEquals(0, calculator.calculate(new BigDecimal("-10.00")));
    }
}
