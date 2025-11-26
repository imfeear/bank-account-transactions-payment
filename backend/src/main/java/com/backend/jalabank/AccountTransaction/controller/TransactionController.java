package com.backend.jalabank.AccountTransaction.controller;

import com.backend.jalabank.AccountTransaction.DTO.TransactionDTO.DepositDTOTest;
import com.backend.jalabank.AccountTransaction.DTO.TransactionDTO.StatementResponseDTO;
import com.backend.jalabank.AccountTransaction.DTO.TransactionDTO.TransactionDTO;
import com.backend.jalabank.AccountTransaction.DTO.TransactionDTO.TransactionPixDTO;
import com.backend.jalabank.AccountTransaction.entity.Transaction;
import com.backend.jalabank.AccountTransaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody @Valid TransactionDTO transactionDTO) {
        Map<String, String> response = new HashMap<>();
        transactionService.transfer(transactionDTO);
        response.put("message", "Transferência concluída");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @PostMapping("/transfer-by-pix")
            public ResponseEntity<String> transferByPix(@RequestBody TransactionPixDTO transactionPixDTO) {
                transactionService.transferByEmail(transactionPixDTO);
                return ResponseEntity.ok("Transferência via PIX realizada com sucesso.");
            }

            

    @GetMapping("/statement")
    public ResponseEntity<List<StatementResponseDTO>> getStatement() {
        List<Transaction> transactions = transactionService.getAccountBankStatement();
        List<StatementResponseDTO> response = transactions.stream()
                .map(StatementResponseDTO::fromTransaction)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

        @PostMapping("/deposit")
    public ResponseEntity<Map<String, String>> deposit(@RequestBody @Valid DepositDTOTest depositDTO) {
        Map<String, String> response = new HashMap<>();

        boolean success = transactionService.depositTest(depositDTO); // Passe o objeto depositDTO diretamente

        if (success) {
            response.put("message", "Deposit successful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Deposit failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

