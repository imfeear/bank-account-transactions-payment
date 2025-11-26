package com.backend.jalabank.Payment.repository;

import com.backend.jalabank.Payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository <PaymentStatus, Integer> {

}
