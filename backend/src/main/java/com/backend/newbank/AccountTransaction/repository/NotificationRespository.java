package com.backend.newbank.AccountTransaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.newbank.AccountTransaction.entity.Notification;

import java.util.List;

public interface NotificationRespository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountId(Long id);
    void deleteAllByAccountId(Long id);
}
