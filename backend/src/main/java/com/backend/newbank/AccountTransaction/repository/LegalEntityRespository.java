package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.AccountTransaction.entity.Legal_Entity;

import java.util.Optional;

public interface LegalEntityRespository extends JpaRepository<Legal_Entity, Long> {
    Optional<Legal_Entity> findByCnpj(String cnpj);
    Optional<Legal_Entity> findByPersonId(Long personId);
}
