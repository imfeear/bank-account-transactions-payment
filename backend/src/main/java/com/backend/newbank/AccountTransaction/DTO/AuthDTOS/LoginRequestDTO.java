package com.backend.newbank.AccountTransaction.DTO.AuthDTOS;

public record LoginRequestDTO(Integer accountNumber, Integer agencyNumber, String password) {
}
