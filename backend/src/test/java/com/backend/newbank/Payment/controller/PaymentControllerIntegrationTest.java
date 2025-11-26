package com.backend.newbank.Payment.controller;
/*package com.backend.newbank.Payment.controller;

import com.backend.newbank.Payment.config.BarCode;
import com.backend.newbank.Payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerIntegrationTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private BarCode barcode;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testProcessPayment_Success() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", "validBarcode");

        // Mocking PaymentService success
        doNothing().when(paymentService).processPayment(anyString(), anyString());

        // Mocking Principal
        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"validBarcode\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Payment processed successfully"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }

    @Test
    public void testProcessPayment_InvalidBarcode() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", "invalidBarcode");

        // Mocking an exception on barcode processing
        doThrow(new IllegalArgumentException("Invalid barcode")).when(paymentService).processPayment(anyString(), anyString());

        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"invalidBarcode\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid barcode"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }

    @Test
    public void testProcessPayment_NullBarcode() throws Exception {
        // Passa um valor nulo para o barcodeText
        String requestJson = "{\"barcodeText\": null}";

        // Mocking principal
        Principal principal = () -> "testUser";

        // Realiza a chamada da API e espera um erro de validação com a mensagem apropriada
        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isBadRequest()) // Espera BAD_REQUEST
                .andExpect(jsonPath("$.error").value("Barcode cannot be null or empty")); // Verifica a mensagem de erro

        // Verifica que o método de processPayment não foi chamado
        verify(paymentService, times(0)).processPayment(anyString(), anyString());
    }


    @Test
    public void testHandleRuntimeException_InsufficientBalance() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", "someBarcodeText");

        // Mocking insufficient balance exception
        doThrow(new RuntimeException("Insufficient balance")).when(paymentService).processPayment(anyString(), anyString());

        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"someBarcodeText\"}"))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.error").value("Insufficient balance"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }

    @Test
    public void testProcessPayment_EmptyBarcode() throws Exception {
        // Preparação de uma requisição com o barcode vazio
        String requestJson = "{\"barcodeText\": \"\"}";

        // Mocking principal
        Principal principal = () -> "testUser";

        // Realiza a chamada da API e espera um erro de validação com a mensagem apropriada
        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isBadRequest()) // Espera BAD_REQUEST
                .andExpect(jsonPath("$.error").value("Barcode cannot be null or empty")); // Verifica a mensagem de erro

        // Verifica que o método de processPayment não foi chamado
        verify(paymentService, times(0)).processPayment(anyString(), anyString());
    }


    @Test
    public void testProcessPayment_BarcodeTooLong() throws Exception {
        String longBarcode = "A".repeat(100); // Simulating a barcode that's too long

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", longBarcode);

        // Mocking a Barcode length error
        doThrow(new IllegalArgumentException("Barcode length exceeds maximum allowed")).when(paymentService).processPayment(anyString(), anyString());

        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"" + longBarcode + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Barcode length exceeds maximum allowed"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }

    @Test
    public void testProcessPayment_BarcodeFormatError() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", "badFormatBarcode!123");

        // Mocking a format error for the barcode
        doThrow(new IllegalArgumentException("Invalid barcode format")).when(paymentService).processPayment(anyString(), anyString());

        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"badFormatBarcode!123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid barcode format"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }

    @Test
    public void testProcessPayment_BarcodeNullValue() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("barcodeText", "nullValue");

        // Simulate the barcode processing with an invalid value
        doThrow(new IllegalArgumentException("Value cannot be null")).when(paymentService).processPayment(anyString(), anyString());

        Principal principal = () -> "testUser";

        mockMvc.perform(post("/api/payments/process-payment")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"barcodeText\": \"nullValue\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Value cannot be null"));

        verify(paymentService, times(1)).processPayment(anyString(), anyString());
    }
}


 */