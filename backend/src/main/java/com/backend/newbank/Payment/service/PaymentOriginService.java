package com.backend.newbank.Payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.newbank.Payment.entity.PaymentOrigin;
import com.backend.newbank.Payment.repository.PaymentOriginRepository;

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
