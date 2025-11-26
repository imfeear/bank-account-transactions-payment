package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Transaction_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<Transaction_Type, Long> {
    Optional<Transaction_Type> findByCode(int code);
    Optional<Transaction_Type> findByTransactionType(String transactionType); // Ajuste para o nome correto do campo
}

