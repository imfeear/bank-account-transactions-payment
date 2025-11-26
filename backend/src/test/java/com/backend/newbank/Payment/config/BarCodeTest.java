package com.backend.newbank.Payment.config;
/*
package com.backend.jalabank.Payment.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class BarCodeTest {

    private BarCode barCode;

    @BeforeEach
    public void setUp() {
        // Inicializa a instância da classe BarCode
        barCode = new BarCode();
    }

    @Test
    public void shouldGenerateBarcodeWith22Characters() {
        // Arrange
        Long paymentId = 12345L;
        BigDecimal value = BigDecimal.valueOf(100.50);

        // Act
        String barcode = barCode.generateBarcode(paymentId, value);

        // Assert
        assertNotNull(barcode, "O código de barras não pode ser nulo.");
        assertEquals(22, barcode.length(), "O código de barras deve ter 22 caracteres.");
        assertTrue(barcode.matches("\\d{22}"), "O código de barras deve conter apenas números.");
    }

    @Test
    public void shouldGenerateBarcodeWithZeroValues() {
        // Arrange
        Long paymentId = 0L;
        BigDecimal value = BigDecimal.ZERO;

        // Act
        String barcode = barCode.generateBarcode(paymentId, value);

        // Assert
        assertNotNull(barcode, "O código de barras não pode ser nulo.");
        assertEquals(22, barcode.length(), "O código de barras deve ter 22 caracteres.");
        assertTrue(barcode.matches("\\d{22}"), "O código de barras deve conter apenas números.");
    }

    @Test
    public void shouldGenerateBarcodeWithLargeValues() {
        // Arrange
        Long paymentId = 1234567890L;  // valor dentro de um intervalo razoável
        BigDecimal value = BigDecimal.valueOf(99999999);  // valor dentro de um intervalo razoável

        // Act
        String barcode = barCode.generateBarcode(paymentId, value);

        // Assert
        assertNotNull(barcode, "O código de barras não pode ser nulo.");
        assertEquals(22, barcode.length(), "O código de barras deve ter 22 caracteres.");
        assertTrue(barcode.matches("\\d{22}"), "O código de barras deve conter apenas números.");
    }

    @Test
    public void shouldHandleNullPaymentId() {
        // Arrange
        Long paymentId = null;
        BigDecimal value = BigDecimal.valueOf(100.50);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            barCode.generateBarcode(paymentId, value);
        }, "Deveria lançar IllegalArgumentException para paymentId nulo.");
    }


    @Test
    public void shouldHandleNullValue() {
        // Arrange
        Long paymentId = 12345L;
        BigDecimal value = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            barCode.generateBarcode(paymentId, value);
        }, "Deveria lançar IllegalArgumentException para value nulo.");
    }

}

 */
