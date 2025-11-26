/*
package com.backend.jalabank.Payment.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentStatusTest {

    private PaymentStatus paymentStatus;

    @BeforeEach
    public void setUp() {
        paymentStatus = new PaymentStatus();
    }

    @Test
    public void testSetAndGetId() {
        paymentStatus.setId(1);
        assertEquals(1, paymentStatus.getId());
    }

    @Test
    public void testSetAndGetStatus() {
        String status = "COMPLETED";
        paymentStatus.setStatus(status);
        assertEquals(status, paymentStatus.getStatus());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentStatus.setCreatedAt(now);
        assertEquals(now, paymentStatus.getCreatedAt());
    }

    @Test
    public void testSetAndGetUpdateAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentStatus.setUpdateAt(now);
        assertEquals(now, paymentStatus.getUpdateAt());
    }

    @Test
    public void testSetAndGetDeleteAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentStatus.setDeleteAt(now);
        assertEquals(now, paymentStatus.getDeleteAt());
    }

    @Test
    public void testConstructorWithFields() {
        LocalDateTime now = LocalDateTime.now();
        PaymentStatus paymentStatusWithValues = new PaymentStatus(1, "COMPLETED", now, now, now);

        assertAll("Testing constructor with fields",
                () -> assertEquals(1, paymentStatusWithValues.getId()),
                () -> assertEquals("COMPLETED", paymentStatusWithValues.getStatus()),
                () -> assertEquals(now, paymentStatusWithValues.getCreatedAt()),
                () -> assertEquals(now, paymentStatusWithValues.getUpdateAt()),
                () -> assertEquals(now, paymentStatusWithValues.getDeleteAt())
        );
    }

    @Test
    public void testDefaultConstructor() {
        PaymentStatus defaultPaymentStatus = new PaymentStatus();
        assertNull(defaultPaymentStatus.getId());
        assertNull(defaultPaymentStatus.getStatus());
        assertNull(defaultPaymentStatus.getCreatedAt());
        assertNull(defaultPaymentStatus.getUpdateAt());
        assertNull(defaultPaymentStatus.getDeleteAt());
    }
}

 */
