package com.backend.jalabank.Payment.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR", length = 20)
    private String status;

    @Timestamp
    private LocalDateTime createdAt;

    @Timestamp
    private LocalDateTime updateAt;

    @Timestamp
    private LocalDateTime deleteAt;
}
