package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.AccountTransaction.entity.Transaction;

import java.util.List;


public interface TransactionRespository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOriginId(Long accountOriginId);
}
