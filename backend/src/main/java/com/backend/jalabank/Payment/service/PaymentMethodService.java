package com.backend.jalabank.Payment.service;

import com.backend.jalabank.Payment.entity.PaymentMethod;
import com.backend.jalabank.Payment.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
