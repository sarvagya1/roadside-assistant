package com.roadside.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Payment using Java 21 record.
 */
public record PaymentDTO(
        Long id,
        Long ticketId,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
