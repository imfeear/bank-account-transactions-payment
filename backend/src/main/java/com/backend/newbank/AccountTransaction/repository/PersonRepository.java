package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.newbank.AccountTransaction.entity.Person;

import java.math.BigDecimal;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p.monthly_Income FROM Person p WHERE p.id = :personId")
    BigDecimal getMontly_income(@Param("personId") Long personId);

    Optional<Person> findByEmail(String email);
}
