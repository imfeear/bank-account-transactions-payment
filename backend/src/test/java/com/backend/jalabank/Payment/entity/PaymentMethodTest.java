/*
package com.backend.jalabank.Payment.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodTest {

    private PaymentMethod paymentMethod;

    @BeforeEach
    public void setUp() {
        paymentMethod = new PaymentMethod();
    }

    @Test
    public void testSetAndGetId() {
        paymentMethod.setId(1);
        assertEquals(1, paymentMethod.getId());
    }

    @Test
    public void testSetAndGetMethod() {
        String method = "Credit Card";
        paymentMethod.setMethod(method);
        assertEquals(method, paymentMethod.getMethod());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentMethod.setCreatedAt(now);
        assertEquals(now, paymentMethod.getCreatedAt());
    }

    @Test
    public void testSetAndGetUpdateAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentMethod.setUpdateAt(now);
        assertEquals(now, paymentMethod.getUpdateAt());
    }

    @Test
    public void testSetAndGetDeleteAt() {
        LocalDateTime now = LocalDateTime.now();
        paymentMethod.setDeleteAt(now);
        assertEquals(now, paymentMethod.getDeleteAt());
    }

    @Test
    public void testConstructorWithFields() {
        LocalDateTime now = LocalDateTime.now();
        PaymentMethod paymentMethodWithValues = new PaymentMethod(1, "Credit Card", now, now, now);

        assertAll("Testing constructor with fields",
                () -> assertEquals(1, paymentMethodWithValues.getId()),
                () -> assertEquals("Credit Card", paymentMethodWithValues.getMethod()),
                () -> assertEquals(now, paymentMethodWithValues.getCreatedAt()),
                () -> assertEquals(now, paymentMethodWithValues.getUpdateAt()),
                () -> assertEquals(now, paymentMethodWithValues.getDeleteAt())
        );
    }

    @Test
    public void testDefaultConstructor() {
        PaymentMethod defaultPaymentMethod = new PaymentMethod();
        assertNull(defaultPaymentMethod.getId());
        assertNull(defaultPaymentMethod.getMethod());
        assertNull(defaultPaymentMethod.getCreatedAt());
        assertNull(defaultPaymentMethod.getUpdateAt());
        assertNull(defaultPaymentMethod.getDeleteAt());
    }
}

 */
