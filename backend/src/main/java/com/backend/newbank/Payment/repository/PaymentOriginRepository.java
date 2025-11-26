package com.backend.newbank.Payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.newbank.Payment.entity.PaymentOrigin;

@Repository
public interface PaymentOriginRepository extends JpaRepository <PaymentOrigin, Integer> {


}
