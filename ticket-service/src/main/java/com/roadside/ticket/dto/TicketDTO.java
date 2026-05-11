package com.roadside.ticket.dto;

import com.roadside.ticket.model.TicketStatus;

/**
 * Data Transfer Object for Ticket using Java 21 record.
 */
public record TicketDTO(
        Long id,
        String customerName,
        TicketStatus status,
        String mechanic
) {
}
