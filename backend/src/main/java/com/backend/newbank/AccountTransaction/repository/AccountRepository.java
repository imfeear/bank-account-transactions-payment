package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.newbank.AccountTransaction.entity.Account;

import java.sql.Timestamp;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.account_Number = :accountNumber AND a.agency_Number = :agencyNumber")
    Optional<Account> findByAccount_NumberAndAgency_Number(@Param("accountNumber") Integer accountNumber, @Param("agencyNumber") Integer agencyNumber);
    Optional<Account> findByUser_Id(Long userId);

    @Query("SELECT a.creation_Date FROM Account a WHERE a.id = :accountId")
    Timestamp getCreationDate(@Param("accountId") Long accountId);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.account_Number = :accountNumber")
    boolean existsByAccountNumber(@Param("accountNumber") int accountNumber);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.agency_Number = :agencyNumber")
    boolean existsByAgencyNumber(@Param("agencyNumber") int agencyNumber);

    @Modifying
    @Query("UPDATE Account a SET a.isActive = false WHERE a.id = :accountId")
    void closeAccount(@Param("accountId") Long accountId);

}
