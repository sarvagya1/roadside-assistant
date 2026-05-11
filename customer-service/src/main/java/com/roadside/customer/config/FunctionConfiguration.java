package com.roadside.customer.config;

import com.roadside.customer.dto.CustomerDTO;
import com.roadside.customer.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Spring Cloud Function configuration for AWS Lambda deployment.
 * Defines function beans that map to service methods for Lambda invocation.
 */
@Configuration
public class FunctionConfiguration {

    private final CustomerService customerService;

    public FunctionConfiguration(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Function bean for registering a customer.
     * Maps to POST /api/v1/customers Lambda function.
     */
    @Bean
    public Function<CustomerDTO, CustomerDTO> registerCustomer() {
        return customerService::registerCustomer;
    }

    /**
     * Function bean for retrieving a customer by ID.
     * Maps to GET /api/v1/customers/{id} Lambda function.
     */
    @Bean
    public Function<Long, CustomerDTO> getCustomerById() {
        return customerService::getCustomerById;
    }

    /**
     * Supplier bean for retrieving all customers.
     * Maps to GET /api/v1/customers Lambda function.
     */
    @Bean
    public Supplier<List<CustomerDTO>> getAllCustomers() {
        return customerService::getAllCustomers;
    }
}
