package com.visa.rewards.controller;

import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.service.RewardService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @Size(min = 2, max = 50, message = "Customer ID length must be between 2 and 50 characters")
            @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Customer ID can only contain letters, numbers, underscores and hyphens")
            String customerId) {

        return rewardService.getRewards(customerId);
    }
}
