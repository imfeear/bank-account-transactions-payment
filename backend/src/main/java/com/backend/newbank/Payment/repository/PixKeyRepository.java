package com.backend.newbank.Payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.Payment.entity.PixKey;

import java.util.List;
import java.util.Optional;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
    Optional<PixKey> findByKey(String key); // Busca chave PIX pelo email

    List<PixKey> findAllByUserId(Long userId); // Busca todas as chaves PIX associadas ao usuário
}
