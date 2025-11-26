package com.backend.jalabank.Payment.repository;

import com.backend.jalabank.Payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.paymentStatusId.id = :statusId")
    List<Payment> findPendingPayment(@Param("statusId") Integer statusId);


}
