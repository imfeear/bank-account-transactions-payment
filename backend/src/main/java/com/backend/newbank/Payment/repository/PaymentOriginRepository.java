package com.backend.jalabank.Payment.repository;

import com.backend.jalabank.Payment.entity.PaymentOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOriginRepository extends JpaRepository <PaymentOrigin, Integer> {


}
