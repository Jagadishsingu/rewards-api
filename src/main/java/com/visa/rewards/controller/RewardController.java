package com.visa.rewards.controller;

import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.service.RewardService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
@Validated
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/{customerId}")
    public RewardResponse getRewards(
            @PathVariable
            @NotBlank(message = "Customer ID must not be blank")
            String customerId) {

        return rewardService.getRewards(customerId);
    }
}
