package com.roadside.ticket.config;

import com.roadside.ticket.dto.CreateTicketRequest;
import com.roadside.ticket.dto.TicketDTO;
import com.roadside.ticket.service.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

/**
 * Spring Cloud Function configuration for AWS Lambda deployment.
 * Defines function beans that map to service methods for Lambda invocation.
 */
@Configuration
public class FunctionConfiguration {

    private final TicketService ticketService;

    public FunctionConfiguration(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Function bean for creating a breakdown ticket.
     * Maps to POST /api/v1/tickets Lambda function.
     */
    @Bean
    public Function<CreateTicketRequest, TicketDTO> createTicket() {
        return ticketService::createTicket;
    }

    /**
     * Function bean for retrieving a ticket by ID.
     * Maps to GET /api/v1/tickets/{id} Lambda function.
     */
    @Bean
    public Function<Long, TicketDTO> getTicketById() {
        return ticketService::getTicketById;
    }
}
