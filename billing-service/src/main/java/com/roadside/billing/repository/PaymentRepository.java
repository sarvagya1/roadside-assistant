package com.roadside.billing.repository;

import com.roadside.billing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Payment entity.
 * Provides standard CRUD operations and custom query methods.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByTicketId(Long ticketId);
}
