package com.backend.newbank.AccountTransaction.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.newbank.AccountTransaction.DTO.TransactionDTO.DepositDTOTest;
import com.backend.newbank.AccountTransaction.DTO.TransactionDTO.TransactionDTO;
import com.backend.newbank.AccountTransaction.DTO.TransactionDTO.TransactionPixDTO;
import com.backend.newbank.AccountTransaction.entity.Account;
import com.backend.newbank.AccountTransaction.entity.Transaction;
import com.backend.newbank.AccountTransaction.entity.Transaction_Type;
import com.backend.newbank.AccountTransaction.repository.AccountRepository;
import com.backend.newbank.AccountTransaction.repository.StatusBankRepository;
import com.backend.newbank.AccountTransaction.repository.TransactionRepository;
import com.backend.newbank.AccountTransaction.repository.TransactionTypeRepository;
import com.backend.newbank.Payment.entity.PixKey;
import com.backend.newbank.Payment.repository.PixKeyRepository;
import com.backend.newbank.common.entity.Status;
import com.backend.newbank.exception.EmptyListException;
import com.backend.newbank.exception.InsuficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private StatusBankRepository statusRepository;
    

    private final PixKeyRepository pixKeyRepository;

    @Autowired
    private NotificationsService notificationsService;

     public TransactionService(
        PixKeyRepository pixKeyRepository
    ) {
        this.pixKeyRepository = pixKeyRepository; // Inicialização do repositório
    }

    @Transactional
    public void transfer(TransactionDTO transactionDTO) {
        Long sourceAccountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada"));
    
        Integer destinationAccountNumber = transactionDTO.destinationAccountNumber();
        Integer destinationAgencyNumber = transactionDTO.destinationAgencyNumber();
    
        if (sourceAccount.getAccount_Number().equals(destinationAccountNumber) &&
                sourceAccount.getAgency_Number().equals(destinationAgencyNumber)) {
            throw new IllegalArgumentException("Transferência para a mesma conta de origem não é permitida");
        }
    
        BigDecimal transferAmount = BigDecimal.valueOf(transactionDTO.amount());
        if (sourceAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsuficientFundsException("Saldo insuficiente");
        }
    
        Account destinationAccount = accountRepository.findByAccount_NumberAndAgency_Number(destinationAccountNumber, destinationAgencyNumber)
                .orElseThrow(() -> new EntityNotFoundException("Conta de destino não encontrada"));
    
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
    
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    
        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccountOrigin(sourceAccount);
        transaction.setAccountDestination(destinationAccount);
        transaction.setDescription("Transferência de conta " + sourceAccount.getAccount_Number() +
                " para conta " + destinationAccount.getAccount_Number());
    
        Transaction_Type transferType = transactionTypeRepository.findByCode(2)
                .orElseGet(() -> {
                    Transaction_Type newType = new Transaction_Type();
                    newType.setTransactionType("TRANSFERENCIA");
                    newType.setCode(2);
                    return transactionTypeRepository.save(newType);
                });
        transaction.setTransactionType(transferType);
    
        Status status = statusRepository.findByCode((short) 1)
                .orElseGet(() -> {
                    Status newStatus = new Status();
                    newStatus.setName("Complete");
                    newStatus.setCode((short) 1);
                    return statusRepository.save(newStatus);
                });
        transaction.setStatus(status);
    
        transactionRepository.save(transaction);
    
        // Envia notificações específicas
        notificationsService.sendNotification("Transferência Enviada", "Você enviou R$ " + transferAmount +
                " para a conta " + destinationAccountNumber + " com sucesso.");
        notificationsService.sendNotification("Transferência Recebida", "Você recebeu R$ " + transferAmount +
                " da conta " + sourceAccount.getAccount_Number() + ".");
    }
    

    @Transactional
    public void transferByEmail(TransactionPixDTO transactionPixDTO) {
        Long sourceAccountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        // Conta de origem
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada"));
    
        // Chave PIX de destino
        String destinationPixKey = transactionPixDTO.getDestinationPixKey();
        PixKey pixKey = pixKeyRepository.findByKey(destinationPixKey)
                .orElseThrow(() -> new EntityNotFoundException("Chave PIX de destino não encontrada"));
    
        // Conta de destino
        Account destinationAccount = accountRepository.findByUser_Id(pixKey.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Conta de destino associada à chave PIX não encontrada"));
    
        // Validações
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new IllegalArgumentException("Transferência para a mesma conta não é permitida");
        }
    
        BigDecimal transferAmount = BigDecimal.valueOf(transactionPixDTO.getAmount());
        if (sourceAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsuficientFundsException("Saldo insuficiente");
        }
    
        // Realiza a transferência
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
    
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    
        // Registrar transação
        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccountOrigin(sourceAccount);
        transaction.setAccountDestination(destinationAccount);
        transaction.setDescription("Transferência via PIX para chave " + destinationPixKey);
    
        // Registra ou busca o tipo de transação PIX
        Transaction_Type pixType = transactionTypeRepository.findByCode(3)
        .orElseGet(() -> {
            Transaction_Type newType = new Transaction_Type();
            newType.setTransactionType("PIX");
            newType.setCode(3);
            return transactionTypeRepository.save(newType);
        });
        transaction.setTransactionType(pixType);
    
        // Define o status da transação como "COMPLETADA"
        Status status = statusRepository.findByCode((short) 1)
                .orElseGet(() -> {
                    Status newStatus = new Status();
                    newStatus.setName("Complete");
                    newStatus.setCode((short) 1);
                    return statusRepository.save(newStatus);
                });
        transaction.setStatus(status);
    
        transactionRepository.save(transaction);
    
        // Envia notificações específicas
        notificationsService.sendNotification("Transferência PIX Enviada",
                "Você enviou R$ " + transferAmount + " via PIX para a chave " + destinationPixKey + ".");
        notificationsService.sendNotification("Transferência PIX Recebida",
                "Você recebeu R$ " + transferAmount + " via PIX da conta " + sourceAccount.getAccount_Number() + ".");
    }
    



    @Transactional
    public boolean depositTest(DepositDTOTest depositDTO) {
        // Converte o número da conta e agência para Integer
        Integer accountNumber = Integer.valueOf(depositDTO.accountNumber());
        Integer agencyNumber = Integer.valueOf(depositDTO.agencyNumber());

        // Busca a conta usando o número da conta e da agência
        Account account = accountRepository.findByAccount_NumberAndAgency_Number(accountNumber, agencyNumber)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        // Verifica se o valor de depósito é positivo
        BigDecimal depositAmount = depositDTO.amount();
        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor de depósito deve ser positivo");
        }

        // Atualiza o saldo da conta
        account.setBalance(account.getBalance().add(depositAmount));
        accountRepository.save(account);

        // Registra a transação na tabela 'transaction'
        Transaction transaction = new Transaction();
        transaction.setAmount(depositAmount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccountOrigin(null);
        transaction.setAccountDestination(account);
        transaction.setDescription("Depósito na conta " + accountNumber);

        // Verifica ou cria o tipo de transação "DEPOSITO"

                Transaction_Type depositType = transactionTypeRepository.findByCode(1)
                        .orElseGet(() -> {
                            Transaction_Type newType = new Transaction_Type();
                            newType.setTransactionType("DEPOSITO"); // Nome correto para depósitos
                            newType.setCode(1);
                            return transactionTypeRepository.save(newType);
                        });
                transaction.setTransactionType(depositType);

        // Verifica ou cria o status "COMPLETADA"
        Status status = statusRepository.findByCode((short) 1)
                .orElseGet(() -> {
                    Status newStatus = new Status();
                    newStatus.setName("Complete");
                    newStatus.setCode((short) 1);
                    return statusRepository.save(newStatus);
                });
        transaction.setStatus(status);

        // Salva a transação de depósito
        transactionRepository.save(transaction);
        notificationsService.sendNotification("Deposito", "Deposito efetuado com sucesso no valor de R$"+depositAmount);

        return true; // Depósito realizado com sucesso
    }


    public List<Transaction> getAccountBankStatement() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca transações onde a conta é origem ou destino
        List<Transaction> transactions = transactionRepository.findByAccountOriginIdOrAccountDestinationId(accountId, accountId);

        if (transactions.isEmpty()) {
            throw new EmptyListException("Nenhum extrato encontrado");
        }

        return transactions;
    }

    @Transactional
    public void debitAmountFromAccount(Long id, double amount) {
        if(amount < 0) throw new IllegalArgumentException("parametro com valor negativo não aceito");

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        BigDecimal accountBalance = account.getBalance();
        BigDecimal decimalAmount = BigDecimal.valueOf(amount);

        if (accountBalance.compareTo(decimalAmount) < 0) {
            throw new InsuficientFundsException("Saldo insuficiente");
        }

        account.setBalance(accountBalance.subtract(decimalAmount));
    }

    @Transactional
    public void creditAmountToAccount(Long id, double amount) {
        if(amount < 0) throw new IllegalArgumentException("parametro com valor negativo não aceito");

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));
    }

    @Transactional
    public void registerTransaction(double amount, String description, short status_code, int transaction_type, Long accountId_origin, Long accountId_destination) {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(description);
        Transaction_Type transactionTypeCode = transactionTypeRepository.findByCode(transaction_type)
                .orElseThrow(() -> new EntityNotFoundException("Código de transação não encontrado"));

        transaction.setTransactionType(transactionTypeCode);

        Status status = statusRepository.findByCode(status_code)
                .orElseThrow(() -> new EntityNotFoundException("Código de status não encontrado"));

        transaction.setStatus(status);

        if (accountId_origin == null) transaction.setAccountOrigin(null);
        else {
            Account accountOrigin = accountRepository.findById(accountId_origin)
                    .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada"));

            transaction.setAccountOrigin(accountOrigin);
        }

        Account accountDestination = accountRepository.findById(accountId_destination)
                .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada"));

        transaction.setAccountDestination(accountDestination);

        transactionRepository.save(transaction);
    }
}
