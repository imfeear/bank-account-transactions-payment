package com.backend.newbank.Payment.entity;
/*
package com.backend.jalabank.Payment.entity;

import com.backend.jalabank.AccountTransaction.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Payment payment;

    @Mock
    private Account account;

    @Mock
    private PaymentMethod paymentMethod;

    @Mock
    private PaymentOrigin paymentOrigin;

    @Mock
    private PaymentStatus paymentStatus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment();
    }

    @Test
    public void testSetAndGetId() {
        payment.setId(1L);
        assertEquals(1L, payment.getId());
    }

    @Test
    public void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("1000.50");
        payment.setValue(value);
        assertEquals(value, payment.getValue());
    }

    @Test
    public void testSetAndGetAccount() {
        payment.setAccount(account);
        assertEquals(account, payment.getAccount());
    }

    @Test
    public void testSetAndGetCode() {
        String code = "123456789";
        payment.setCode(code);
        assertEquals(code, payment.getCode());
    }

    @Test
    public void testSetAndGetPaymentMethod() {
        payment.setPaymentMethodId(paymentMethod);
        assertEquals(paymentMethod, payment.getPaymentMethodId());
    }
    /*
        @Test
       public void testSetAndGetPaymentOrigin() {
            payment.setPaymentOriginId(paymentOrigin);
            assertEquals(paymentOrigin, payment.getPaymentOriginId());
        }

    @Test
    public void testSetAndGetPaymentStatus() {
        payment.setPaymentStatusId(paymentStatus);
        assertEquals(paymentStatus, payment.getPaymentStatusId());
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        payment.setCreatedAt(now);
        assertEquals(now, payment.getCreatedAt());
    }

    @Test
    public void testSetAndGetUpdateAt() {
        LocalDateTime now = LocalDateTime.now();
        payment.setUpdateAt(now);
        assertEquals(now, payment.getUpdateAt());
    }

    @Test
    public void testSetAndGetDeleteAt() {
        LocalDateTime now = LocalDateTime.now();
        payment.setDeleteAt(now);
        assertEquals(now, payment.getDeleteAt());
    }

    @Test
    public void testConstructorWithFields() {
        LocalDateTime now = LocalDateTime.now();
        Payment paymentWithValues = new Payment(1L, new BigDecimal("1500.75"), account, "123456", paymentMethod, paymentOrigin, paymentStatus, now, now, now);

        assertAll("Testing constructor with fields",
                () -> assertEquals(1L, paymentWithValues.getId()),
                () -> assertEquals(new BigDecimal("1500.75"), paymentWithValues.getValue()),
                () -> assertEquals(account, paymentWithValues.getAccount()),
                () -> assertEquals("123456", paymentWithValues.getCode()),
                () -> assertEquals(paymentMethod, paymentWithValues.getPaymentMethodId()),
                () -> assertEquals(paymentOrigin, paymentWithValues.getPaymentOriginId()),
                () -> assertEquals(paymentStatus, paymentWithValues.getPaymentStatusId()),
                () -> assertEquals(now, paymentWithValues.getCreatedAt()),
                () -> assertEquals(now, paymentWithValues.getUpdateAt()),
                () -> assertEquals(now, paymentWithValues.getDeleteAt())
        );
    }

    @Test
    public void testDefaultConstructor() {
        Payment defaultPayment = new Payment();
        assertNull(defaultPayment.getId());
        assertNull(defaultPayment.getValue());
        assertNull(defaultPayment.getAccount());
        assertNull(defaultPayment.getCode());
        assertNull(defaultPayment.getPaymentMethodId());
        assertNull(defaultPayment.getPaymentOriginId());
        assertNull(defaultPayment.getPaymentStatusId());
        assertNull(defaultPayment.getCreatedAt());
        assertNull(defaultPayment.getUpdateAt());
        assertNull(defaultPayment.getDeleteAt());
    }

}
        */

