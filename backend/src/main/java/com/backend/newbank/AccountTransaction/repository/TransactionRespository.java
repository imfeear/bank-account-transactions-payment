package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRespository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOriginId(Long accountOriginId);
}
