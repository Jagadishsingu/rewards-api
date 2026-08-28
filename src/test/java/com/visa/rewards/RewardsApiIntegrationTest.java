package com.visa.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCustomerRewards() throws Exception {
        mockMvc.perform(get("/api/rewards/CUST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST001"))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("2026-06"))
                .andExpect(jsonPath("$.monthlyRewards[0].points").value(180))
                .andExpect(jsonPath("$.monthlyRewards[1].month").value("2026-07"))
                .andExpect(jsonPath("$.monthlyRewards[1].points").value(240))
                .andExpect(jsonPath("$.monthlyRewards[2].month").value("2026-08"))
                .andExpect(jsonPath("$.monthlyRewards[2].points").value(150))
                .andExpect(jsonPath("$.totalPoints").value(570));
    }

    @Test
    void shouldReturnNotFoundForUnknownCustomer() throws Exception {
        mockMvc.perform(get("/api/rewards/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Customer not found"));
    }
}
