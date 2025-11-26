package com.backend.jalabank.Payment.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import com.backend.jalabank.AccountTransaction.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(columnDefinition = "VARCHAR")
    private String code;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethodId;

    @ManyToOne
    @JoinColumn(name = "payment_origin_id", nullable = false)
    private PaymentOrigin paymentOriginId;

    @ManyToOne
    @JoinColumn(name = "payment_status_id", nullable = false)
    private PaymentStatus paymentStatusId;

    @Timestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Timestamp
    @Column(name = "update_at")
    private LocalDate updateAt;

    @Timestamp
    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

}
