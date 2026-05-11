package com.roadside.ticket.controller;

import com.roadside.ticket.dto.AssignMechanicRequest;
import com.roadside.ticket.dto.CreateTicketRequest;
import com.roadside.ticket.dto.TicketDTO;
import com.roadside.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for breakdown ticket management operations.
 * Provides endpoints for ticket creation, mechanic assignment, and retrieval.
 */
@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Ticket", description = "Breakdown ticket management API")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Create a breakdown ticket",
            description = "Creates a new breakdown ticket for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(
            @Valid @RequestBody CreateTicketRequest request) {
        var ticket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @Operation(summary = "Assign a mechanic to a ticket",
            description = "Assigns a mechanic to an existing breakdown ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanic assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "409", description = "Ticket is closed, cannot assign")
    })
    @PutMapping("/{id}/assign")
    public ResponseEntity<TicketDTO> assignMechanic(
            @Parameter(description = "Ticket ID") @PathVariable Long id,
            @Valid @RequestBody AssignMechanicRequest request) {
        var ticket = ticketService.assignMechanic(id, request);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Get ticket by ID",
            description = "Retrieves a breakdown ticket by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(
            @Parameter(description = "Ticket ID") @PathVariable Long id) {
        var ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }
}
