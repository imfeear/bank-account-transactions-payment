package com.backend.jalabank.AccountTransaction.entity;

import com.backend.jalabank.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_type", nullable = false)
    private Transaction_Type transactionType;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime transactionDate;

    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_origin_id", nullable = true)
    private Account accountOrigin;

    @ManyToOne
    @JoinColumn(name = "account_destination_id", nullable = false)
    private Account accountDestination;

    @ManyToOne
    @JoinColumn(name = "code", nullable = false)
    private Status status;
}
