package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOriginIdOrAccountDestinationId(Long originId, Long destinationId);
}
