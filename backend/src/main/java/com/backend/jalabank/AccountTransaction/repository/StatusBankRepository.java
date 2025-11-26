package com.backend.jalabank.AccountTransaction.repository;


import com.backend.jalabank.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusBankRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
    Optional<Status> findByCode(Short code);
}

