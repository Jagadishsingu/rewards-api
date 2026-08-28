package com.visa.rewards.dto;

import java.util.List;

public record RewardResponse(
        String customerId,
        List<MonthlyReward> monthlyRewards,
        long totalPoints
) {
}
