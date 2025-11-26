package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Adress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdressRepository extends JpaRepository<Adress, Long> {
    Optional<Adress> findByPerson_id(Long id);
    Adress findByPersonId(Long id);
}
