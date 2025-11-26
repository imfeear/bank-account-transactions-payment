package com.backend.newbank.AccountTransaction.DTO.AccountDTO;



import java.math.BigDecimal;

public class DepositRequestDTO {
    private BigDecimal amount;
    private String boletoCode;

    // Getters e Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBoletoCode() {
        return boletoCode;
    }

    public void setBoletoCode(String boletoCode) {
        this.boletoCode = boletoCode;
    }
}


