package com.visa.rewards.dto;

import java.math.BigDecimal;
import java.util.List;

public record RewardResponse(
        String customerId,
        List<MonthlyReward> monthlyRewards,
        BigDecimal totalPoints
) {
}
