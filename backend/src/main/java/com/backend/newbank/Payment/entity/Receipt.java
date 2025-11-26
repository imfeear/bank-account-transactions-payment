package com.backend.newbank.Payment.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipt")
@Data
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Timestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Timestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Timestamp
    @Column(name = "delete_at")
    private LocalDateTime deleteAt;
}
