package com.backend.newbank.Payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.newbank.Payment.entity.PaymentStatus;

@Repository
public interface PaymentStatusRepository extends JpaRepository <PaymentStatus, Integer> {

}
