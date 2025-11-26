/*
package com.backend.jalabank.Payment.controller;

import com.backend.jalabank.AccountTransaction.repository.AccountRepository;
import com.backend.jalabank.Payment.config.BarCode;
import com.backend.jalabank.Payment.service.PaymentService;
import com.backend.jalabank.securityConfig.SecurityConfig;
import com.backend.jalabank.securityConfig.TokenJWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PaymentController.class)
@Import(SecurityConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private BarCode barcode;

    @MockBean
    private TokenJWTService tokenJWTService;

    @MockBean
    private AccountRepository accountRepository;

    private final String barcodeText = "1234567890123456789012";
    private final String userName = "user1";

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void shouldProcessPaymentSuccessfully() throws Exception {
        doNothing().when(paymentService).processPayment(barcodeText, userName);

        mockMvc.perform(post("/api/payments/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcodeText\": \"" + barcodeText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Payment processed successfully"));

        verify(paymentService, times(1)).processPayment(barcodeText, userName);
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void shouldReturnErrorForInvalidBarcode() throws Exception {
        doThrow(new IllegalArgumentException("Invalid barcode")).when(paymentService).processPayment(barcodeText, userName);

        mockMvc.perform(post("/api/payments/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcodeText\": \"" + barcodeText + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid barcode"));

        verify(paymentService, times(1)).processPayment(barcodeText, userName);
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void shouldReturnErrorForInsufficientBalance() throws Exception {
        doThrow(new RuntimeException("Insufficient balance")).when(paymentService).processPayment(barcodeText, userName);

        mockMvc.perform(post("/api/payments/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcodeText\": \"" + barcodeText + "\"}"))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.error").value("Insufficient balance"));

        verify(paymentService, times(1)).processPayment(barcodeText, userName);
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void shouldReturnErrorForServiceFailure() throws Exception {
        doThrow(new RuntimeException("Service error")).when(paymentService).processPayment(barcodeText, userName);

        mockMvc.perform(post("/api/payments/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcodeText\": \"" + barcodeText + "\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Service error"));

        verify(paymentService, times(1)).processPayment(barcodeText, userName);
    }
}

 */
