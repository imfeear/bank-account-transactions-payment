package com.backend.newbank.exception;

public class InsuficientFundsException extends RuntimeException {
    public InsuficientFundsException(String message) {
        super(message);
    }
}
