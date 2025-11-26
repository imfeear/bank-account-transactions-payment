package com.backend.newbank.AccountTransaction.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.newbank.AccountTransaction.DTO.NotificationDTO.ResponseNotificationDTO;
import com.backend.newbank.AccountTransaction.DTO.TransactionDTO.DepositDTOTest;
import com.backend.newbank.AccountTransaction.entity.Account;
import com.backend.newbank.AccountTransaction.repository.AccountRepository;
import com.backend.newbank.AccountTransaction.repository.NaturalPersonRepository;
import com.backend.newbank.AccountTransaction.repository.PersonRepository;
import com.backend.newbank.AccountTransaction.repository.UserRepository;
import com.backend.newbank.AccountTransaction.service.NotificationsService;
import com.backend.newbank.AccountTransaction.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/teste")
public class testeController {

    @Autowired
    public PersonRepository personRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public NaturalPersonRepository naturalPersonRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationsService notificationsService;


    @GetMapping
    public String teste() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "User ID: " + accountId;
    }

    @GetMapping("/getuserinfos")
    public void getInfo() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("conta não encontrada"));
        Long personId = account.getUser().getPerson().getId();

        System.out.println(personRepository.getMontly_income(personId));
        System.out.println(account.getCreation_Date());
        System.out.println(naturalPersonRepository.getBornDate(personId));
    }


    @GetMapping("/debit")
    public void debit() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.debitAmountFromAccount(accountId, 100);
        notificationsService.sendNotification("Debito", "100 reais foram debitados da sua conta" );

    }

    @GetMapping("/credit")
    public void credit() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.creditAmountToAccount(accountId, 100);
        transactionService.registerTransaction(100.0, "Deposit", (short) 1, 1, null, accountId);
    }

    @GetMapping("/not")
    public List<ResponseNotificationDTO> getNots() {
       return notificationsService.getNotifications();
    }

    @DeleteMapping("/clearNots")
    public String clearNots() {
        notificationsService.clearNotifications();
        return "Notificações limpas";
    }

    @DeleteMapping("/excludeNotById/{id}")
    public void excludeNot(@PathVariable Long id) {
        notificationsService.excludeNotification(id);
    }
}

