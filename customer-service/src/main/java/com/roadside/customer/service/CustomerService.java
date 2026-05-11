package com.roadside.customer.service;

import com.roadside.customer.dto.CustomerDTO;
import com.roadside.customer.exception.CustomerNotFoundException;
import com.roadside.customer.model.Customer;
import com.roadside.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for customer business logic.
 * Uses Optional for null safety and Streams for collection processing.
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Registers a new customer in the system.
     *
     * @param customerDTO the customer data transfer object
     * @return the registered customer as DTO
     */
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        logger.info("Registering new customer: {}", customerDTO.name());

        var customer = new Customer(customerDTO.name(), customerDTO.vehicle());
        var savedCustomer = customerRepository.save(customer);

        logger.info("Customer registered successfully with id: {}", savedCustomer.getId());
        return toDTO(savedCustomer);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the customer ID
     * @return the customer as DTO
     * @throws CustomerNotFoundException if no customer exists with the given ID
     */
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        logger.info("Fetching customer with id: {}", id);

        return customerRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    /**
     * Retrieves all customers in the system.
     *
     * @return list of all customers as DTOs
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Fetching all customers");

        return customerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Converts a Customer entity to a CustomerDTO record.
     */
    private CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getVehicle()
        );
    }
}
