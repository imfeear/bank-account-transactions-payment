package com.backend.newbank.AccountTransaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.newbank.AccountTransaction.DTO.AccountDTO.BalanceDetailsDTO;
import com.backend.newbank.AccountTransaction.DTO.AccountDTO.DepositRequestDTO;
import com.backend.newbank.AccountTransaction.service.AccountService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    public ResponseEntity<BalanceDetailsDTO> deposit(@RequestBody DepositRequestDTO depositRequest) {
        Long accountId;
        try {
            // Recupera o ID do usuário autenticado
            accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Validação do valor de depósito
        if (depositRequest.getAmount().compareTo(new BigDecimal("5.00")) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BalanceDetailsDTO(BigDecimal.ZERO)); // Retorna zero ou uma mensagem, se desejar
        }

        

        // Processa o depósito e atualiza o saldo
        BigDecimal updatedBalance = accountService.deposit(accountId, depositRequest.getAmount(), depositRequest.getBoletoCode());

        return ResponseEntity.status(HttpStatus.OK).body(new BalanceDetailsDTO(updatedBalance));
    }
}
