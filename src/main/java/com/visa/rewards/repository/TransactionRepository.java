package com.visa.rewards.repository;

import com.visa.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdOrderByTransactionDateAsc(String customerId);
}
