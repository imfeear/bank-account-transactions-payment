package com.backend.newbank.Payment.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameService;

    @Timestamp
    private LocalDateTime createdAt;

    @Timestamp
    private LocalDateTime updateAt;

    @Timestamp
    private LocalDateTime deleteAt;
}
