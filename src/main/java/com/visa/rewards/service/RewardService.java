package com.visa.rewards.service;

import com.visa.rewards.dto.RewardResponse;

public interface RewardService {

    RewardResponse getRewards(String customerId);
}
