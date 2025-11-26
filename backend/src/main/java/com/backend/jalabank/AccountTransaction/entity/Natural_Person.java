package com.backend.jalabank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "natural_person")
public class Natural_Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String full_Name;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false)
    private Date born_Date;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
