package com.visa.rewards.exception;

/**
 * Raised when a customer does not exist in the system.
 * This exception is still kept for true customer lookup failures, but the reward
 * service now returns an empty response for customers with no transactions.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("Customer '" + customerId + "' was not found");
    }
}
