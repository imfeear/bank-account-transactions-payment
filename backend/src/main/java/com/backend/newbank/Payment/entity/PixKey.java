package com.backend.newbank.Payment.entity;

import jakarta.persistence.*;

@Entity
public class PixKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key; // Email associado à chave PIX

    @Column(nullable = false)
    private Long userId; // Relaciona a chave ao ID do usuário

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
