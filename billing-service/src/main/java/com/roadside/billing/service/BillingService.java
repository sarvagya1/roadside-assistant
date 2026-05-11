package com.roadside.billing.service;

import com.roadside.billing.dto.GenerateBillRequest;
import com.roadside.billing.dto.PaymentDTO;
import com.roadside.billing.exception.PaymentNotFoundException;
import com.roadside.billing.model.Payment;
import com.roadside.billing.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for billing business logic.
 * Uses Optional, Streams, and text blocks for modern Java patterns.
 */
@Service
@Transactional
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    private final PaymentRepository paymentRepository;

    public BillingService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Generates a bill (payment record) for a ticket.
     *
     * @param request the generate bill request containing ticket ID and amount
     * @return the created payment as DTO
     */
    public PaymentDTO generateBill(GenerateBillRequest request) {
        logger.info("Generating bill for ticket: {} with amount: {}",
                request.ticketId(), request.amount());

        var payment = new Payment(request.ticketId(), request.amount());
        var savedPayment = paymentRepository.save(payment);

        logger.info("Bill generated successfully with payment id: {}", savedPayment.getId());
        return toDTO(savedPayment);
    }

    /**
     * Retrieves all payments for a given ticket.
     *
     * @param ticketId the ticket ID
     * @return list of payments for the ticket
     * @throws PaymentNotFoundException if no payments exist for the ticket
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByTicketId(Long ticketId) {
        logger.info("Fetching payments for ticket: {}", ticketId);

        var payments = paymentRepository.findByTicketId(ticketId)
                .stream()
                .map(this::toDTO)
                .toList();

        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(ticketId);
        }

        return payments;
    }

    /**
     * Converts a Payment entity to a PaymentDTO record.
     */
    private PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getTicketId(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
