package com.visa.rewards.service;

import com.visa.rewards.dto.MonthlyReward;
import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.model.Transaction;
import com.visa.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private RewardCalculator calculator;

    @InjectMocks
    private RewardServiceImpl service;

    @Test
    void shouldAggregatePointsByMonthAndCalculateTotal() {
        var june = new Transaction("CUST001", new BigDecimal("120.00"), LocalDate.of(2026, 6, 10));
        var july = new Transaction("CUST001", new BigDecimal("150.00"), LocalDate.of(2026, 7, 10));

        when(repository.findByCustomerIdOrderByTransactionDateAsc("CUST001"))
                .thenReturn(List.of(june, july));
        when(calculator.calculate(new BigDecimal("120.00"))).thenReturn(new BigDecimal("90.00"));
        when(calculator.calculate(new BigDecimal("150.00"))).thenReturn(new BigDecimal("150.00"));

        RewardResponse response = service.getRewards("CUST001");

        assertEquals("CUST001", response.customerId());
        assertEquals(List.of(
                new MonthlyReward(YearMonth.of(2026, 6), new BigDecimal("90.00")),
                new MonthlyReward(YearMonth.of(2026, 7), new BigDecimal("150.00"))
        ), response.monthlyRewards());
        assertEquals(new BigDecimal("240.00"), response.totalPoints());
    }

    @Test
    void shouldReturnEmptyRewardsForCustomerWithoutTransactions() {
        when(repository.findByCustomerIdOrderByTransactionDateAsc("UNKNOWN"))
                .thenReturn(List.of());

        RewardResponse response = service.getRewards("UNKNOWN");

        assertEquals("UNKNOWN", response.customerId());
        assertEquals(List.of(), response.monthlyRewards());
        assertEquals(BigDecimal.ZERO, response.totalPoints());
    }
}
