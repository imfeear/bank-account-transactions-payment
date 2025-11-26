package com.backend.newbank.Payment.entity;
/*
package com.backend.jalabank.Payment.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReceiptTest {

    private Receipt receipt;
    private Payment payment;

    @BeforeEach
    public void setUp() {
        receipt = new Receipt();
        payment = mock(Payment.class); // Utilizando um mock para a classe Payment
    }

    @Test
    public void testSetAndGetId() {
        receipt.setId(1L);
        assertEquals(1L, receipt.getId());
    }

    @Test
    public void testSetAndGetPayment() {
        receipt.setPayment(payment);
        assertEquals(payment, receipt.getPayment());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        receipt.setCreatedAt(now);
        assertEquals(now, receipt.getCreatedAt());
    }

    @Test
    public void testSetAndGetUpdateAt() {
        LocalDateTime now = LocalDateTime.now();
        receipt.setUpdateAt(now);
        assertEquals(now, receipt.getUpdateAt());
    }

    @Test
    public void testSetAndGetDeleteAt() {
        LocalDateTime now = LocalDateTime.now();
        receipt.setDeleteAt(now);
        assertEquals(now, receipt.getDeleteAt());
    }

    @Test
    public void testConstructorWithFields() {
        LocalDateTime now = LocalDateTime.now();
        Receipt receiptWithValues = new Receipt();
        receiptWithValues.setId(1L);
        receiptWithValues.setPayment(payment);
        receiptWithValues.setCreatedAt(now);
        receiptWithValues.setUpdateAt(now);
        receiptWithValues.setDeleteAt(now);

        assertAll("Testing constructor with fields",
                () -> assertEquals(1L, receiptWithValues.getId()),
                () -> assertEquals(payment, receiptWithValues.getPayment()),
                () -> assertEquals(now, receiptWithValues.getCreatedAt()),
                () -> assertEquals(now, receiptWithValues.getUpdateAt()),
                () -> assertEquals(now, receiptWithValues.getDeleteAt())
        );
    }

    @Test
    public void testDefaultConstructor() {
        Receipt defaultReceipt = new Receipt();
        assertNull(defaultReceipt.getId());
        assertNull(defaultReceipt.getPayment());
        assertNull(defaultReceipt.getCreatedAt());
        assertNull(defaultReceipt.getUpdateAt());
        assertNull(defaultReceipt.getDeleteAt());
    }

    @Test
    public void testEqualityAndHashCode() {
        Receipt receipt1 = new Receipt();
        Receipt receipt2 = new Receipt();

        receipt1.setId(1L);
        receipt2.setId(1L);

        assertEquals(receipt1, receipt2);
        assertEquals(receipt1.hashCode(), receipt2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        Receipt receipt = new Receipt();
        receipt.setId(1L);
        receipt.setPayment(payment);
        receipt.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));

        String expectedString = "Receipt(id=1, payment=" + payment +
                ", createdAt=2024-01-01T10:00, updateAt=null, deleteAt=null)";
        assertEquals(expectedString, receipt.toString());
    }
}

 */
