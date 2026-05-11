package com.roadside.ticket.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request record for creating a new breakdown ticket.
 */
public record CreateTicketRequest(
        @NotBlank(message = "Customer name is required")
        String customerName
) {
}
