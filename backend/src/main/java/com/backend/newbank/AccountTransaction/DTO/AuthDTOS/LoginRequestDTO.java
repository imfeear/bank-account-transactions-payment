package com.backend.jalabank.AccountTransaction.DTO.AuthDTOS;

public record LoginRequestDTO(Integer accountNumber, Integer agencyNumber, String password) {
}
