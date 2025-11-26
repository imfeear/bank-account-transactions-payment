package com.backend.jalabank.Payment.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.backend.newbank.Payment.DTO.PaymentDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDTOTest {

    private PaymentDTO paymentDTO;

    @BeforeEach
    public void setUp() {
        paymentDTO = new PaymentDTO();
    }

    @Test
    public void testSetAndGetAccountNumber() {
        // Verificando o setter e getter do campo accountNumber
        paymentDTO.setAccountNumber(12345);
        assertEquals(12345, paymentDTO.getAccountNumber());
    }

    @Test
    public void testSetAndGetAgencyNumber() {
        // Verificando o setter e getter do campo agencyNumber
        paymentDTO.setAgencyNumber(678);
        assertEquals(678, paymentDTO.getAgencyNumber());
    }

    @Test
    public void testSetAndGetValue() {
        // Verificando o setter e getter do campo value
        BigDecimal value = new BigDecimal("1500.75");
        paymentDTO.setValue(value);
        assertEquals(value, paymentDTO.getValue());
    }

    @Test
    public void testSetAndGetCode() {
        // Verificando o setter e getter do campo code
        String code = "12345678901234567890";
        paymentDTO.setCode(code);
        assertEquals(code, paymentDTO.getCode());
    }

    @Test
    public void testPaymentDTOConstructorAndGetters() {
        // Verificando a instância da classe com um construtor e os valores de retorno
        PaymentDTO dto = new PaymentDTO();
        dto.setAccountNumber(98765);
        dto.setAgencyNumber(4321);
        dto.setValue(new BigDecimal("999.99"));
        dto.setCode("0987654321");

        assertAll("Testing getters",
                () -> assertEquals(98765, dto.getAccountNumber()),
                () -> assertEquals(4321, dto.getAgencyNumber()),
                () -> assertEquals(new BigDecimal("999.99"), dto.getValue()),
                () -> assertEquals("0987654321", dto.getCode())
        );
    }

    // Teste adicional para garantir que a classe tem um comportamento esperado no caso de valores nulos ou inválidos
    @Test
    public void testSetValueNull() {
        // Verificando o comportamento caso o valor seja nulo
        paymentDTO.setValue(null);
        assertNull(paymentDTO.getValue());
    }

    @Test
    public void testSetCodeNull() {
        // Verificando o comportamento caso o código seja nulo
        paymentDTO.setCode(null);
        assertNull(paymentDTO.getCode());
    }
}
