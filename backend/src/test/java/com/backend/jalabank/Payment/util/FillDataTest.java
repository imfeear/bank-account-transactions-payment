/*
package com.backend.jalabank.Payment.util;

import com.backend.jalabank.Payment.DTO.PaymentDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FillDataTest {

    private static final String VALID_BARCODE = "12345|678|100.50";
    private static final String INVALID_BARCODE_FORMAT = "12345|678"; // Dados incompletos
    private static final String NON_NUMERIC_BARCODE = "abc|678|100.50"; // Texto onde deveria haver número

    @Test
    void testFillDataWithValidBarcode() {
        try (MockedStatic<DecodeBarcode> mocked = Mockito.mockStatic(DecodeBarcode.class)) {
            mocked.when(() -> DecodeBarcode.fromAsciiCode(VALID_BARCODE)).thenReturn(VALID_BARCODE);

            PaymentDTO payment = FillData.fillData(VALID_BARCODE);

            assertNotNull(payment, "O objeto PaymentDTO não deve ser nulo.");
            assertEquals(12345, payment.getAccountNumber(), "Account number incorreto.");
            assertEquals(678, payment.getAgencyNumber(), "Agency number incorreto.");
            assertEquals(new BigDecimal("100.50"), payment.getValue(), "Valor incorreto.");
        }
    }

    @Test
    void testFillDataWithInvalidBarcodeFormat() {
        try (MockedStatic<DecodeBarcode> mocked = Mockito.mockStatic(DecodeBarcode.class)) {
            mocked.when(() -> DecodeBarcode.fromAsciiCode(INVALID_BARCODE_FORMAT)).thenReturn(INVALID_BARCODE_FORMAT);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                FillData.fillData(INVALID_BARCODE_FORMAT);
            });

            assertEquals("Invalid barcode format", exception.getMessage(), "Mensagem de erro incorreta para formato inválido.");
        }
    }

    @Test
    void testFillDataWithNonNumericBarcode() {
        try (MockedStatic<DecodeBarcode> mocked = Mockito.mockStatic(DecodeBarcode.class)) {
            mocked.when(() -> DecodeBarcode.fromAsciiCode(NON_NUMERIC_BARCODE)).thenReturn(NON_NUMERIC_BARCODE);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                FillData.fillData(NON_NUMERIC_BARCODE);
            });

            assertTrue(exception.getMessage().contains("For input string"), "Esperado erro de formato de número.");
        }
    }

    @Test
    void testFillDataWithNegativeValue() {
        String negativeValueBarcode = "12345|678|-50.00";
        try (MockedStatic<DecodeBarcode> mocked = Mockito.mockStatic(DecodeBarcode.class)) {
            mocked.when(() -> DecodeBarcode.fromAsciiCode(negativeValueBarcode)).thenReturn(negativeValueBarcode);

            PaymentDTO payment = FillData.fillData(negativeValueBarcode);

            assertEquals(new BigDecimal("-50.00"), payment.getValue(), "Valor negativo não foi processado corretamente.");
        }
    }

    @Test
    void testFillDataWithLargeNumbers() {
        String largeNumbersBarcode = "999999999|888888888|1000000000.00";
        try (MockedStatic<DecodeBarcode> mocked = Mockito.mockStatic(DecodeBarcode.class)) {
            mocked.when(() -> DecodeBarcode.fromAsciiCode(largeNumbersBarcode)).thenReturn(largeNumbersBarcode);

            PaymentDTO payment = FillData.fillData(largeNumbersBarcode);

            assertEquals(999999999, payment.getAccountNumber(), "Número de conta incorreto para números grandes.");
            assertEquals(888888888, payment.getAgencyNumber(), "Número de agência incorreto para números grandes.");
            assertEquals(new BigDecimal("1000000000.00"), payment.getValue(), "Valor incorreto para números grandes.");
        }
    }
}

 */
