package com.backend.newbank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.backend.newbank.AccountTransaction.entity.Enum.Person_type;

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Person_type person_Type;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(length = 11)
    private String phone_Number;

    @Column(precision = 10, scale = 2)
    private BigDecimal monthly_Income;

}
