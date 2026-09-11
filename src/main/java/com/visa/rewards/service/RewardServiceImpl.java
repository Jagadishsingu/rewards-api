package com.visa.rewards.service;

import com.visa.rewards.dto.MonthlyReward;
import com.visa.rewards.dto.RewardResponse;
import com.visa.rewards.model.Transaction;
import com.visa.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
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
     * which keeps the API response consistent and avoids misclassifying a valid
     * customer as missing.
     */
    @Override
    public RewardResponse getRewards(String customerId) {
        List<Transaction> transactions =
                transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customerId);

        if (transactions.isEmpty()) {
            return new RewardResponse(customerId, List.of(), BigDecimal.ZERO);
        }

        Map<YearMonth, BigDecimal> pointsByMonth = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            YearMonth month = YearMonth.from(transaction.getTransactionDate());
            BigDecimal points = rewardCalculator.calculate(transaction.getAmount());
            pointsByMonth.merge(month, points, BigDecimal::add);
        }

        List<MonthlyReward> monthlyRewards = pointsByMonth.entrySet()
                .stream()
                .map(entry -> new MonthlyReward(entry.getKey(), entry.getValue()))
                .toList();

        BigDecimal totalPoints = monthlyRewards.stream()
                .map(MonthlyReward::points)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RewardResponse(customerId, monthlyRewards, totalPoints);
    }
}
