package com.backend.jalabank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String password;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

}