package com.backend.jalabank.AccountTransaction.DTO.TransactionDTO;

import com.backend.jalabank.AccountTransaction.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementResponseDTO(
        Long id,
        String transactionType,
        BigDecimal amount,
        LocalDateTime transactionDate,
        String description,
        Integer originAccountNumber,
        Integer destinationAccountNumber,
        String status
) {
    // Método estático para mapear da entidade para o DTO
    public static StatementResponseDTO fromTransaction(Transaction transaction) {
        return new StatementResponseDTO(
                transaction.getId(),
                transaction.getTransactionType().getTransactionType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getAccountOrigin() != null ?
                        transaction.getAccountOrigin().getAccount_Number() : null,
                transaction.getAccountDestination() != null ?
                        transaction.getAccountDestination().getAccount_Number() : null,
                transaction.getStatus().getName()
        );
    }
}