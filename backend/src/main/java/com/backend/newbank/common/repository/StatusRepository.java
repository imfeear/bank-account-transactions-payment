package com.backend.jalabank.common.repository;

import com.backend.jalabank.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Short> {
    Status findStatusByCode(Short code);
    Status findByCode(Short code);
}
