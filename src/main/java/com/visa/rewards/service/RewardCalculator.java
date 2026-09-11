package com.visa.rewards.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RewardCalculator {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public BigDecimal calculate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (amount.compareTo(FIFTY) <= 0) {
            return BigDecimal.ZERO;
        }

        if (amount.compareTo(ONE_HUNDRED) <= 0) {
            return amount.subtract(FIFTY);
        }

        return BigDecimal.valueOf(50)
                .add(amount.subtract(ONE_HUNDRED).multiply(BigDecimal.valueOf(2)));
    }
}
