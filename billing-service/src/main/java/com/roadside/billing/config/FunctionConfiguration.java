package com.roadside.billing.config;

import com.roadside.billing.dto.GenerateBillRequest;
import com.roadside.billing.dto.PaymentDTO;
import com.roadside.billing.service.BillingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

/**
 * Spring Cloud Function configuration for AWS Lambda deployment.
 * Defines function beans that map to service methods for Lambda invocation.
 */
@Configuration
public class FunctionConfiguration {

    private final BillingService billingService;

    public FunctionConfiguration(BillingService billingService) {
        this.billingService = billingService;
    }

    /**
     * Function bean for generating a bill.
     * Maps to POST /api/v1/billing/generate Lambda function.
     */
    @Bean
    public Function<GenerateBillRequest, PaymentDTO> generateBill() {
        return billingService::generateBill;
    }

    /**
     * Function bean for retrieving payments by ticket ID.
     * Maps to GET /api/v1/billing/payments/{ticketId} Lambda function.
     */
    @Bean
    public Function<Long, List<PaymentDTO>> getPaymentsByTicketId() {
        return billingService::getPaymentsByTicketId;
    }
}
