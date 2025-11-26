package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Legal_Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LegalEntityRespository extends JpaRepository<Legal_Entity, Long> {
    Optional<Legal_Entity> findByCnpj(String cnpj);
    Optional<Legal_Entity> findByPersonId(Long personId);
}
