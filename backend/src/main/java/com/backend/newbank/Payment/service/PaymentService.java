package com.backend.jalabank.Payment.service;

import com.backend.jalabank.AccountTransaction.entity.Account;
import com.backend.jalabank.AccountTransaction.repository.AccountRepository;
import com.backend.jalabank.AccountTransaction.service.TransactionService;
import com.backend.jalabank.Payment.DTO.PaymentDTO;
import com.backend.jalabank.Payment.entity.Payment;
import com.backend.jalabank.Payment.repository.PaymentRepository;
import com.backend.jalabank.Payment.util.FillData;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentOriginService originService;

    @Autowired
    private PaymentMethodService methodService;

    @Autowired
    private PaymentStatusService statusService;

    @Autowired
    private TransactionService transactionService;



    public Payment processPayment(String barcodeText, Long accountId) {
        // Decodifica o código de barras para obter os dados
        PaymentDTO paymentData = FillData.fillData(barcodeText);
        Payment payment = new Payment();

        //100
        // Verifica a conta do remetente com base no usuário logado
        Account senderAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));


        // Busca a conta do destinatário com os dados decodificados
        Account recipientAccount = accountRepository.findByAccount_NumberAndAgency_Number(
                        paymentData.getAccountNumber(), paymentData.getAgencyNumber())
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (recipientAccount.equals(senderAccount)){
            throw new RuntimeException("sender's account and recipient's account are the same");
        }

        Payment paymentResponse = transactionBillPayment(senderAccount, recipientAccount, paymentData, payment);

        return paymentResponse;
    }

    public Payment transactionBillPayment (Account senderAccount, Account recipientAccount, PaymentDTO paymentData, Payment payment){


        if (senderAccount.getBalance().compareTo(paymentData.getValue()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        else{
            payment.setPaymentStatusId(statusService.findById(1));
        }

        // Registra o pagamento
        payment.setValue(paymentData.getValue());
        payment.setAccount(senderAccount);
        payment.setCode(paymentData.getCode());
        payment.setPaymentOriginId(originService.findById(paymentData.getOriginId()));
        payment.setPaymentMethodId(methodService.findById(2));
        payment.setCreatedAt(LocalDateTime.now());


        // Realiza a transação
        senderAccount.setBalance(senderAccount.getBalance().subtract(paymentData.getValue()));
        recipientAccount.setBalance(recipientAccount.getBalance().add(paymentData.getValue()));

        // Salva as alterações nas contas
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);


        paymentRepository.save(payment);


        transactionService.registerTransaction(
                paymentData.getValue().doubleValue(),
                "Bill Payment",
                (short) 1, (short) 3,
                senderAccount.getId(),
                recipientAccount.getId());

        return payment;
    }

    public List<PaymentDTO> findAll(Long accountId){
        List <Payment> listPayments = paymentRepository.findAll();
        List <PaymentDTO> listFiltered = new ArrayList<>();
        PaymentDTO showPay;


        for (Payment payObj : listPayments){
            Long idPay = payObj.getAccount().getId();

            if (!idPay.equals(accountId)){
                String code= payObj.getCode();
                showPay = FillData.fillData(code);
                listFiltered.add(showPay);
            }
        }

    return listFiltered;
    }

    public void payment (Long idSender, Long idRecipient, Double value, String description){
        Account account = accountRepository.findById(idSender)
                .orElseThrow(() -> new EntityNotFoundException("Account not found !"));

        transactionService.debitAmountFromAccount(idSender, value);
        transactionService.creditAmountToAccount(idRecipient, value);

        transactionService.registerTransaction(value, description,(short) 1, (short) 3, idSender, idRecipient);
    }

    public Payment processScheduledPay (String barcodeText, Long accountId, String dateScheduledPay){

        PaymentDTO paymentData = FillData.fillData(barcodeText);
        Payment payment = new Payment();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate datePay = LocalDate.parse(dateScheduledPay, formatter);


        // Verifica a conta do remetente com base no usuário logado
        Account senderAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));


        // Busca a conta do destinatário com os dados decodificados
        Account recipientAccount = accountRepository.findByAccount_NumberAndAgency_Number(
                        paymentData.getAccountNumber(), paymentData.getAgencyNumber())
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (senderAccount.getBalance().compareTo(paymentData.getValue()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        else if (recipientAccount.equals(senderAccount)){
            throw new RuntimeException("sender's account and recipient's account are the same");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(paymentData.getValue()));

        accountRepository.save(senderAccount);

        payment.setValue(paymentData.getValue());
        payment.setAccount(senderAccount);
        payment.setCode(paymentData.getCode());
        payment.setPaymentMethodId(methodService.findById(2));
        payment.setPaymentOriginId(originService.findById(paymentData.getOriginId()));
        payment.setPaymentStatusId(statusService.findById(2));
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdateAt(datePay);

        paymentRepository.save(payment);

        return payment;
    }
    @Scheduled(cron = "0 * * * * ?")
    public void ScheduledPay (){
        List <Payment> pendingList = paymentRepository.findPendingPayment(2);

        for (Payment paymentObj : pendingList ){
            LocalDate datePayment = paymentObj.getUpdateAt();


            if (datePayment.equals(LocalDate.now())){
                String code = paymentObj.getCode();

                PaymentDTO data = FillData.fillData(code);

                Account accountId = paymentObj.getAccount();

                Account recipientAccount = accountRepository.findByAccount_NumberAndAgency_Number(data.getAccountNumber(), data.getAgencyNumber())
                        .orElseThrow(() -> new RuntimeException("Account Not Found"));

                recipientAccount.setBalance(recipientAccount.getBalance().add(data.getValue()));
                accountRepository.save(recipientAccount);

                System.out.println("Cheguei aqui");
                paymentObj.setPaymentStatusId(statusService.findById(1));
                paymentRepository.save(paymentObj);

                transactionService.registerTransaction(
                        data.getValue().doubleValue(),
                        "Bill Payment",
                        (short) 1, (short) 3, accountId.getId(),
                        recipientAccount.getId());
            }
        }
    }

}
