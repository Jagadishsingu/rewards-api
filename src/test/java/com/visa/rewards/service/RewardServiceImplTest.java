package com.visa.rewards.service;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        var june = new Transaction("CUST001", new BigDecimal("120"), LocalDate.of(2026, 6, 10));
        var july = new Transaction("CUST001", new BigDecimal("150"), LocalDate.of(2026, 7, 10));

        when(repository.findByCustomerIdOrderByTransactionDateAsc("CUST001"))
                .thenReturn(List.of(june, july));
        when(calculator.calculate(new BigDecimal("120"))).thenReturn(90L);
        when(calculator.calculate(new BigDecimal("150"))).thenReturn(150L);

        RewardResponse response = service.getRewards("CUST001");

        assertEquals("CUST001", response.customerId());
        assertEquals(2, response.monthlyRewards().size());
        assertEquals(90, response.monthlyRewards().get(0).points());
        assertEquals(150, response.monthlyRewards().get(1).points());
        assertEquals(240, response.totalPoints());
    }

    @Test
    void shouldRejectUnknownCustomer() {
        when(repository.findByCustomerIdOrderByTransactionDateAsc("UNKNOWN"))
                .thenReturn(List.of());

        assertThrows(
                com.visa.rewards.exception.CustomerNotFoundException.class,
                () -> service.getRewards("UNKNOWN")
        );
    }
}
