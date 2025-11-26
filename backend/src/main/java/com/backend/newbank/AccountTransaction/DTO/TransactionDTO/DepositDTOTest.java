package com.backend.newbank.AccountTransaction.DTO.TransactionDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record DepositDTOTest(
        @NotNull
        String accountNumber,

        @NotNull
        String agencyNumber,

        @NotNull
        @Positive
        BigDecimal amount
) {}