package com.backend.jalabank.AccountTransaction.entity;

import com.backend.jalabank.AccountTransaction.entity.Enum.Person_type;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

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
