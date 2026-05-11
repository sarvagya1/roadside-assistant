package com.roadside.billing.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request record for generating a bill for a ticket.
 */
public record GenerateBillRequest(
        @NotNull(message = "Ticket ID is required")
        Long ticketId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
) {
}
