package com.backend.jalabank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adress")
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String street;

    @Column(nullable = false, length = 200)
    private String neighborhood;

    @Column(nullable = false, length = 8)
    private String cep;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(nullable = false, length = 150)
    private String state;

    @Column(nullable = false, length = 150)
    private String city;

    @Column(length = 150)
    private String complement;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
