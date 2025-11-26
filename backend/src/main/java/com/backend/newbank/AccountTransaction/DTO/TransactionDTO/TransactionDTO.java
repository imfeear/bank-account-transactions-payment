package com.backend.newbank.AccountTransaction.DTO.TransactionDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionDTO(
        @NotNull
        Integer destinationAccountNumber,

        @NotNull
        Integer destinationAgencyNumber,

        @NotNull
        @Positive
        Double amount
) {}