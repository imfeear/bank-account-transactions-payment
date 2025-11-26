package com.backend.newbank.AccountTransaction.DTO.TransactionDTO;

public class TransactionPixDTO {
    private String destinationPixKey; // Chave PIX de destino
    private double amount; // Valor da transferência

    // Getters e Setters
    public String getDestinationPixKey() {
        return destinationPixKey;
    }

    public void setDestinationPixKey(String destinationPixKey) {
        this.destinationPixKey = destinationPixKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

