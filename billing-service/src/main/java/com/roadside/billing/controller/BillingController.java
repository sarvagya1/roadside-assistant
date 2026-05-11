package com.roadside.billing.controller;

import com.roadside.billing.dto.GenerateBillRequest;
import com.roadside.billing.dto.PaymentDTO;
import com.roadside.billing.service.BillingService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for billing and payment operations.
 * Provides endpoints for bill generation and payment retrieval.
 */
@RestController
@RequestMapping("/api/v1/billing")
@Tag(name = "Billing", description = "Billing and payment management API")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @Operation(summary = "Generate a bill",
            description = "Generates a bill (payment record) for a breakdown ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bill generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/generate")
    public ResponseEntity<PaymentDTO> generateBill(
            @Valid @RequestBody GenerateBillRequest request) {
        var payment = billingService.generateBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @Operation(summary = "Get payments by ticket ID",
            description = "Retrieves all payments for a given breakdown ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments found"),
            @ApiResponse(responseCode = "404", description = "No payments found for the ticket")
    })
    @GetMapping("/payments/{ticketId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByTicketId(
            @Parameter(description = "Ticket ID") @PathVariable Long ticketId) {
        var payments = billingService.getPaymentsByTicketId(ticketId);
        return ResponseEntity.ok(payments);
    }
}
