package com.visa.rewards.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RewardCalculator {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public long calculate(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return 0;
        }

        BigDecimal normalized = amount.setScale(0, RoundingMode.DOWN);

        if (normalized.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (normalized.compareTo(ONE_HUNDRED) <= 0) {
            return normalized.subtract(FIFTY).longValue();
        }

        return 50 + normalized.subtract(ONE_HUNDRED)
                .multiply(BigDecimal.valueOf(2))
                .longValue();
    }
}
