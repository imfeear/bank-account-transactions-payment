package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.AccountTransaction.entity.Transaction_Type;

import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<Transaction_Type, Long> {
    Optional<Transaction_Type> findByCode(int code);
    Optional<Transaction_Type> findByTransactionType(String transactionType); // Ajuste para o nome correto do campo
}

