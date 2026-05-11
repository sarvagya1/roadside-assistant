package com.roadside.ticket.service;

import com.roadside.ticket.dto.AssignMechanicRequest;
import com.roadside.ticket.dto.CreateTicketRequest;
import com.roadside.ticket.dto.TicketDTO;
import com.roadside.ticket.exception.TicketNotFoundException;
import com.roadside.ticket.model.Ticket;
import com.roadside.ticket.model.TicketStatus;
import com.roadside.ticket.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for ticket business logic.
 * Uses Optional, switch expressions, and streams for modern Java patterns.
 */
@Service
@Transactional
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Creates a new breakdown ticket for a customer.
     *
     * @param request the create ticket request
     * @return the created ticket as DTO
     */
    public TicketDTO createTicket(CreateTicketRequest request) {
        logger.info("Creating breakdown ticket for customer: {}", request.customerName());

        var ticket = new Ticket(request.customerName(), TicketStatus.OPEN);
        var savedTicket = ticketRepository.save(ticket);

        logger.info("Ticket created successfully with id: {}", savedTicket.getId());
        return toDTO(savedTicket);
    }

    /**
     * Assigns a mechanic to an existing ticket.
     * Uses switch expression for status validation.
     *
     * @param ticketId the ticket ID
     * @param request  the assign mechanic request
     * @return the updated ticket as DTO
     */
    public TicketDTO assignMechanic(Long ticketId, AssignMechanicRequest request) {
        logger.info("Assigning mechanic '{}' to ticket: {}", request.mechanicName(), ticketId);

        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // Use switch expression to validate ticket status
        var canAssign = switch (ticket.getStatus()) {
            case OPEN -> true;
            case ASSIGNED -> {
                logger.warn("Ticket {} is already assigned, reassigning", ticketId);
                yield true;
            }
            case CLOSED -> false;
        };

        if (!canAssign) {
            throw new IllegalStateException("Cannot assign mechanic to a closed ticket");
        }

        ticket.setMechanic(request.mechanicName());
        ticket.setStatus(TicketStatus.ASSIGNED);
        var updatedTicket = ticketRepository.save(ticket);

        logger.info("Mechanic '{}' assigned to ticket: {}", request.mechanicName(), ticketId);
        return toDTO(updatedTicket);
    }

    /**
     * Retrieves a ticket by its ID.
     *
     * @param id the ticket ID
     * @return the ticket as DTO
     * @throws TicketNotFoundException if no ticket exists with the given ID
     */
    @Transactional(readOnly = true)
    public TicketDTO getTicketById(Long id) {
        logger.info("Fetching ticket with id: {}", id);

        return ticketRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    /**
     * Retrieves all tickets filtered by status.
     *
     * @param status the ticket status to filter by
     * @return list of tickets with the given status
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketsByStatus(TicketStatus status) {
        logger.info("Fetching tickets with status: {}", status);

        return ticketRepository.findByStatus(status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Converts a Ticket entity to a TicketDTO record.
     */
    private TicketDTO toDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getCustomerName(),
                ticket.getStatus(),
                ticket.getMechanic()
        );
    }
}
