package com.roadside.ticket.repository;

import com.roadside.ticket.model.Ticket;
import com.roadside.ticket.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Ticket entity.
 * Provides standard CRUD operations and custom query methods.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomerName(String customerName);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByMechanic(String mechanic);
}
