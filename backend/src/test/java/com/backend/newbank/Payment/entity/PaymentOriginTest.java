package com.backend.newbank.Payment.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.backend.newbank.Payment.entity.PaymentOrigin;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentOriginTest {

    private PaymentOrigin paymentOrigin;

    @BeforeEach
    public void setUp() {
        paymentOrigin = new PaymentOrigin();
    }

    @Test
    public void testSetAndGetId() {
        paymentOrigin.setId(1);
        assertEquals(1, paymentOrigin.getId());
    }

    @Test
    public void testSetAndGetNameService() {
        String nameService = "Service Name";
        paymentOrigin.setNameService(nameService);
        assertEquals(nameService, paymentOrigin.getNameService());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentOrigin.setCreatedAt(now);
        assertEquals(now, paymentOrigin.getCreatedAt());
    }

    @Test
    public void testSetAndGetUpdateAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentOrigin.setUpdateAt(now);
        assertEquals(now, paymentOrigin.getUpdateAt());
    }

    @Test
    public void testSetAndGetDeleteAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentOrigin.setDeleteAt(now);
        assertEquals(now, paymentOrigin.getDeleteAt());
    }

    @Test
    public void testConstructorWithFields() {
        LocalDateTime now = LocalDateTime.now();
        PaymentOrigin paymentOriginWithValues = new PaymentOrigin(1, "Service Name", now, now, now);

        assertAll("Testing constructor with fields",
                () -> assertEquals(1, paymentOriginWithValues.getId()),
                () -> assertEquals("Service Name", paymentOriginWithValues.getNameService()),
                () -> assertEquals(now, paymentOriginWithValues.getCreatedAt()),
                () -> assertEquals(now, paymentOriginWithValues.getUpdateAt()),
                () -> assertEquals(now, paymentOriginWithValues.getDeleteAt())
        );
    }

    @Test
    public void testDefaultConstructor() {
        PaymentOrigin defaultPaymentOrigin = new PaymentOrigin();
        assertNull(defaultPaymentOrigin.getId());
        assertNull(defaultPaymentOrigin.getNameService());
        assertNull(defaultPaymentOrigin.getCreatedAt());
        assertNull(defaultPaymentOrigin.getUpdateAt());
        assertNull(defaultPaymentOrigin.getDeleteAt());
    }
}
