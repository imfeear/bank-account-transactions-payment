package com.backend.jalabank.AccountTransaction.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer account_Number;

    @Column(nullable = false)
    private Integer agency_Number;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp creation_Date;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
