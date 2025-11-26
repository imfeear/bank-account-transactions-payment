package com.backend.newbank.Payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.newbank.Payment.entity.PaymentMethod;
import com.backend.newbank.Payment.repository.PaymentMethodRepository;

import java.util.List;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> findAll (){
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod findById (Integer paymentMethodId){
        return paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new RuntimeException("Object Not Found " + paymentMethodId));
    }

}
