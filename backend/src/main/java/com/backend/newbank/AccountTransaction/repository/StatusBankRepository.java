package com.backend.newbank.AccountTransaction.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.common.entity.Status;

import java.util.Optional;

public interface StatusBankRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
    Optional<Status> findByCode(Short code);
}

