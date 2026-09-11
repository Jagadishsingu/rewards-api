package com.visa.rewards.controller;

import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(RewardController.class)
@Import(com.visa.rewards.exception.GlobalExceptionHandler.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Test
    void shouldReturnRewardsForValidCustomerId() throws Exception {
        RewardResponse response = new RewardResponse(
                "CUST001",
                List.of(),
                BigDecimal.ZERO
        );

        Mockito.when(rewardService.getRewards("CUST001")).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/CUST001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value("CUST001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPoints").value(0));
    }

    @Test
    void shouldRejectBlankCustomerId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/ ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400));
    }
}
