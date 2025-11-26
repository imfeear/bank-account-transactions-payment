package com.backend.newbank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "legal_entity")
public class Legal_Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String razao_Social;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Column(nullable = false, length = 200)
    private String responsible;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
