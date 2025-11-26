package com.backend.jalabank.AccountTransaction.DTO.TransactionDTO;

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