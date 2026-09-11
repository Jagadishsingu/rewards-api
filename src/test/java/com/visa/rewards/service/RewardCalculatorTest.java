package com.visa.rewards.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private final RewardCalculator calculator = new RewardCalculator();

    @Test
    void shouldReturnZeroForAmountAtFiftyDollars() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(new BigDecimal("50.00")));
    }

    @Test
    void shouldReturnZeroForAmountBelowFiftyDollars() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(new BigDecimal("49.99")));
    }

    @Test
    void shouldCalculateTenPointsForSixtyDollarPurchase() {
        assertEquals(new BigDecimal("10.00"), calculator.calculate(new BigDecimal("60.00")));
    }

    @Test
    void shouldCalculateFiftyPointsForOneHundredDollarPurchase() {
        assertEquals(new BigDecimal("50.00"), calculator.calculate(new BigDecimal("100.00")));
    }

    @Test
    void shouldCalculateNinetyPointsForOneHundredTwentyDollarPurchase() {
        assertEquals(new BigDecimal("90.00"), calculator.calculate(new BigDecimal("120.00")));
    }

    @Test
    void shouldCalculateTwoHundredFiftyPointsForTwoHundredDollarPurchase() {
        assertEquals(new BigDecimal("250.00"), calculator.calculate(new BigDecimal("200.00")));
    }

    @Test
    void shouldPreservePositiveFractionalDollarPrecisionAtSixtyNinetyNine() {
        assertEquals(new BigDecimal("10.99"), calculator.calculate(new BigDecimal("60.99")));
    }

    @Test
    void shouldPreservePositiveFractionalDollarPrecisionAboveOneHundred() {
        assertEquals(new BigDecimal("91.98"), calculator.calculate(new BigDecimal("120.99")));
    }

    @Test
    void shouldReturnZeroForNullAmount() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(null));
    }

    @Test
    void shouldReturnZeroForZeroAmount() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(BigDecimal.ZERO));
    }

    @Test
    void shouldReturnZeroForNegativeAmount() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(new BigDecimal("-10.00")));
    }
}
