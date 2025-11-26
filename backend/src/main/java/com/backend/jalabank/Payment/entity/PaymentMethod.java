package com.backend.jalabank.Payment.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "payment_method")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "method", length = 20, nullable = false)
    private String method;

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
