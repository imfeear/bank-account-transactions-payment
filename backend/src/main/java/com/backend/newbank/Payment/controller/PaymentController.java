package com.backend.newbank.Payment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.newbank.Payment.DTO.BarcodeDTO;
import com.backend.newbank.Payment.DTO.PaymentDTO;
import com.backend.newbank.Payment.entity.Payment;
import com.backend.newbank.Payment.repository.PixKeyRepository;
import com.backend.newbank.Payment.service.PaymentService;
import com.backend.newbank.Payment.util.BarcodeGenerator;
import com.backend.newbank.Payment.util.FillData;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService, PixKeyRepository pixKeyRepository) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process-payment")
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestBody Map<String, String> requestMap) {

        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> response = new HashMap<>();


        String barcodeText = requestMap.get("barcodeText");
        String dateScheduledPay = requestMap.get("datePayment");


        // Adiciona a validação para barcodeText nulo ou vazio
        if (barcodeText == null) {
            throw new IllegalArgumentException("Barcode cannot be null or empty");
        }

        if (!dateScheduledPay.equalsIgnoreCase("Hoje")){
            Map<String, Object> data = new HashMap<>();
            Payment payment = paymentService.processScheduledPay(barcodeText, accountId, dateScheduledPay);

            data.put("value", payment.getValue());
            data.put("code", payment.getCode());
            data.put("date", payment.getCreatedAt());
            data.put("paid to", payment.getAccount().getAccount_Number()+"-"+payment.getAccount().getAgency_Number());

            response.put("status", "Payment processed successfully");
            response.put("receipt", data);
            return ResponseEntity.ok(response);
        }
        else {
            Map<String, Object> data = new HashMap<>();

            Payment payment = paymentService.processPayment(barcodeText, accountId);
            data.put("value", payment.getValue());
            data.put("code", payment.getCode());
            data.put("date", payment.getCreatedAt());
            data.put("paid to", payment.getAccount().getAccount_Number()+"-"+payment.getAccount().getAgency_Number());

            response.put("status", "Payment processed successfully");
            response.put("receipt", data);
            return ResponseEntity.ok(response);
        }
    }
    

    @GetMapping("/confirm-payment")
    public ResponseEntity <Map<String, Object>> confirmPayment (@RequestBody Map <String, String> requestMap){
        PaymentDTO paymentData;
        Map<String, Object> paymentInfo = new HashMap<>();

        String barcodeText = requestMap.get("barcodeText");
        paymentData = FillData.fillData(barcodeText);
        LocalDateTime payDate = LocalDateTime.now();

        paymentInfo.put("account-number", paymentData.getAccountNumber()+"-"+paymentData.getAgencyNumber());
        paymentInfo.put("value", paymentData.getValue());
        paymentInfo.put("code", paymentData.getCode());
        paymentInfo.put("date",payDate);

        return ResponseEntity.ok(paymentInfo);
    }

    @GetMapping("/history")
    public ResponseEntity <List<PaymentDTO>> historyPayments(){

        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List <PaymentDTO> history = paymentService.findAll(accountId);

        return ResponseEntity.ok(history);
    }

    @PostMapping("/generate-barcode")
    public ResponseEntity<Map<String, String>> generateBarcode(BarcodeDTO body) {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> response = new HashMap<>();
        String barcode = BarcodeGenerator.generateBarcode(body.accountNumber(), body.agencyNumber(), body.value(), 3);
        response.put("barcode", barcode);
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBarcodeException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        String message = ex.getMessage();

        // Adiciona a mensagem de erro para o caso de saldo insuficiente
        if ("Insufficient balance".equals(message)) {
            errorResponse.put("error", message);
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(errorResponse);
        }

        errorResponse.put("error", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
