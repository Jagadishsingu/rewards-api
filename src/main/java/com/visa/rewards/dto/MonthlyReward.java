package com.visa.rewards.dto;

import java.time.YearMonth;

public record MonthlyReward(
        YearMonth month,
        long points
) {
}
