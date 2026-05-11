package com.roadside.customer.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for Customer using Java 21 record.
 * Records provide immutable data carriers with auto-generated
 * equals(), hashCode(), toString(), and accessor methods.
 */
public record CustomerDTO(
        Long id,

        @NotBlank(message = "Customer name is required")
        String name,

        @NotBlank(message = "Vehicle information is required")
        String vehicle
) {
}
