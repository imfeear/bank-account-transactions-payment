package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Natural_Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface NaturalPersonRepository extends JpaRepository<Natural_Person, Long> {

    Optional<Natural_Person> findByCpf(String cpf);
    Optional<Natural_Person> findByPersonId(Long personId);

    @Query("SELECT np.born_Date FROM Natural_Person np WHERE np.person.id = :personId")
    Date getBornDate(@Param("personId") Long personId);

    @Query("SELECT np.full_Name FROM Natural_Person np WHERE np.person.id = :personId")
    String getFullNameByPersonId(@Param("personId") Long personId);
}
