package com.backend.newbank.Payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.newbank.Payment.entity.Receipt;
import com.backend.newbank.Payment.repository.ReceiptRepository;

import java.util.List;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRep;

    public List<Receipt> findAll (){
        return receiptRep.findAll();
    }

    public Receipt findById (Long receiptId){
        return receiptRep.findById(receiptId).orElseThrow(() -> new RuntimeException("Object Not Found " + receiptId));
    }
}
