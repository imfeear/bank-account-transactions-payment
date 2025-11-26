package com.backend.newbank.AccountTransaction.DTO.AccountDTO;

import java.math.BigDecimal;

public record AccountDetailsDTO(
    String fullName,
    Integer accountNumber,
    Integer agencyNumber,
    BigDecimal balance
) {}

