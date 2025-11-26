package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.AccountTransaction.entity.Adress;

import java.util.Optional;

public interface AdressRepository extends JpaRepository<Adress, Long> {
    Optional<Adress> findByPerson_id(Long id);
    Adress findByPersonId(Long id);
}
