package com.backend.jalabank.Payment.service;

import com.backend.jalabank.Payment.entity.PaymentOrigin;
import com.backend.jalabank.Payment.repository.PaymentOriginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentOriginService {
    @Autowired
    private PaymentOriginRepository repo;

    public List<PaymentOrigin> findAll() {
        return repo.findAll();
    }

    public PaymentOrigin findById(Integer paymentOriginId) {
        return repo.findById(paymentOriginId).orElseThrow(() -> new RuntimeException("Object Not Found with id: " + paymentOriginId));
    }

}
