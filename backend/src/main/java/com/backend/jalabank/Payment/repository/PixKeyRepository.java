package com.backend.jalabank.Payment.repository;

import com.backend.jalabank.Payment.entity.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
    Optional<PixKey> findByKey(String key); // Busca chave PIX pelo email

    List<PixKey> findAllByUserId(Long userId); // Busca todas as chaves PIX associadas ao usuário
}
