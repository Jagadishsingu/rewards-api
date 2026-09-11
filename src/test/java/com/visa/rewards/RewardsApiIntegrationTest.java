package com.visa.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCustomerRewards() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/CUST001"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value("CUST001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[0].month").value("2026-06"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[0].points").value(180))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[1].month").value("2026-07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[1].points").value(240))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[2].month").value("2026-08"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards[2].points").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPoints").value(470));
    }

    @Test
    void shouldReturnEmptyRewardsForCustomerWithoutTransactions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/UNKNOWN"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value("UNKNOWN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyRewards.length()").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPoints").value(0));
    }
}
