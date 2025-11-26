package com.backend.jalabank.Payment.service;

import com.backend.jalabank.Payment.entity.PaymentStatus;
import com.backend.jalabank.Payment.repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
