package com.visa.rewards.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyReward(
        YearMonth month,
        BigDecimal points
) {
}
