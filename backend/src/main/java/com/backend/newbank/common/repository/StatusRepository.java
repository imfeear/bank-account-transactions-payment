package com.backend.newbank.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.newbank.common.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Short> {
    Status findStatusByCode(Short code);
    Status findByCode(Short code);
}
