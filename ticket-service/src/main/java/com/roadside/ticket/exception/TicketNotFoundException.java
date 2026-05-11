package com.roadside.ticket.exception;

/**
 * Exception thrown when a ticket is not found in the system.
 */
public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Ticket not found with id: " + id);
    }
}
