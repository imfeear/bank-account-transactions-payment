package com.backend.jalabank.AccountTransaction.DTO.AuthDTOS;

public record ResponseLoginDTO(String username,
                               String accountAndAgency,
                               String email,
                               Long accountId,
                               String token) {
}
