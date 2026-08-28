package com.visa.rewards.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("No transactions found for customer '" + customerId + "'");
    }
}
