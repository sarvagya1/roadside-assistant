package com.roadside.ticket.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request record for assigning a mechanic to a ticket.
 */
public record AssignMechanicRequest(
        @NotBlank(message = "Mechanic name is required")
        String mechanicName
) {
}
