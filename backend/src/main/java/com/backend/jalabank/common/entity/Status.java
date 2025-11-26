package com.backend.jalabank.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Short code;

    public Status(Short code,String name) {
        this.name = name;
        this.code = code;
    }

    public Status() {

    }
}


