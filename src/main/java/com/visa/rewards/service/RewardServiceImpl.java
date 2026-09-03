package com.visa.rewards.service;

import com.visa.rewards.dto.MonthlyReward;
import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.exception.CustomerNotFoundException;
import com.visa.rewards.model.Transaction;
import com.visa.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository transactionRepository;
    private final RewardCalculator rewardCalculator;

    public RewardServiceImpl(
            TransactionRepository transactionRepository,
            RewardCalculator rewardCalculator) {
        this.transactionRepository = transactionRepository;
        this.rewardCalculator = rewardCalculator;
    }

    /**
     * Returns the monthly reward totals for a customer.
     *
     * A customer without any transactions is treated as having zero reward activity,
     * not as an unknown customer. This avoids misleading 404 responses for valid
     * customers with no recorded spending yet.
     */
    @Override
    public RewardResponse getRewards(String customerId) {
        List<Transaction> transactions =
                transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customerId);

        if (transactions.isEmpty()) {
            return new RewardResponse(customerId, List.of(), 0L);
        }

        Map<java.time.YearMonth, Long> pointsByMonth = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            var month = java.time.YearMonth.from(transaction.getTransactionDate());
            long points = rewardCalculator.calculate(transaction.getAmount());
            pointsByMonth.merge(month, points, Long::sum);
        }

        List<MonthlyReward> monthlyRewards = pointsByMonth.entrySet()
                .stream()
                .map(entry -> new MonthlyReward(entry.getKey(), entry.getValue()))
                .toList();

        long totalPoints = monthlyRewards.stream()
                .mapToLong(MonthlyReward::points)
                .sum();

        return new RewardResponse(customerId, monthlyRewards, totalPoints);
    }
}
