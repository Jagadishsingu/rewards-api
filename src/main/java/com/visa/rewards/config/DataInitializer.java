package com.visa.rewards.config;

import com.visa.rewards.model.Transaction;
import com.visa.rewards.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(TransactionRepository repository) {
        return args -> repository.saveAll(List.of(
                // CUST001 - June: 90 + 90 = 180
                new Transaction("CUST001", new BigDecimal("120.00"), LocalDate.of(2026, 6, 5)),
                new Transaction("CUST001", new BigDecimal("120.00"), LocalDate.of(2026, 6, 20)),

                // CUST001 - July: 150 + 90 = 240
                new Transaction("CUST001", new BigDecimal("150.00"), LocalDate.of(2026, 7, 8)),
                new Transaction("CUST001", new BigDecimal("120.00"), LocalDate.of(2026, 7, 25)),

                // CUST001 - August: 100 + 50 = 150
                new Transaction("CUST001", new BigDecimal("100.00"), LocalDate.of(2026, 8, 3)),
                new Transaction("CUST001", new BigDecimal("50.00"), LocalDate.of(2026, 8, 22)),

                // CUST002 - demonstrates lower-value purchases
                new Transaction("CUST002", new BigDecimal("45.00"), LocalDate.of(2026, 6, 10)),
                new Transaction("CUST002", new BigDecimal("75.00"), LocalDate.of(2026, 7, 12)),
                new Transaction("CUST002", new BigDecimal("200.00"), LocalDate.of(2026, 8, 15))
        ));
    }
}
