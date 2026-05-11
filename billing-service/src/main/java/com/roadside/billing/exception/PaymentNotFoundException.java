package com.roadside.billing.exception;

/**
 * Exception thrown when a payment is not found in the system.
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(Long ticketId) {
        super("No payments found for ticket id: " + ticketId);
    }
}
