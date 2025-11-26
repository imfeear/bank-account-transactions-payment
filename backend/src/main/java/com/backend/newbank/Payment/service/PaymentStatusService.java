package com.backend.newbank.Payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.newbank.Payment.entity.PaymentStatus;
import com.backend.newbank.Payment.repository.PaymentStatusRepository;

import java.util.List;

@Service
public class PaymentStatusService {

    @Autowired
    private PaymentStatusRepository statusRepository;

    public List<PaymentStatus> findAll (){
        return statusRepository.findAll();
    }

    public PaymentStatus findById (Integer paymentStatusId){
        return statusRepository.findById(paymentStatusId).orElseThrow(() -> new RuntimeException("Object Not Found " + paymentStatusId));
    }

}
