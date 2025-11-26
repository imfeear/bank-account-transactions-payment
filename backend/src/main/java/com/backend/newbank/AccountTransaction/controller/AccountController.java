package com.backend.newbank.AccountTransaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.newbank.AccountTransaction.DTO.AccountDTO.BalanceDetailsDTO;
import com.backend.newbank.AccountTransaction.DTO.AccountDTO.UpdateAccountDTO;
import com.backend.newbank.AccountTransaction.service.AccountService;
import com.backend.newbank.Payment.entity.PixKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceDetailsDTO> getAccountBalance() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountBalance(accountId));
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> getUserByAccountAndAgency(
            @RequestParam Integer accountNumber,
            @RequestParam Integer agencyNumber) {

        String fullName = accountService.findFullNameByAccountAndAgency(accountNumber, agencyNumber);

        if (fullName != null) {
            Map<String, String> response = new HashMap<>();
            response.put("name", fullName);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAccountDetails(@RequestBody UpdateAccountDTO updateAccountDTO) {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean updated = accountService.updateAccountDetails(accountId, updateAccountDTO);

        if (updated) {
            return ResponseEntity.ok("Account details updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update account details.");
        }
    }

    @GetMapping("/account-id")
public ResponseEntity<Long> getAuthenticatedAccountId() {
    Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(accountId);
}

    @GetMapping("/details")
    public ResponseEntity<UpdateAccountDTO> getAccountDetails() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Buscar os detalhes da conta com base no ID do usuário autenticado
        UpdateAccountDTO accountDetails = accountService.getAccountDetails(accountId);

        return ResponseEntity.ok(accountDetails);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.deleteAccount(accountId);

        return ResponseEntity.ok("Account deleted successfully");
    }

    // NOVO: Endpoint para listar as chaves PIX associadas à conta
    @GetMapping("/pix-keys")
    public ResponseEntity<List<String>> getPixKeys() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca as chaves PIX associadas ao usuário da conta
        List<String> pixKeys = accountService.getPixKeysByAccount(accountId)
                .stream()
                .map(PixKey::getKey) // Extrai apenas as chaves (emails)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pixKeys);
    }

    // NOVO: Endpoint para cadastrar uma nova chave PIX associada à conta
    @PostMapping("/register-pix-keys")
    public ResponseEntity<String> registerPixKey(@RequestBody Map<String, String> request) {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Valida o email recebido
        String pixKey = request.get("pixKey");
        if (pixKey == null || pixKey.isBlank()) {
            return ResponseEntity.badRequest().body("A chave PIX não pode ser vazia.");
        }

        boolean success = accountService.registerPixKey(accountId, pixKey);

        if (success) {
            return ResponseEntity.ok("Chave PIX cadastrada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao cadastrar a chave PIX.");
        }
    }
}
